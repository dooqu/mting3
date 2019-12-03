package cn.xylink.mting.speech;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import cn.xylink.mting.BuildConfig;
import cn.xylink.mting.MTing;
import cn.xylink.mting.speech.data.XiaoIceTTSAudioLoader;

public abstract class XiaoIceSpeechor implements Speechor {

    static String TAG = XiaoIceSpeechor.class.getSimpleName();

    enum SpeechTextFragmentState {
        TextReady,
        AudioLoadding,
        AudioReady,
        Error
    }

    public class SpeechTextFragment {
        String fragmentText;
        String audioUrl;
        SpeechTextFragmentState fragmentState;
        String errorMessage;
        int retryCount = 0;
        int frameIndex;
        long seekTime;
        boolean firstFragment;


        public SpeechTextFragment() {
            this.fragmentState = SpeechTextFragmentState.TextReady;
        }

        public String getFragmentText() {
            return fragmentText;
        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public SpeechTextFragmentState getFragmentState() {
            return fragmentState;
        }

        public void setAudioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
        }

        public void setFragmentState(SpeechTextFragmentState fragmentState) {
            this.fragmentState = fragmentState;
        }

        public void setFragmentText(String fragmentText) {
            this.fragmentText = fragmentText;
        }

        public int getFrameIndex() {
            return frameIndex;
        }

        public void setFrameIndex(int frameIndex) {
            this.frameIndex = frameIndex;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public long getSeekTime() {
            return seekTime;
        }

        public void setSeekTime(long seekTime) {
            this.seekTime = seekTime;
        }

        public boolean isFirstFragment() {
            return firstFragment;
        }

        public void setFirstFragment(boolean firstFragment) {
            this.firstFragment = firstFragment;
        }
    }


    public abstract class IceLoadResult implements TTSAudioLoader.LoadResult {
        SpeechTextFragment fragment;

        public IceLoadResult(SpeechTextFragment fragment) {
            this.fragment = fragment;
        }
    }


    int fragmentIndex;
    int fragmentIndexNext;
    boolean isReleased;
    List<String> textFragments;
    List<SpeechTextFragment> speechTextFragments;
    SpeechorState state;
    SpeechorSpeed speed;
    String speedInternal;
    SpeechHelper speechHelper;
    MediaPlayer mediaPlayer;
    boolean isSimulatePaused;
    TTSAudioLoader ttsAudioLoader;
    long seekTime;
    static String BufferErrorHint = "音频缓冲错误";


    public XiaoIceSpeechor() {
        state = SpeechorState.SpeechorStateReady;
        speed = SpeechorSpeed.SPEECH_SPEED_NORMAL;
        speedInternal = "0";
        speechHelper = new SpeechHelper();
        textFragments = new ArrayList<>();
        speechTextFragments = new ArrayList<>();

        //init mediaplay
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this::onMediaFragmentPrepared);
        mediaPlayer.setOnCompletionListener(this::onMediaFragmentComplete);
        mediaPlayer.setOnErrorListener(this::onMediaFragmentError);
        //init xiaoice tts loader
        ttsAudioLoader = new XiaoIceTTSAudioLoader();
        this.reset();
    }


    @Override
    public synchronized void prepare(String text) {
        if (isReleased) {
            return;
        }
        if (this.state != SpeechorState.SpeechorStateReady) {
            this.reset();
        }
        List<String> textFragmentsNew = speechHelper.prepareTextFragments(text, 90, false);
        this.textFragments.addAll(textFragmentsNew);

        for (int i = 0, size = textFragmentsNew.size(); i < size; i++) {
            SpeechTextFragment fragment = new SpeechTextFragment();
            fragment.setFragmentText(textFragmentsNew.get(i));
            fragment.setFragmentState(SpeechTextFragmentState.TextReady);
            fragment.setAudioUrl(null);
            speechTextFragments.add(fragment);
        }
    }


