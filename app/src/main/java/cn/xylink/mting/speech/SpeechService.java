package cn.xylink.mting.speech;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import cn.xylink.mting.bean.Article;
import cn.xylink.mting.speech.data.ArticleDataProvider;
import cn.xylink.mting.speech.list.DynamicSpeechList;
import cn.xylink.mting.speech.list.SpeechList;
import cn.xylink.mting.speech.event.*;
import cn.xylink.mting.speech.list.UnreadSpeechList;


public class SpeechService extends Service {
    static String TAG = SpeechService.class.getSimpleName();

    /*SpeechService的状态描述类型*/
    public enum SpeechServiceState {
        /*正文准备就绪准备就绪*/
        Ready,
        /*播放中*/
        Playing,
        /*暂停中*/
        Paused,
        /*播放完成*/
        //Stoped,
        /*加载中*/
        Loadding,
        /*发生错误*/
        Error
    }


    public enum SpeechListType {
        Dynamic,
        Unread
    }

    /*
    定时器类型，用于service.setCountDown
     */
    public enum CountDownMode {
        /*定时器关闭*/
        None,
        /*播放数量定时器*/
        NumberCount,
        /*分钟定时器*/
        MinuteCount
    }

    /*TTS 播报接口对象*/
    Speechor speechor;

    /*binder 对象，用来返回service对象*/
    IBinder binder = new SpeechBinder();

    /*Speech 算法的常用函数*/
    SpeechHelper helper = new SpeechHelper();

    /*播放列表，speechList是对全局SpeechList对象的引用*/
    SpeechList speechList = new DynamicSpeechList();

    /*SpeechService的状态*/
    SpeechServiceState serviceState;

    /*常用对Article的网络操作类*/
    ArticleDataProvider articleDataProvider;


    SpeechNotification speechNotification;

    /*倒计时数值，可以表示倒计时的分钟数，也可以表示倒计时的播放数*/
    int countdownValue;

    /*指示countdownValue的类型*/
    CountDownMode countDownMode;

    /*分钟倒计时要使用的Timer，没分钟递减一次*/
    Timer countdownTimer;


    //NotificationManager notificationManager;

    boolean isForegroundService;

    static int executeCode = 0;

    boolean isReleased;

    boolean isSimulatePaused;

    long speechDurationOfArticle;
    long speechStartTimeOfArticle;
    String speechArticleIdOfArticle;


