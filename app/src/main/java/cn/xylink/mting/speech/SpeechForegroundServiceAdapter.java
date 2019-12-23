package cn.xylink.mting.speech;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import cn.xylink.mting.MainActivity;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.event.StoreRefreshEvent;
import cn.xylink.mting.speech.data.ArticleDataProvider;
import cn.xylink.mting.speech.event.SpeechEvent;
import cn.xylink.mting.speech.event.SpeechFavorArticleEvent;
import cn.xylink.mting.speech.list.DynamicSpeechList;
import cn.xylink.mting.speech.list.SpeechList;

import static android.content.Context.NOTIFICATION_SERVICE;

/*
协助SpeechService完成两件事
1、提升至优先级更高的ForegroundService，或者关闭优先级
2、在适当的时候更新和关闭跟优先级服务绑定的Notification

将该逻辑拆分成独立的对象，与SpeechService进行逻辑分离
 */
public class SpeechForegroundServiceAdapter {

    static String TAG = SpeechForegroundServiceAdapter.class.getSimpleName();
    WeakReference<SpeechService> speechServiceWeakReference;
    static int executeCode = 0;

    public SpeechForegroundServiceAdapter(SpeechService service) {
        if (service != null) {
            speechServiceWeakReference = new WeakReference<>(service);
            IntentFilter notifIntent = new IntentFilter();
            notifIntent.addAction("SPEECH_ACTION_PLAY");
            notifIntent.addAction("SPEECH_ACTION_PAUSE");
            notifIntent.addAction("SPEECH_ACTION_NEXT");
            notifIntent.addAction("SPEECH_ACTION_RESUME");
            notifIntent.addAction("SPEECH_ACTION_FAVOR");
            notifIntent.addAction("SPEECH_ACTION_UNFAVOR");
            notifIntent.addAction("SPEECH_ACTION_EXIT");
            service.registerReceiver(notifReceiver, notifIntent);

            if (EventBus.getDefault().isRegistered(this) == false) {
                EventBus.getDefault().register(this);
            }
        }
    }


