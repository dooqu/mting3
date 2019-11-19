package cn.xylink.mting.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import java.util.ArrayList;
import java.util.List;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.Speechor;
import cn.xylink.mting.speech.event.SpeechEvent;
import cn.xylink.mting.speech.event.SpeechBufferingEvent;
import cn.xylink.mting.speech.event.SpeechProgressEvent;
import cn.xylink.mting.speech.event.SpeechStartEvent;
import cn.xylink.mting.speech.event.SpeechStopEvent;
import cn.xylink.mting.ui.activity.BaseActivity;
import cn.xylink.mting.ui.adapter.ControlPanelAdapter;

import static cn.xylink.mting.speech.Speechor.SpeechorRole.XiaoIce;
import static cn.xylink.mting.speech.Speechor.SpeechorRole.XiaoYao;
import static cn.xylink.mting.speech.Speechor.SpeechorRole.XiaoYu;
import static cn.xylink.mting.speech.Speechor.SpeechorRole.YaYa;

public class SpeechPanelDialog extends Dialog implements SeekBar.OnSeekBarChangeListener, ViewPager.OnPageChangeListener {
    WeakReference<BaseActivity> contextWeakReference;
    WeakReference<SpeechService> speechServiceWeakReference;
    SeekBar seekBar;

    TextView tvTitle;
    View buttonClose;
    View buttonPlay;
    ImageView icoPlay;
    ProgressBar progressBar;
    boolean isPlaying;
    boolean seekBarIsSlideByUser = false;
    Article currentArticle;
    ViewPager viewPager;

    View controlView;
    View soundSettingView;
    View timetickcountView;

    ImageView portraitMasks[];
    ImageView portraits[];
    ProgressBar portraitProgress[];
    View portraitButton[];

    public SpeechPanelDialog(@NonNull Context context, SpeechService speechService) {
        super(context, R.style.bottom_dialog);
        contextWeakReference = new WeakReference<BaseActivity>((BaseActivity) context);
        speechServiceWeakReference = new WeakReference<>(speechService);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_control_panel_main);