    public class SpeechBinder extends Binder {
        public SpeechService getService() {
            return SpeechService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initService();
        initReceiver();
        setRole(Speechor.SpeechorRole.XiaoYao);
        speechNotification = new SpeechNotification(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SpeechService", "onStartCommand");

        if (intent != null) {
            String roleValue = intent.getStringExtra("role");
            String speedValue = intent.getStringExtra("speed");
            Speechor.SpeechorRole currRole = roleValue != null && roleValue.trim() != "" ? Speechor.SpeechorRole.valueOf(roleValue) : Speechor.SpeechorRole.XiaoIce;
            Speechor.SpeechorSpeed currSpeed = speedValue != null && speedValue.trim() != "" ? Speechor.SpeechorSpeed.valueOf(speedValue) : Speechor.SpeechorSpeed.SPEECH_SPEED_NORMAL;
            this.setRole(currRole);
            this.setSpeed(currSpeed);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "SpeechService.onDestroy");
        super.onDestroy();
        unregisterReceiver(notifReceiver);
        unregisterReceiver(a2dpReceiver);
        isReleased = true;
        speechor.reset();
        speechor.release();
        //articleDataProvider.release();
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
        //this.stopForeground(true);
        speechNotification.stopAndRemove();
    }


    private void initService() {
        isForegroundService = false;
        isReleased = false;
        serviceState = SpeechServiceState.Ready;
        articleDataProvider = new ArticleDataProvider(this);

        speechor = new SpeechEngineWrapper(this) {
            @Override
            public void onStateChanged(SpeechorState speakerState) {
                synchronized (SpeechService.this) {
                    if (isReleased) {
                        return;
                    }
                    Article currentArticle = SpeechService.this.getSelected();
                    if (currentArticle == null) {
                        return;
                    }

                    if (speakerState == SpeechorState.SpeechorStateReady) {
                        Log.d(TAG, "SpeechService.onStateChanged:Ready");

                        currentArticle.setProgress(1);

                        SpeechService.this.onSpeechEnd(currentArticle, 1, true);

                        SpeechStopEvent.StopReason reason = SpeechStopEvent.StopReason.ListIsNull;

                        if (SpeechService.this.countDownMode == CountDownMode.NumberCount && --countdownValue == 0) {
                            SpeechService.this.cancelCountDown();
                            reason = SpeechStopEvent.StopReason.CountDownToZero;
                        }

                        else if (SpeechService.this.hasNext()) {
                            SpeechService.this.playNextInvokeByInternal();
                            return;
                        }

                        SpeechService.this.moveNext();

                        serviceState = SpeechServiceState.Ready;
                        onSpeechStoped(reason);
                    }
                    else if (speakerState == SpeechorState.SpeechorStateLoadding) {
                        onSpeechBuffering(currentArticle);
                    }
                }
            }

            @Override
            public void onProgress(List<String> textFragments, int index) {
                synchronized (SpeechService.this) {
                    if (speechList.getCurrent() == null) {
                        return;
                    }
                    onSpeechProgress(speechList.getCurrent(), index, textFragments);
                }
            }

            @Override
            public void onError(int errorCode, String message) {
                synchronized (SpeechService.this) {
                    SpeechService.this.serviceState = SpeechServiceState.Error;
                    onSpeechError(errorCode, message, speechList.getCurrent());
                }
            }
        };

    }

    private void initReceiver() {
        IntentFilter notifIntent = new IntentFilter();
        notifIntent.addAction("play");
        notifIntent.addAction("pause");
        notifIntent.addAction("next");
        notifIntent.addAction("resume");
        notifIntent.addAction("favorite");
        notifIntent.addAction("unfavorite");
        notifIntent.addAction("exit");
        registerReceiver(notifReceiver, notifIntent);
        IntentFilter a2dpIntent = new IntentFilter(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(a2dpReceiver, a2dpIntent);
    }


    private void onSpeechStart(Article article) {
        speechNotification.update();
        //initNotification();
        EventBus.getDefault().post(new SpeechStartEvent(article));

    }

    private void onSpeechReady(Article article) {
        speechDurationOfArticle = System.currentTimeMillis();
        speechDurationOfArticle = 0;
        speechStartTimeOfArticle = new java.util.Date().getTime();
        speechArticleIdOfArticle = article.getArticleId();
        EventBus.getDefault().post(new SpeechReadyEvent(article));
    }

    private void onSpeechProgress(Article article, int fragmentIndex, List<String> fragments) {
        article.setProgress((float) fragmentIndex / (float) fragments.size());
        //if(fragmentIndex == 0) {
        speechNotification.update();
        // }
        EventBus.getDefault().post(new SpeechProgressEvent(fragmentIndex, fragments, article));
    }

    private void onSpeechBuffering(Article article) {
        EventBus.getDefault().post(new SpeechBufferingEvent(getSpeechorFrameIndex(), getSpeechorTextFragments(), article));
    }

    private void onSpeechError(int errorCode, String message, Article article) {
        EventBus.getDefault().post(new SpeechErrorEvent(errorCode, message, article));
        speechNotification.update();
    }

    private void onSpeechEnd(Article article, float progress, boolean deleteFromList) {
        //与云端同步数据状态
        //articleDataProvider.readArticle(article, progress, deleteFromList, ((errorCode, articleResult) -> {
        //EventBus.getDefault().post(new SpeechArticleStatusSavedOnServerEvent(errorCode, "", articleResult));
        //}));

        if (progress == 1) {
            EventBus.getDefault().post(new SpeechEndEvent(article, progress));
        }

        long duration = new java.util.Date().getTime() - speechStartTimeOfArticle;
        speechDurationOfArticle += (duration > 0) ? duration : 0;
        //articleDataProvider.appendArticleRecord(speechArticleIdOfArticle, speechDurationOfArticle / 1000);
    }

    private void onSpeechPause(Article article) {
        EventBus.getDefault().post(new SpeechPauseEvent(article));
        long duration = new java.util.Date().getTime() - speechStartTimeOfArticle;
        speechDurationOfArticle += (duration > 0) ? duration : 0;
    }

    private void onSpeechResume(Article article) {
        EventBus.getDefault().post(new SpeechResumeEvent(article));
        speechStartTimeOfArticle = new java.util.Date().getTime();
    }


    private void onSpeechStoped(SpeechStopEvent.StopReason reason) {
        EventBus.getDefault().post(new SpeechStopEvent(reason));
        //notificationManager.cancelAll();
        if (reason == SpeechStopEvent.StopReason.ListIsNull) {
            //this.stopForeground(true);
            speechNotification.stopAndRemove();
        }
    }


    public synchronized SpeechServiceState getState() {
        return serviceState;
    }


    public synchronized void setCountDown(CountDownMode mode, int tickcountValue) {

        this.cancelCountDown();
        if (tickcountValue <= 0 || mode == CountDownMode.None) {
            return;
        }

        this.countDownMode = mode;
        this.countdownValue = tickcountValue;

        if (mode == CountDownMode.MinuteCount) {
            countdownTimer = new Timer();
            countdownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (SpeechService.this) {
                        if (--SpeechService.this.countdownValue == 0
                                && (getState() == SpeechServiceState.Playing || getState() == SpeechServiceState.Loadding)) {
                            SpeechService.this.pause();
                            SpeechService.this.cancelCountDown();
                            SpeechService.this.onSpeechStoped(SpeechStopEvent.StopReason.CountDownToZero);
                        }
                    }
                }
            }, 1000 * 60, 1000 * 60);
        }
    }

