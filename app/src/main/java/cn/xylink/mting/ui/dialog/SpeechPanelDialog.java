package cn.xylink.mting.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import cn.xylink.mting.R;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.event.RecycleEvent;
import cn.xylink.mting.speech.event.SpeechProgressEvent;
import cn.xylink.mting.speech.event.SpeechStopEvent;
import cn.xylink.mting.utils.ContentManager;

public class SpeechPanelDialog extends Dialog {
    Context context;
    WeakReference<Context> contextWeakReference;
    WeakReference<SpeechService> speechServiceWeakReference;
    SeekBar seekBar;

    TextView tvTitle;
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

        Window dialogWindow = this.getWindow();
        dialogWindow.setWindowAnimations(R.style.share_animation);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

        renderPanelView();
        EventBus.getDefault().register(this);
    }


    protected void renderPanelView() {
        if(speechServiceWeakReference.get() == null) {
            return;
        }

        if(speechServiceWeakReference.get().getSelected() != null) {
            tvTitle.setText(speechServiceWeakReference.get().getSelected().getTitle());
        }

        switch (speechServiceWeakReference.get().getState()) {
            case Paused:
            case Loadding:
            case Playing:
                seekBar.setProgress((int)(100 * speechServiceWeakReference.get().getProgress()));
            case Error:
                break;
            case Ready:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechEvent(RecycleEvent event) {
        if(event instanceof SpeechProgressEvent) {

        }
        renderPanelView();
    }

    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }
}