        viewPager = findViewById(R.id.dialog_control_panel_pager);
        controlView = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_control_panel, viewPager, false);
        soundSettingView = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_panel_sound_setting, viewPager, false);
        timetickcountView = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_panel_time_setting, viewPager, false);

        List<View> viewlist = new ArrayList<>();
        viewlist.add(soundSettingView);
        viewlist.add(controlView);
        viewlist.add(timetickcountView);

        ControlPanelAdapter controlPanelAdapter = new ControlPanelAdapter(viewlist);
        viewPager.setAdapter(controlPanelAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(1, false);

        Window dialogWindow = this.getWindow();
        dialogWindow.setWindowAnimations(R.style.share_animation);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏


        onInitControlView(controlView);
        onInitSoundSettingView(soundSettingView);
        onInitTimeSettingView(timetickcountView);

        validatePanelView(null);

        if (EventBus.getDefault().isRegistered(this) == false) {
            EventBus.getDefault().register(this);
        }
    }

    private void onInitControlView(View controlView) {
        Article articlePlaying = speechServiceWeakReference.get().getSelected();
        SpeechService.SpeechServiceState currentState = speechServiceWeakReference.get().getState();
        boolean seekBarEnabled = currentState != SpeechService.SpeechServiceState.Loadding && currentState != SpeechService.SpeechServiceState.Ready && articlePlaying != null;
        float currentProgress = seekBarEnabled ? articlePlaying.getProgress() : 0f;
        tvTitle = controlView.findViewById(R.id.dialog_panel_article_title);
        seekBar = controlView.findViewById(R.id.dialog_panel_seekbar);
        buttonClose = controlView.findViewById(R.id.dialog_panel_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buttonPlay = controlView.findViewById(R.id.view_dialog_panel_play);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayButtonClick(v);
            }
        });
        icoPlay = controlView.findViewById(R.id.img_dialog_panel_play);
        progressBar = controlView.findViewById(R.id.dialog_panel_progress);
        seekBar.setOnSeekBarChangeListener(this);
        View btnSoundSetting = controlView.findViewById(R.id.btn_control_panel_sound_setting);
        btnSoundSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });

        View btnTimeSetting = controlView.findViewById(R.id.btn_control_panel_time_setting);
        btnTimeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2, true);
            }
        });
    }

    private void onInitSoundSettingView(View soundSettingView) {
        portraits = new ImageView[4];
        portraits[0] = soundSettingView.findViewById(R.id.portrait_0);
        portraits[1] = soundSettingView.findViewById(R.id.portrait_1);
        portraits[2] = soundSettingView.findViewById(R.id.portrait_2);
        portraits[3] = soundSettingView.findViewById(R.id.portrait_3);

        portraitMasks = new ImageView[4];
        portraitMasks[0] = soundSettingView.findViewById(R.id.portrait_mask_0);
        portraitMasks[1] = soundSettingView.findViewById(R.id.portrait_mask_1);
        portraitMasks[2] = soundSettingView.findViewById(R.id.portrait_mask_2);
        portraitMasks[3] = soundSettingView.findViewById(R.id.portrait_mask_3);

        portraitProgress = new ProgressBar[4];
        portraitProgress[0] = soundSettingView.findViewById(R.id.portrait_progress_0);
        portraitProgress[1] = soundSettingView.findViewById(R.id.portrait_progress_1);
        portraitProgress[2] = soundSettingView.findViewById(R.id.portrait_progress_2);
        portraitProgress[3] = soundSettingView.findViewById(R.id.portrait_progress_3);

        portraitButton = new View[4];
        portraitButton[0] = soundSettingView.findViewById(R.id.portrait_button_0);
        portraitButton[1] = soundSettingView.findViewById(R.id.portrait_button_1);
        portraitButton[2] = soundSettingView.findViewById(R.id.portrait_button_2);
        portraitButton[3] = soundSettingView.findViewById(R.id.portrait_button_3);

        for (int i = 0; i < portraitButton.length; i++) {
            portraitButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Speechor.SpeechorRole role = XiaoIce;
                    switch (v.getId()) {
                        case R.id.portrait_button_0:
                            role = XiaoIce;
                            break;
                        case R.id.portrait_button_1:
                            role = YaYa;
                            break;
                        case R.id.portrait_button_2:
                            role = XiaoYu;
                            break;
                        case R.id.portrait_button_3:
                            role = XiaoYao;
                            break;
                    }
                    speechServiceWeakReference.get().setRole(role);
                    renderPortraitFromServiceRole();
                }
            });
        }
        renderPortraitFromServiceRole();
        renderPortraitLoaddingState();
    }


    private void onInitTimeSettingView(View timeSettingView) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    protected void onPlayButtonClick(View v) {
        if (speechServiceWeakReference.get() == null) {
            if (contextWeakReference.get() != null && currentArticle != null) {
                contextWeakReference.get().postToSpeechService(currentArticle);
            }
            return;
        }
        if (isPlaying) {
            speechServiceWeakReference.get().pause();
        }
        else {
            speechServiceWeakReference.get().resume();
        }
    }

    public void renderPortraitFromServiceRole() {
        for (int i = 0; i < portraitMasks.length; i++) {
            portraitMasks[i].setVisibility(View.INVISIBLE);
        }
        if (speechServiceWeakReference.get() == null) {
            return;
        }
        switch (speechServiceWeakReference.get().getRole()) {
            case XiaoIce:
                portraitMasks[0].setVisibility(View.VISIBLE);
                break;
            case YaYa:
                portraitMasks[1].setVisibility(View.VISIBLE);
                break;
            case XiaoYu:
                portraitMasks[2].setVisibility(View.VISIBLE);
                break;

            case XiaoYao:
                portraitMasks[3].setVisibility(View.VISIBLE);
                break;
        }
    }

    public void renderPortraitLoaddingState() {
        for (int i = 0; i < portraitMasks.length; i++) {
            portraitProgress[i].setVisibility(View.INVISIBLE);
        }

        SpeechService speechService = speechServiceWeakReference.get();
        if (speechService == null || speechService.getState() != SpeechService.SpeechServiceState.Loadding) {
            return;
        }
        switch (speechService.getRole()) {
            case XiaoIce:
                portraitMasks[0].setVisibility(View.VISIBLE);
                break;
            case YaYa:
                portraitMasks[1].setVisibility(View.VISIBLE);
                break;
            case XiaoYu:
                portraitMasks[2].setVisibility(View.VISIBLE);
                break;
            case XiaoYao:
                portraitMasks[3].setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser == false) {
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
        if (speechService == null) {
            if (contextWeakReference.get() != null && currentArticle != null) {
                contextWeakReference.get().postToSpeechService(currentArticle);
            }
            return;
        }
        if (speechService.getSelected() != null &&
                speechService.getState() != SpeechService.SpeechServiceState.Loadding &&
                speechService.getState() != SpeechService.SpeechServiceState.Ready) {
            displayLoaddingAnim(true);
            speechService.seek((float) seekBar.getProgress() / 100f);
        }
    }


    protected void validatePanelView(SpeechEvent speechEvent) {
        SpeechService speechService = speechServiceWeakReference.get();
        if (speechService == null) {
            return;
        }
        Article article = speechService.getSelected();
        if (article != null) {
            tvTitle.setText(article.getTitle());
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
                if (seekBarIsSlideByUser == false && !(speechEvent instanceof SpeechBufferingEvent)) {
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
        icoPlay.setImageResource(isPlaying ? R.mipmap.ico_dialog_pause : R.mipmap.ico_dialog_play);
    }

    protected void displayLoaddingAnim(boolean display) {
        progressBar.setVisibility(display ? View.VISIBLE : View.INVISIBLE);
    }


    protected void enableSeekbar(boolean isEnabled) {
        seekBar.setEnabled(isEnabled);
        if (isEnabled == false) {
            seekBar.setProgress(0);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeechEvent(SpeechEvent event) {
        validatePanelView(event);
        if (event instanceof SpeechStartEvent) {
            currentArticle = event.getArticle();
        }
        if (event instanceof SpeechProgressEvent) {
            displayLoaddingAnim(false);
            seekBar.setEnabled(true);
        }
        else if (event instanceof SpeechBufferingEvent) {
            //如果是SpeechStartEvent 或者 BufferEvent，就显示loadding
            displayLoaddingAnim(true);
        }
        else if (event instanceof SpeechStopEvent) {
            if (((SpeechStopEvent) event).getStopReason() == SpeechStopEvent.StopReason.ListIsNull) {
                seekBar.setProgress(0);
                seekBar.setEnabled(false);
            }
        }
    }


    @Override
    public void dismiss() {
        if (EventBus.getDefault().isRegistered(this) == true) {
            EventBus.getDefault().unregister(this);
        }
        super.dismiss();
    }
}