    public synchronized void cancelCountDown() {
        if (this.countDownMode == CountDownMode.MinuteCount) {
            if (this.countdownTimer != null) {
                this.countdownTimer.cancel();
                this.countdownTimer = null;
            }
        }
        this.countDownMode = CountDownMode.None;
        this.countdownValue = 0;
    }


    public synchronized CountDownMode getCountDownMode() {
        return this.countDownMode;
    }


    public synchronized int getCountDownValue() {
        return this.countdownValue;
    }


    public synchronized int seek(float percentage) {
        //如果当前播放不存在，那返回错误
        if (speechList.getCurrent() == null) {
            return -SpeechError.NOTHING_TO_PLAY;
        }
        SpeechServiceState preState = serviceState;

        if (serviceState == SpeechServiceState.Ready && serviceState == SpeechServiceState.Loadding) {
            return -SpeechError.SEEK_NOT_ALLOW;
        }
        if (getSelected() == null || getSelected().getTextBody() == null) {
            return -SpeechError.SEEK_NOT_ALLOW;
        }
        int index;
        if ((index = helper.seekFragmentIndex(percentage, speechor.getTextFragments())) >= 0) {
            int result = speechor.seek(index);
            if (result >= 0) {
                this.serviceState = SpeechServiceState.Playing;
                if (preState == SpeechServiceState.Paused || preState == SpeechServiceState.Error) {
                    onSpeechResume(getSelected());
                }
            }
            return result;
        }
        return index;
    }


    /*
    pause的结果，可能导致当前状态为Paused或者Ready
     */
    public synchronized boolean pause() {
        if (speechList.getCurrent() == null) {
            return false;
        }

        boolean result = false;
        switch (serviceState) {
            case Loadding:
                isSimulatePaused = true;
                //do not break;
            case Playing:
                this.serviceState = SpeechServiceState.Paused;
                result = this.speechor.pause();
                speechNotification.update();
                onSpeechPause(speechList.getCurrent());
                return true;
        }
        return false;
    }


    public synchronized boolean resume() {
        if (speechList.getCurrent() == null) {
            return false;
        }

        boolean result = false;
        if (serviceState == SpeechServiceState.Paused) {

            if (this.isSimulatePaused == true) {
                playSelected();
                speechNotification.update();
                //initNotification();
                onSpeechResume(speechList.getCurrent());
                return true;
            }
            else {
                result = this.speechor.resume();
                if (result) {
                    serviceState = SpeechServiceState.Playing;
                    speechNotification.update();
                    //initNotification();
                    onSpeechResume(speechList.getCurrent());
                }
                else {
                    result = seek(getProgress()) > 0;
                    speechNotification.update();
                    //onSpeechResume(speechList.getCurrent());
                }
                return result;
            }
        }
        return false;
    }

    private synchronized boolean playSelected() {
        if (speechList == null || speechList.getCurrent() == null)
            return false;

        prepareArticle(speechList.getCurrent(), false);
        return true;
    }

    public synchronized Article getSelected() {
        if (speechList == null || speechList.getCurrent() == null) {
            return null;
        }
        return speechList.getCurrent();
    }

    public synchronized void loadAndPlay(String broadcastId, String articleId) {
        ArticleDataProvider articleDataProvider = new ArticleDataProvider(this);
        if("-1".equals(broadcastId) == false) {
            articleDataProvider.getSpeechList(broadcastId, articleId, new ArticleDataProvider.ArticleLoader<List<Article>>() {
                @Override
                public void invoke(int errorCode, List<Article> data) {
                    if (errorCode == 0) {
                        synchronized (SpeechService.this) {
                            SpeechService.this.resetSpeechList(data, SpeechService.SpeechListType.Dynamic);
                            SpeechService.this.play(articleId);
                        }
                    }
                }
            });
        }
        else {
            articleDataProvider.getUnreadSpeechList(new ArticleDataProvider.ArticleLoader<List<Article>>() {
                @Override
                public void invoke(int errorCode, List<Article> data) {
                    if (errorCode == 0) {
                        synchronized (SpeechService.this) {
                            SpeechService.this.resetSpeechList(data, SpeechListType.Unread);
                            SpeechService.this.play(articleId);
                        }
                    }
                }
            });
        }
    }