    public void retainForeground() {
        if (speechServiceWeakReference.get() == null) {
            stopForeground(true);
            return;
        }
        else if (speechServiceWeakReference.get().getSelected() == null) {
            //dismissNotif();
        }

        SpeechService service = speechServiceWeakReference.get();

        Article currentArticle = service.getSelected();
        boolean hasNext = service.hasNext();
        Intent intentNotifOpen = new Intent(service, MainActivity.class);
        intentNotifOpen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 1, intentNotifOpen, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(service)
                .setContentIntent(pendingIntent)
                //.setDeleteIntent(pendingIntentCancel)
                .setSmallIcon(R.mipmap.icon_notif)
                .setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.mipmap.icon_notify_logo))
                .setTicker(currentArticle != null? currentArticle.getTitle() : "轩辕听")
                .setContentTitle("轩辕听")
                .setContentText(currentArticle != null? currentArticle.getTitle() : "轩辕听")
                .setOngoing(true)
                .setAutoCancel(false)
                .setShowWhen(false);

        //>= android 8.0 设定foregroundService的前提是notification要创建channel，并关掉channel的sound
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "cn.xylink.mting";
            String channelName = "轩辕听";
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setSound(null, null);
            NotificationManager manager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            //设定builder的channelid
            builder.setChannelId(channelId);
        }

        Intent playIntent = new Intent("SPEECH_ACTION_PLAY");
        Intent resumeIntent = new Intent("SPEECH_ACTION_RESUME");
        Intent pauseIntent = new Intent("SPEECH_ACTION_PAUSE");
        Intent favIntent = new Intent("SPEECH_ACTION_FAVOR");
        Intent unFavIntent = new Intent("SPEECH_ACTION_UNFAVOR");
        Intent nextIntent = new Intent("SPEECH_ACTION_NEXT");
        Intent noneIntent = new Intent("SPEECH_ACTION_NULL");
        Intent exitIntent = new Intent("SPEECH_ACTION_EXIT");

        Notification.Action actionPlay = null, actionNext = null, actionFav = null, actionExit = null;

        boolean favorited = currentArticle != null && currentArticle.getStore() == 1;

        switch (service.getState()) {
            case Loadding:
                SpeechList speechList = service.getSpeechList();
                boolean isFirst = (speechList instanceof DynamicSpeechList && speechList.getFirst() != null && speechList.getFirst() == currentArticle);
                boolean isLast = (speechList instanceof DynamicSpeechList && speechList.getLast() != null && speechList.getLast() == currentArticle);
                boolean listIsNull = speechList.getCurrent() == null;
                actionFav = new Notification.Action(favorited ? R.mipmap.icon_favorited : R.mipmap.icon_unfavorited, "", PendingIntent.getBroadcast(service, ++executeCode, favorited ? unFavIntent : favIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionPlay = new Notification.Action(R.mipmap.icon_pause, "", PendingIntent.getBroadcast(service, ++executeCode, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionNext = new Notification.Action(R.mipmap.next, "", PendingIntent.getBroadcast(service, ++executeCode, (hasNext && !isLast ? nextIntent : noneIntent), PendingIntent.FLAG_UPDATE_CURRENT));
            case Playing:
                actionFav = new Notification.Action(favorited ? R.mipmap.icon_favorited : R.mipmap.icon_unfavorited, "", PendingIntent.getBroadcast(service, ++executeCode, favorited ? unFavIntent : favIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionPlay = new Notification.Action(R.mipmap.icon_pause, "", PendingIntent.getBroadcast(service, ++executeCode, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionNext = new Notification.Action(R.mipmap.next, "", PendingIntent.getBroadcast(service, ++executeCode, (hasNext ? nextIntent : noneIntent), PendingIntent.FLAG_UPDATE_CURRENT));
                break;

            case Paused:
                actionFav = new Notification.Action(favorited ? R.mipmap.icon_favorited : R.mipmap.icon_unfavorited, "", PendingIntent.getBroadcast(service, ++executeCode, favorited ? unFavIntent : favIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionPlay = new Notification.Action(R.mipmap.icon_playing, "", PendingIntent.getBroadcast(service, ++executeCode, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionNext = new Notification.Action(R.mipmap.next, "", PendingIntent.getBroadcast(service, ++executeCode, (hasNext ? nextIntent : noneIntent), PendingIntent.FLAG_UPDATE_CURRENT));
                break;

            case Error:
                actionFav = new Notification.Action(favorited ? R.mipmap.icon_favorited : R.mipmap.icon_unfavorited, "", PendingIntent.getBroadcast(service, ++executeCode, noneIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionPlay = new Notification.Action(R.mipmap.icon_playing, "", PendingIntent.getBroadcast(service, ++executeCode, playIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionNext = new Notification.Action(R.mipmap.next, "", PendingIntent.getBroadcast(service, ++executeCode, (hasNext ? nextIntent : noneIntent), PendingIntent.FLAG_UPDATE_CURRENT));
                break;

            default:
                return;
        }

        actionExit = new Notification.Action(R.mipmap.icon_dialog_close, "", PendingIntent.getBroadcast(service, ++executeCode, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        if (actionFav != null) {
            builder.addAction(actionFav);
        }

        if (actionPlay != null) {
            builder.addAction(actionPlay);
        }

        if (actionNext != null) {
            builder.addAction(actionNext);
        }

        builder.addAction(actionExit);

        Notification.MediaStyle mediaStyle = new Notification.MediaStyle();
        mediaStyle.setShowActionsInCompactView(1, 2, 3);
        builder.setStyle(mediaStyle);

        Notification notification = builder.build();
        service.startForeground(android.os.Process.myPid(), notification);
    }

    public void destroy() {
        this.stopForeground(true);
        SpeechService service = speechServiceWeakReference.get();
        if (service != null) {
            service.unregisterReceiver(notifReceiver);
        }
        EventBus.getDefault().unregister(this);
    }

    protected void dismissNotif() {
        SpeechService service = speechServiceWeakReference.get();
        if (service != null) {
            ((NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        }
    }

    public void stopForeground(boolean removeNotification) {
        SpeechService service = speechServiceWeakReference.get();
        if (service != null) {
            service.stopForeground(removeNotification);
            if (removeNotification == true) {
                dismissNotif();
            }
        }
    }

    private ArticleDataProvider.ArticleLoader<Article> favorCallback = new ArticleDataProvider.ArticleLoader<Article>() {
        @Override
        public void invoke(int errorCode, Article data) {
            if (errorCode == 0) {
                //SpeechFavorArticleEvent event = new SpeechFavorArticleEvent(data);
                StoreRefreshEvent event = new StoreRefreshEvent();
                event.setStroe(data.getStore());
                event.setArticleID(data.getArticleId());
                EventBus.getDefault().post(event);
                retainForeground();
            }
        }
    };



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArticleFavoriteChanged(StoreRefreshEvent event) {
        if (speechServiceWeakReference.get() != null
                && speechServiceWeakReference.get().getSelected() != null
                && speechServiceWeakReference.get().getState() == SpeechService.SpeechServiceState.Playing) {
            if (speechServiceWeakReference.get().getSelected().getArticleId().equals(event.getArticleID())) {
                speechServiceWeakReference.get().getSelected().setStore(event.getStroe());
                retainForeground();
            }
        }
    }

    private ArticleDataProvider articleDataProvider;

    private BroadcastReceiver notifReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SpeechService speechService = speechServiceWeakReference.get();
            if (speechService == null) {
                return;
            }
            synchronized (speechService) {
                final String action = intent.getAction();
                Article currentArticle = speechService.getSelected();
                if (currentArticle == null) {
                    return;
                }

                switch (action) {
                    case "SPEECH_ACTION_PLAY":
                        speechService.play(speechService.getSelected().getArticleId());
                        break;
                    case "SPEECH_ACTION_PAUSE":
                        speechService.pause();
                        break;
                    case "SPEECH_ACTION_RESUME":
                        speechService.resume();
                        break;
                    case "SPEECH_ACTION_NEXT":
                        switch (speechService.getState()) {
                            case Stoped:
                                if (speechService.getSelected() != null) {
                                    speechService.play(speechService.getSelected().getArticleId());
                                }
                                break;
                            default:
                                if (speechService.hasNext()) {
                                    speechService.playNext();
                                }
                                break;
                        }
                        break;

                    case "SPEECH_ACTION_FAVOR":
                        if (currentArticle.getStore() == 1) {
                            break;
                        }
                        if (speechServiceWeakReference.get() != null) {
                            articleDataProvider = new ArticleDataProvider(speechServiceWeakReference.get());
                            articleDataProvider.favorArticle(currentArticle, favorCallback);
                        }
                        break;
                    case "SPEECH_ACTION_UNFAVOR":
                        if (currentArticle.getStore() == 0) {
                            break;
                        }
                        if (speechServiceWeakReference.get() != null) {
                            articleDataProvider = new ArticleDataProvider(speechServiceWeakReference.get());
                            articleDataProvider.unfavorArticle(currentArticle, favorCallback);
                        }

                        break;
                    case "SPEECH_ACTION_EXIT":
                        speechService.pause();
                        stopForeground(true);
                        context.stopService(new Intent(context, SpeechService.class));
                        break;
                } // end switch
            } // end sychornized
        } // end onReceive
    }; // end class

}