    @Override
    public synchronized int seek(int index) {
        synchronized (this) {
            if (isReleased) {
                return -SpeechError.TARGET_IS_RELEASED;
            }
            if (fragmentIndex < 0
                    || fragmentIndex >= this.textFragments.size()) {
                return -SpeechError.INDEX_OUT_OF_RANGE;
            }
            if (textFragments.size() == 0) {
                return -SpeechError.HAS_NO_FRAGMENTS;
            }
            if (this.state == SpeechorState.SpeechorStatePlaying) {
                mediaPlayer.stop();
            }

            this.fragmentIndex = index;
            this.fragmentIndexNext = fragmentIndex;
            ttsAudioLoader.cancelAll();
            resetFragmentsState(index);
            SpeechTextFragment firstFrgament = this.speechTextFragments.get(index);
            firstFrgament.setFirstFragment(true);

            if (seekAndPlay(index, 1) == true) {
                new Thread(() -> {
                    onStateChanged(SpeechorState.SpeechorStatePlaying);
                }).start();
                return index;
            }
            return -SpeechError.FRAGMENT_TTS_ERROR;
        }
    }


    private synchronized boolean seekAndPlay(int indexToPlay, int bufferSize) {
        Log.d(TAG, "seekAndPlay:" + indexToPlay + ",bufferSize=" + bufferSize + ", threaid=" + Thread.currentThread().getId());
        seekTime = System.currentTimeMillis();
        int segmentSize = this.textFragments.size();
        boolean seekResult = true;
        for (int startIndex = indexToPlay, endIndex = Math.min(startIndex + bufferSize, segmentSize); startIndex < endIndex; ++startIndex) {
            boolean isSegumentCurrentToPlay = startIndex == this.fragmentIndex;
            SpeechTextFragment fragment = this.speechTextFragments.get(startIndex);
            fragment.setFrameIndex(startIndex);
            fragment.setSeekTime(seekTime);
            switch (fragment.getFragmentState()) {
                case AudioLoadding:
                    if (isSegumentCurrentToPlay == true) {
                        if (this.state != SpeechorState.SpeechorStateBuffering) {
                            this.state = SpeechorState.SpeechorStateBuffering;
                            new Thread(() -> {
                                onStateChanged(SpeechorState.SpeechorStateBuffering);
                            }).start();
                        }
                        
                    }
                    break;

                case Error:
                    if (isSegumentCurrentToPlay == true) {
                        this.state = SpeechorState.SpeechorStateReady;
                        //此时fragmentText装载的是错误信息
                        this.onError(SpeechError.FRAGMENT_TTS_ERROR, BuildConfig.DEBUG ? fragment.getErrorMessage() : BufferErrorHint);
                    }
                    return false;

                case TextReady:
                    if (isSegumentCurrentToPlay == true) {
                        if (this.state != SpeechorState.SpeechorStateBuffering) {
                            this.state = SpeechorState.SpeechorStateBuffering;
                            new Thread(() -> {
                                onStateChanged(SpeechorState.SpeechorStateBuffering);
                            }).start();
                        }
                    }

                    Log.d(TAG, "start loading audio fragment: [loadIndex=" + startIndex + ", frameIndex=" + indexToPlay + "]");
                    TTSAudioLoader.LoadResult loadResult = new IceLoadResult(fragment) {
                        @Override
                        public void invoke(int errorCode, String message, String audioUrl) {
                            synchronized (XiaoIceSpeechor.this) {
                                if (isReleased == true) {
                                    return;
                                }
                                Log.d(TAG, "audio fragment loaded: [loadIndex =" + this.fragment.getFrameIndex() + ", indexToPlay=" + indexToPlay + ", errorCode=" + errorCode + ", threaid=" + Thread.currentThread().getId() + "]");
                                if (errorCode == 0) {
                                    boolean isFirstFragment = this.fragment.isFirstFragment();
                                    this.fragment.setFragmentState(SpeechTextFragmentState.AudioReady);
                                    this.fragment.setAudioUrl(audioUrl);
                                    if (XiaoIceSpeechor.this.seekTime != this.fragment.getSeekTime()) {
                                        Log.d(TAG, "---->callback token invalided;");
                                        return;
                                    }
                                    if (this.fragment.getFrameIndex() == XiaoIceSpeechor.this.fragmentIndex) {
                                        if (state == SpeechorState.SpeechorStateBuffering) {
                                            //定性
                                            state = SpeechorState.SpeechorStatePlaying;
                                            //play it;
                                            boolean isPlaying = playSegment(this.fragment.getFrameIndex());
                                            if (isPlaying == true && isFirstFragment == true && bufferSize == 1) {
                                                //此时buffer_size = 1;
                                                //算上index那个，就意味着在当前之后，加载了两个
                                                new Thread(() -> {
                                                    seekAndPlay(this.fragment.getFrameIndex() + 1, 2);
                                                }).start();
                                            }
                                        }
                                    }
                                }
                                else {
                                    //加载失败之后的逻辑分之
                                    if (++this.fragment.retryCount > Speechor.ERROR_RETRY_COUNT) {
                                        this.fragment.setFragmentState(SpeechTextFragmentState.Error);
                                        this.fragment.setErrorMessage("网络连接失败，请稍候重试:" + errorCode);

                                        if (this.fragment.getFrameIndex() == XiaoIceSpeechor.this.fragmentIndex) {

                                            if (state == SpeechorState.SpeechorStateBuffering) {
                                                state = SpeechorState.SpeechorStateReady;
                                                onError(errorCode, BuildConfig.DEBUG ? this.fragment.getErrorMessage() : BufferErrorHint);
                                            }
                                        }
                                    }
                                    else {
                                        if (state != SpeechorState.SpeechorStateReady) {
                                            ttsAudioLoader.textToSpeech(fragment.getFragmentText(), speed, this);
                                        }
                                    }
                                }
                            } // end synchornized
                        } // end invoke
                    };

                    ttsAudioLoader.textToSpeech(fragment.getFragmentText(), speed, loadResult);
                    fragment.setFragmentState(SpeechTextFragmentState.AudioLoadding);
                    break;

                case AudioReady:
                    if (isSegumentCurrentToPlay == true) {
                        state = SpeechorState.SpeechorStatePlaying;
                        if (playSegment(fragmentIndex) == false) {
                            return false;
                        }
                    }
                    break;
            }//end switch
        }
        return true;
    }