    public synchronized Article play(String articleId) {
        if (articleId == null || speechList == null) {
            return null;
        }
        Article previousArt = this.speechList.getCurrent();
        if (previousArt != null && articleId.equals(previousArt.getArticleId()) == false) {
            if (previousArt.getProgress() != 1) {
                this.onSpeechEnd(previousArt, previousArt.getProgress(), false);
            }
        }

        Article article = this.speechList.select(articleId);
        if (article != null) {
            prepareArticle(article, false);
        }
        return article;
    }


    public synchronized Article pushFrontToSpeechListAndPlay(Article article) {
        Article previousArt = this.speechList.getCurrent();
        if (previousArt != null && article.getArticleId().equals(previousArt.getArticleId()) == false) {
            if (previousArt.getProgress() != 1) {
                this.onSpeechEnd(previousArt, previousArt.getProgress(), false);
            }
        }

        List<Article> list = new ArrayList<>();
        list.add(article);
        this.speechList.pushFront(list);
        Article artcleSelected = this.speechList.select(article.getArticleId());
        if (artcleSelected != null) {
            prepareArticle(article, false);
        }

        return artcleSelected;
    }


    public synchronized void setSpeed(Speechor.SpeechorSpeed speed) {
        this.speechor.setSpeed(speed);
    }

    public synchronized Speechor.SpeechorSpeed getSpeed() {
        return this.speechor.getSpeed();
    }

    public synchronized SpeechList getSpeechList() {
        return this.speechList;
    }


    private void prepareArticle(final Article article, boolean needSourceEffect) {
        if (speechList.getCurrent() != null && serviceState == SpeechServiceState.Playing) {
            this.speechor.stop();
        }

        this.serviceState = SpeechServiceState.Loadding;
        this.onSpeechStart(article);
        boolean isFirst = (speechList instanceof DynamicSpeechList && speechList.getFirst() != null && speechList.getFirst() == article);
        boolean isLast = (speechList instanceof DynamicSpeechList && speechList.getLast() != null && speechList.getLast() == article);

        this.articleDataProvider.loadArticleAndList(article, needSourceEffect, isFirst, isLast, (int errorcode, ArticleDataProvider.ArticleListArgument responseResult) -> {

            synchronized (this) {

                if (isReleased || serviceState != SpeechServiceState.Loadding || responseResult.article != this.speechList.getCurrent()) {
                    return;
                }

                if (errorcode != 0) {
                    this.serviceState = SpeechServiceState.Error;
                    this.onSpeechError(SpeechError.ARTICLE_LOAD_ERROR, "文章正文加载失败", article);
                    return;
                }

                if (responseResult.list != null && responseResult.list.size() > 0) {
                    if (isFirst) {
                        speechList.pushFront(responseResult.list);
                    }
                    else if (isLast) {
                        speechList.pushBack(responseResult.list);
                    }
                }

                this.onSpeechReady(article);
                speechor.reset();
                speechor.prepare(article.getTitle());
                speechor.prepare(article.getTextBody());

                int fragmentSize = speechor.getTextFragments().size();
                int destFragIndex = helper.seekFragmentIndex(article.getProgress(), speechor.getTextFragments());

                if (destFragIndex >= fragmentSize) {
                    destFragIndex = fragmentSize - 1;
                }

                if (speechor.seek(destFragIndex) >= 0) {
                    this.serviceState = SpeechServiceState.Playing;
                }
                else {
                    this.serviceState = SpeechServiceState.Ready;
                }
            } // end synchonized
        });
    }

    private boolean moveNext() {
        return speechList.moveNext();
    }


    public synchronized boolean playNext() {
        if (speechList.getCurrent() != null) {
            speechor.stop();
            this.onSpeechEnd(speechList.getCurrent(), speechList.getCurrent().getProgress(), true);
        }

        boolean nextExists = speechList.moveNext();
        if (nextExists) {
            prepareArticle(speechList.getCurrent(), false);
        }
        return nextExists;
    }


