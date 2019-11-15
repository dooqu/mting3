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
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.event.SpeechEvent;
import cn.xylink.mting.speech.event.SpeechBufferingEvent;
import cn.xylink.mting.speech.event.SpeechProgressEvent;
import cn.xylink.mting.speech.event.SpeechSerieLoaddingEvent;
import cn.xylink.mting.speech.event.SpeechStartEvent;

public class SpeechPanelDialog extends Dialog {
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
    public SpeechPanelDialog(@NonNull Context context, SpeechService speechService) {
        super(context, R.style.bottom_dialog);
        contextWeakReference = new WeakReference<Context>(context);
        speechServiceWeakReference = new WeakReference<>(speechService);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_control_panel);
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

        Window dialogWindow = this.getWindow();
        dialogWindow.setWindowAnimations(R.style.share_animation);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

        renderPanelView();
        EventBus.getDefault().register(this);
    }

    protected void onPlayButtonClick(View v) {
        if(isPlaying) {
            speechServiceWeakReference.get().pause();
        }
        else {
            speechServiceWeakReference.get().resume();
        }
    }


    protected void renderPanelView() {
        if(speechServiceWeakReference.get() == null) {
            return;
        }
        if(speechServiceWeakReference.get().getSelected() != null) {
            tvTitle.setText(speechServiceWeakReference.get().getSelected().getTitle());
        }
        switch (speechServiceWeakReference.get().getState()) {
            case Loadding:
            case Playing:
                setPlayButton(true);
                seekBar.setProgress((int)(100 * speechServiceWeakReference.get().getProgress()));
                break;
            case Paused:
            case Error:
            case Ready:
                setProgress(false);
                setPlayButton(false);
                break;
        }
    }

    protected void setPlayButton(boolean isPlaying) {
        this.isPlaying = isPlaying;
        icoPlay.setImageResource(isPlaying? R.mipmap.ico_dialog_pause : R.mipmap.ico_dialog_play);
    }

    protected void setProgress(boolean display) {
        progressBar.setVisibility(display? View.VISIBLE : View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechEvent(SpeechEvent event) {
        if(event instanceof SpeechProgressEvent) {
            setProgress(false);
        }
        else if(event instanceof SpeechBufferingEvent) {
            //如果是SpeechStartEvent 或者 BufferEvent，就显示loadding
            setProgress(true);
        }
        else if(event instanceof SpeechStartEvent) {
            setProgress(true);
        }
        else if(event instanceof SpeechSerieLoaddingEvent) {
            setProgress(true);
        }
        renderPanelView();
    }

    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }
}