    private boolean playSegment(int segmentIndex) {
        try {
            SpeechTextFragment fragmentToPlay = speechTextFragments.get(segmentIndex);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fragmentToPlay.getAudioUrl());
            mediaPlayer.prepareAsync();
            return true;
        }
        catch (IOException ex) {
            onError(SpeechError.FRAGMENT_IO_ERROR, MTing.getInstance().isDebugMode() ? "media player 分片IO错误:" + ex.getMessage() : BufferErrorHint);
        }
        catch (NullPointerException ex) {
            SpeechTextFragment fragment = speechTextFragments.get(segmentIndex);
            onError(SpeechError.MEDIA_PLAYER_NULL_ERROR, MTing.getInstance().isDebugMode() ? "NullPointError:" + ex.getMessage() + ",source=" + speechTextFragments.get(segmentIndex).getAudioUrl() + ", text=" + speechTextFragments.get(segmentIndex).getFragmentText() : BufferErrorHint);
        }
        return false;
    }

    /*
    media player播放完一个媒体切片后进行回调；
     */
    private synchronized void onMediaFragmentComplete(MediaPlayer player) {
        //Log.d(TAG, "mediaPlay:onComplete:(state=" + state + ", fragmentIndex=" + fragmentIndex + ",size=" + textFragments.size() + ",frgtext=" + textFragments.get(fragmentIndex));
        /*
        在进入fragmentComplete之前枪入下一个分片的播放之前，要判定是否被用户的主控所中断；
        如果当前状态是playing，那继续播放下一个分片
        如果当前状态是paused，说明用户抢入了控制权， 进入假暂停状态
        其他情况，返回退出
         */
        switch (state) {
            case SpeechorStatePlaying:
                break;
            case SpeechorStatePaused:
                //如果被paused掉了，那么设定标志，直接返回
                this.isSimulatePaused = true;
            default:
                return;
        }
        SpeechTextFragment fragment = this.speechTextFragments.get(fragmentIndex);
        //一个切片播放完成之后，下一步的操作：
        //1、判断播放器是否被停止：可能在一个切片播放完成之后，被外部抢入控制权
        //2、当前还存在下一个切片
        ++fragmentIndex;
        int fragmentSize = speechTextFragments.size();

        if (fragmentIndex < fragmentSize) {
            seekAndPlay(fragmentIndex, fragment.isFirstFragment() ? 2 : 4);
        }
        else if (fragmentIndex == fragmentSize) {
            state = SpeechorState.SpeechorStateReady;
            fragmentIndex = 0;
            //在这里灵气线程进行事件通知，防止上层回调有锁调用防止死锁；
            new Thread(() -> {
                onStateChanged(SpeechorState.SpeechorStateReady);
            }).start();
        }
    }

    private void resetFragmentsState(int startIndex) {
        for (; startIndex < this.speechTextFragments.size(); startIndex++) {
            SpeechTextFragment fragment = this.speechTextFragments.get(startIndex);
            if (fragment.getFragmentState() == SpeechTextFragmentState.Error) {
                fragment.setFragmentState(SpeechTextFragmentState.TextReady);
                fragment.setFirstFragment(false);
            }
        }
    }


    private void clearCachedFragmentsAudio() {
        for (int index = 0, size = this.speechTextFragments.size(); index < size; ++index) {
            this.speechTextFragments.get(index).setFragmentState(SpeechTextFragmentState.TextReady);
            this.speechTextFragments.get(index).setAudioUrl(null);
            this.speechTextFragments.get(index).setFirstFragment(false);
        }
    }
    /*
    media player完成一个媒体切片加载后进行回调调用
     */
    private synchronized void onMediaFragmentPrepared(MediaPlayer mp) {
        Log.d(TAG, "play fragment at:{" + fragmentIndex + "},duration=" + mp.getDuration());
        if (state == SpeechorState.SpeechorStatePlaying) {
            new Thread(() -> {
                onProgress(textFragments, fragmentIndex);
                mp.start();
            }).start();
        }
    }

    /*
    media player在播放中遇到播放错误后，进行回调调用
     */
    private synchronized boolean onMediaFragmentError(MediaPlayer mp, int what, int extra) {
        new Thread(() -> {
            onError(SpeechError.MEDIA_PLAYER_ERROR, "播放器错误");
        });
        return false;
    }


    @Override
    public synchronized boolean pause() {
        if (isReleased) {
            return false;
        }
        switch (state) {
            case SpeechorStateBuffering:
                this.isSimulatePaused = true;
            case SpeechorStatePlaying:
                mediaPlayer.pause();
                this.state = SpeechorState.SpeechorStatePaused;
                return true;
        }
        return false;
    }


    @Override
    public synchronized boolean resume() {
        if (isReleased) {
            return false;
        }
        if (this.state != SpeechorState.SpeechorStatePaused) {
            return false;
        }

        if (this.isSimulatePaused == true) {
            this.isSimulatePaused = false;
            seekAndPlay(fragmentIndex, 1);
            //不设定状态，状态由seekAndPlay确定
            return true;
        }
        else {
            int fragSize = speechTextFragments.size();
            if (fragSize > 0 && fragmentIndex < fragSize &&
                    speechTextFragments.get(fragmentIndex).getAudioUrl() != null &&
                    speechTextFragments.get(fragmentIndex).getFragmentState() == SpeechTextFragmentState.AudioReady) {
                mediaPlayer.start();
                this.state = SpeechorState.SpeechorStatePlaying;
                return true;
            }
            else {
                return false;
            }
        }
    }


    @Override
    public synchronized void stop() {
        if (this.isReleased) {
            return;
        }

        if (state != SpeechorState.SpeechorStateReady) {
            state = SpeechorState.SpeechorStateReady;
            ttsAudioLoader.cancelAll();
            mediaPlayer.stop();
        }
    }


    @Override
    public synchronized void reset() {
        if (this.isReleased) {
            return;
        }
        SpeechorState currState = this.state;
        this.state = SpeechorState.SpeechorStateReady;
        this.fragmentIndex = 0;
        this.fragmentIndexNext = 0;
        this.isSimulatePaused = false;

        if (this.textFragments != null) {
            this.textFragments.clear();
        }
        this.textFragments = new ArrayList<>();

        if (this.speechTextFragments != null) {
            this.speechTextFragments.clear();
        }
        this.textFragments = new ArrayList<>();

        if (currState == SpeechorState.SpeechorStatePlaying) {
            mediaPlayer.reset();
            ttsAudioLoader.cancelAll();
            this.onStateChanged(SpeechorState.SpeechorStateReady);
        }
    }

    @Override
    public synchronized void release() {
        if (isReleased) {
            return;
        }

        state = SpeechorState.SpeechorStateReady;
        ttsAudioLoader.cancelAll();

        mediaPlayer.setOnErrorListener(null);
        mediaPlayer.setOnCompletionListener(null);
        mediaPlayer.setOnPreparedListener(null);

        if (this.state == SpeechorState.SpeechorStatePlaying) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        isReleased = true;
    }

    @Override
    public void setRole(SpeechorRole role) {
        if (state == SpeechorState.SpeechorStatePlaying || state == SpeechorState.SpeechorStatePaused) {
            mediaPlayer.stop();
            this.state = SpeechorState.SpeechorStateReady;
        }
    }

    @Override
    public synchronized void setSpeed(SpeechorSpeed speed) {
        if (this.speed == speed) {
            return;
        }
        switch (speed) {
            case SPEECH_SPEED_NORMAL:
                speedInternal = "0";
                break;

            case SPEECH_SPEED_MULTIPLE_1_POINT_5:
                speedInternal = "50";
                break;

            case SPEECH_SPEED_MULTIPLE_2:
                speedInternal = "100";
                break;

            case SPEECH_SPEED_MULTIPLE_2_POINT_5:
                speedInternal = "200";
                break;

            default:
                speedInternal = "0";
                break;
        }

        this.speed = speed;
        ttsAudioLoader.cancelAll();
        clearCachedFragmentsAudio();

        switch (state) {
            case SpeechorStateBuffering:
            case SpeechorStatePlaying:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                seek(fragmentIndex);
                break;

            case SpeechorStatePaused:
                break;
        }
    }

    @Override
    public SpeechorRole getRole() {
        return SpeechorRole.XiaoIce;
    }

    @Override
    public synchronized SpeechorSpeed getSpeed() {
        return speed;
    }

    @Override
    public synchronized SpeechorState getState() {
        return state;
    }

    @Override
    public synchronized int getFragmentIndex() {
        return fragmentIndex;
    }

    @Override
    public void setFragmentIndex(int fragmentIndex) {
        this.fragmentIndex = fragmentIndex;
    }

    @Override
    public synchronized List<String> getTextFragments() {
        return textFragments;
    }

    @Override
    public float getProgress() {
        synchronized (this) {
            if (this.textFragments.size() <= 0)
                return 0f;
            return (float) fragmentIndex / (float) this.textFragments.size();
        }
    }
}
