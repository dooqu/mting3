package cn.xylink.mting.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.event.SpeechEvent;
import cn.xylink.mting.speech.event.SpeechBufferingEvent;
import cn.xylink.mting.speech.event.SpeechProgressEvent;
import cn.xylink.mting.speech.event.SpeechSerieLoaddingEvent;
import cn.xylink.mting.speech.event.SpeechStartEvent;
import cn.xylink.mting.speech.event.SpeechStopEvent;

public class SpeechPanelDialog extends Dialog implements SeekBar.OnSeekBarChangeListener {
    Context context;
    WeakReference<Context> contextWeakReference;
    WeakReference<SpeechService> speechServiceWeakReference;
    SeekBar seekBar;

    TextView tvTitle;
    View buttonClose;
    View buttonPlay;
    ImageView icoPlay;
    ProgressBar progressBar;
    boolean isPlaying;
    boolean seekBarIsSlideByUser = false;
    public SpeechPanelDialog(@NonNull Context context, SpeechService speechService) {
        super(context, R.style.bottom_dialog);
        contextWeakReference = new WeakReference<Context>(context);
        speechServiceWeakReference = new WeakReference<>(speechService);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_control_panel);

        Article articlePlaying = speechServiceWeakReference.get().getSelected();
        SpeechService.SpeechServiceState currentState = speechServiceWeakReference.get().getState();
        boolean seekBarEnabled = currentState != SpeechService.SpeechServiceState.Loadding && currentState != SpeechService.SpeechServiceState.Ready && articlePlaying != null;
        float currentProgress = seekBarEnabled? articlePlaying.getProgress() : 0f;
        tvTitle = findViewById(R.id.dialog_panel_article_title);
        seekBar = findViewById(R.id.dialog_panel_seekbar);
        buttonClose = findViewById(R.id.dialog_panel_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buttonPlay = findViewById(R.id.view_dialog_panel_play);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayButtonClick(v);
            }
        });
        icoPlay = findViewById(R.id.img_dialog_panel_play);
        progressBar = findViewById(R.id.dialog_panel_progress);
        seekBar.setOnSeekBarChangeListener(this);
        //progressBar.setVisibility(currentState == SpeechService.SpeechServiceState.Loadding? View.VISIBLE : View.INVISIBLE);
        //seekBar.setEnabled(currentState != SpeechService.SpeechServiceState.Loadding && currentState != SpeechService.SpeechServiceState.Ready && articlePlaying != null);
        //seekBar.setProgress((int)(currentProgress * 100));
        Window dialogWindow = this.getWindow();
        dialogWindow.setWindowAnimations(R.style.share_animation);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

        validatePanelView(null);

        if(EventBus.getDefault().isRegistered(this) == false) {
            EventBus.getDefault().register(this);
        }
    }

    protected void onPlayButtonClick(View v) {
        if(isPlaying) {
            speechServiceWeakReference.get().pause();
        }
        else {
            speechServiceWeakReference.get().resume();
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser == false) {
            return;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBarIsSlideByUser = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        seekBarIsSlideByUser = false;
        SpeechService speechService = speechServiceWeakReference.get();
        if(speechService != null &&
                speechService.getSelected() != null &&
                speechService.getState() != SpeechService.SpeechServiceState.Loadding &&
                speechService.getState() != SpeechService.SpeechServiceState.Ready) {
            speechService.seek((float)seekBar.getProgress() / 100f);
        }

    }


    protected void validatePanelView(SpeechEvent speechEvent) {
        SpeechService speechService = speechServiceWeakReference.get();
        if(speechService == null) {
            return;
        }
        Article currentArticle = speechService.getSelected();
        if(currentArticle != null) {
            tvTitle.setText(currentArticle.getTitle());
        }
        else {
            tvTitle.setText("正在加载...");
        }
        switch (speechService.getState()) {
            case Loadding:
                displayLoaddingAnim(true);
                enableSeekbar(false);
                setPlayingState(true);
                break;
            case Playing:
                enableSeekbar(true);
                setPlayingState(true);
                if(seekBarIsSlideByUser == false && !(speechEvent instanceof  SpeechBufferingEvent)) {
                    seekBar.setProgress((int) (100 * speechService.getProgress()));
                }
                break;
            case Paused:
                enableSeekbar(true);
                seekBar.setProgress((int) (100 * speechService.getProgress()));
            case Ready:
            case Error:
                displayLoaddingAnim(false);
                setPlayingState(false);
                break;
        }
    }


    protected void setPlayingState(boolean isPlaying) {
        this.isPlaying = isPlaying;
        icoPlay.setImageResource(isPlaying? R.mipmap.ico_dialog_pause : R.mipmap.ico_dialog_play);
    }

    protected void displayLoaddingAnim(boolean display) {
        progressBar.setVisibility(display? View.VISIBLE : View.INVISIBLE);
    }


    protected void enableSeekbar(boolean isEnabled) {
        seekBar.setEnabled(isEnabled);
        if(isEnabled == false) {
            seekBar.setProgress(0);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechEvent(SpeechEvent event) {
        validatePanelView(event);
        if(event instanceof SpeechProgressEvent) {
            displayLoaddingAnim(false);
            seekBar.setEnabled(true);
        }
        else if(event instanceof SpeechBufferingEvent) {
            //如果是SpeechStartEvent 或者 BufferEvent，就显示loadding
            displayLoaddingAnim(true);
        }
        else if(event instanceof SpeechStopEvent) {
            if(((SpeechStopEvent) event).getStopReason() == SpeechStopEvent.StopReason.ListIsNull) {
                seekBar.setProgress(0);
                seekBar.setEnabled(false);
            }
        }
    }


    @Override
    public void dismiss() {
        if(EventBus.getDefault().isRegistered(this) == true) {
            EventBus.getDefault().unregister(this);
        }
        super.dismiss();
    }
}