    private boolean playNextInvokeByInternal() {
        boolean nextExists = this.speechList.moveNext();
        if (nextExists) {
            prepareArticle(speechList.getCurrent(), true);
        }
        return nextExists;
    }

    public synchronized void setRole(Speechor.SpeechorRole role) {
        speechor.setRole(role);
    }


    public synchronized Speechor.SpeechorRole getRole() {
        return speechor.getRole();
    }


    public boolean hasNext() {
        return speechList.hasNext();
    }


    protected synchronized void pushFrontToSpeechList(List<Article> list) {
        if (this.speechList == null) {
            return;
        }

        this.speechList.pushFront(list);
    }


    protected synchronized void pushBackToSpeechList(List<Article> list) {
        if (this.speechList == null) {
            return;
        }
        this.speechList.pushBack(list);
    }

    public synchronized void resetSpeechList(List<Article> list, SpeechListType speechListType) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.clearSpeechList();
        switch (speechListType) {
            case Unread:
                this.speechList = new UnreadSpeechList(list);
                break;
            case Dynamic:
                this.speechList = new DynamicSpeechList(list);
                break;
        }
    }


    public synchronized void clearSpeechList() {
        boolean isSelectedDeleted = this.speechList.removeAll();
        if (isSelectedDeleted) {
            this.speechor.stop();
            this.serviceState = SpeechServiceState.Ready;
            this.onSpeechStoped(SpeechStopEvent.StopReason.ListIsNull);
        }
    }


    public synchronized void removeFromSpeechList(List<String> articleIds) {
        boolean isSelectedDeleted = this.speechList.removeSome(articleIds);
        //如果当前正在播放的被删除掉
        if (isSelectedDeleted) {
            this.speechor.stop();
            //this.onSpeechEnd(currentArt, currentArt.getProgress());
            //??是否还记录播放进度
            if (this.speechList.size() > 0) {

                Article article = this.speechList.selectFirst();
                prepareArticle(article, false);
            }
            else {
                this.serviceState = SpeechServiceState.Ready;
                //没有要播放的内容了
                this.onSpeechStoped(SpeechStopEvent.StopReason.ListIsNull);
            }
        }
    }


    public synchronized Article getSpeechListSelected() {
        return this.speechList != null ? this.speechList.getCurrent() : null;
    }


    public synchronized float getProgress() {
        return speechor.getProgress();
    }


    public synchronized List<String> getSpeechorTextFragments() {
        return speechor.getTextFragments();
    }

    public synchronized int getSpeechorFrameIndex() {
        return speechor.getFragmentIndex();
    }


    private BroadcastReceiver notifReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (SpeechService.this) {
                final String action = intent.getAction();
                Article currentArticle = getSelected();
                if (currentArticle == null) {
                    return;
                }

                switch (action) {
                    case "play":
                        if (getSelected() != null) {
                            playSelected();
                        }
                        break;

                    case "pause":
                        pause();
                        break;

                    case "resume":
                        resume();
                        break;

                    case "next":
                        switch (serviceState) {
                            case Ready:
                                if (getSelected() != null) {
                                    playSelected();
                                }
                                break;
                            default:
                                if (hasNext()) {
                                    playNext();
                                }
                                break;
                        }
                        break;

                    case "favorite":
                        if (currentArticle.getStore() == 1) {
                            break;
                        }
                        /*
                        articleDataProvider.favorite(currentArticle, true, ((errorCode, article) -> {
                            if (errorCode == 0) {
                                initNotification();
                                EventBus.getDefault().post(new FavoriteEvent(currentArticle));
                            }
                        }));
                         */
                    case "unfavorite":
                        if (currentArticle.getStore() == 0) {
                            break;
                        }
                        /*
                        articleDataProvider.favorite(currentArticle, false, ((errorCode, article) -> {
                            if (errorCode == 0) {
                                initNotification();
                                EventBus.getDefault().post(new FavoriteEvent(currentArticle));
                            }
                        }));

                         */
                        break;

                    case "exit":
                        pause();
                        speechNotification.stopAndNotRemove();
                        // stopForeground(false);
                        // ((NotificationManager) SpeechService.this.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
                        break;
                } // end switch
            } // end sychornized
        } // end onReceive
    }; // end class

    private BroadcastReceiver a2dpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive action=" + action);
            switch (action) {
                case BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_DISCONNECTED);
                    Log.d("SPEECH_", "A2DP_Connection_State_Changed:state=" + state);
                    if (state == BluetoothA2dp.STATE_DISCONNECTED) {
                        if (serviceState == SpeechServiceState.Playing) {
                            pause();
                        }
                    }
                    break;
            }
        }
    };
}


