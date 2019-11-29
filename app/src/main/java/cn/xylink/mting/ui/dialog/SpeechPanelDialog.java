package cn.xylink.mting.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import cn.xylink.mting.speech.data.ArticleDataProvider;
import cn.xylink.mting.speech.event.SpeechEvent;
import cn.xylink.mting.speech.event.SpeechBufferingEvent;
import cn.xylink.mting.speech.event.SpeechFavorArticleEvent;
import cn.xylink.mting.speech.event.SpeechProgressEvent;
import cn.xylink.mting.speech.event.SpeechStartEvent;
import cn.xylink.mting.speech.event.SpeechStopEvent;
import cn.xylink.mting.ui.activity.BaseActivity;
import cn.xylink.mting.ui.activity.BroadcastItemAddActivity;
import cn.xylink.mting.ui.adapter.ControlPanelAdapter;

import static cn.xylink.mting.speech.Speechor.SpeechorRole.XiaoIce;
import static cn.xylink.mting.speech.Speechor.SpeechorRole.XiaoYao;
import static cn.xylink.mting.speech.Speechor.SpeechorRole.XiaoYu;
import static cn.xylink.mting.speech.Speechor.SpeechorRole.YaYa;

public class SpeechPanelDialog extends Dialog implements SeekBar.OnSeekBarChangeListener, ViewPager.OnPageChangeListener {
    WeakReference<BaseActivity> contextWeakReference;
    WeakReference<SpeechService> speechServiceWeakReference;

    ViewPager viewPager;

    SeekBar seekBar;
    TextView tvTitle;
    View buttonClose;
    View buttonPlay;
    ImageView icoPlay;
    ProgressBar progressBar;
    View favorButton;
    ImageView icoFavor;
    boolean isPlaying;
    boolean seekBarIsSlideByUser = false;
    Article currentArticle;


    View controlView;
    View soundSettingView;
    View timetickcountView;

    ImageView portraitMasks[];
    ImageView portraits[];
    ProgressBar portraitProgress[];
    View portraitButton[];
    RadioButton vibrates[];
    RadioButton countDowns[];

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

    private ArticleDataProvider.ArticleLoader<Article> favorCallback = new ArticleDataProvider.ArticleLoader<Article>() {
        @Override
        public void invoke(int errorCode, Article data) {
            if(errorCode == 0) {
                SpeechFavorArticleEvent event = new SpeechFavorArticleEvent(data);
                EventBus.getDefault().post(event);
            }
        }
    };

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

        icoFavor = controlView.findViewById(R.id.ico_favor);
        favorButton = controlView.findViewById(R.id.view_dialog_panel_favor);
        favorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contextWeakReference.get() == null
                        || speechServiceWeakReference.get() == null
                        || speechServiceWeakReference.get().getSelected() == null
                        || speechServiceWeakReference.get().getState() == SpeechService.SpeechServiceState.Loadding) {
                    return;
                }
                Article article = speechServiceWeakReference.get().getSelected();
                ArticleDataProvider provider = new ArticleDataProvider(contextWeakReference.get());

                if(article.getStore() == 0) {
                    icoFavor.setImageResource(R.mipmap.ico_dialog_favor);
                    provider.favorArticle(article, favorCallback);
                }
                else {
                    icoFavor.setImageResource(R.mipmap.ico_dialog_unfavor);
                    provider.unfavorArticle(article, favorCallback);
                }
            }
        });
        controlView.findViewById(R.id.view_dialog_panel_add_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contextWeakReference.get() == null
                        || speechServiceWeakReference.get() == null
                        || speechServiceWeakReference.get().getSelected() == null) {
                    return;
                }
                Intent intent = new Intent(contextWeakReference.get(), BroadcastItemAddActivity.class);
                intent.putExtra(BroadcastItemAddActivity.ARTICLE_IDS_EXTRA, speechServiceWeakReference.get().getSelected().getArticleId());
                contextWeakReference.get().startActivity(intent);
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

        vibrates = new RadioButton[4];
        vibrates[0] = soundSettingView.findViewById(R.id.rb_sound_setting_vrbrate1);
        vibrates[1] = soundSettingView.findViewById(R.id.rb_sound_setting_vrbrate2);
        vibrates[2] = soundSettingView.findViewById(R.id.rb_sound_setting_vrbrate3);
        vibrates[3] = soundSettingView.findViewById(R.id.rb_sound_setting_vrbrate4);

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
                    //更新界面
                    renderRolePortraitFromService();
                }
            });
        }

        soundSettingView.findViewById(R.id.btn_sound_setting_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        RadioGroup radioGroup = soundSettingView.findViewById(R.id.rg_sound_setting_vibrate);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SpeechService speechService = speechServiceWeakReference.get();
                if (speechService == null) {
                    return;
                }
                switch (checkedId) {
                    case R.id.rb_sound_setting_vrbrate1:
                        if (speechService.getSpeed() != Speechor.SpeechorSpeed.SPEECH_SPEED_HALF) {
                            speechService.setSpeed(Speechor.SpeechorSpeed.SPEECH_SPEED_HALF);
                        }
                        break;
                    case R.id.rb_sound_setting_vrbrate2:
                        if (speechService.getSpeed() != Speechor.SpeechorSpeed.SPEECH_SPEED_NORMAL) {
                            speechService.setSpeed(Speechor.SpeechorSpeed.SPEECH_SPEED_NORMAL);
                        }
                        break;
                    case R.id.rb_sound_setting_vrbrate3:
                        if (speechService.getSpeed() != Speechor.SpeechorSpeed.SPEECH_SPEED_MULTIPLE_1_POINT_5) {
                            speechService.setSpeed(Speechor.SpeechorSpeed.SPEECH_SPEED_MULTIPLE_1_POINT_5);
                        }
                        break;
                    case R.id.rb_sound_setting_vrbrate4:
                        if (speechService.getSpeed() != Speechor.SpeechorSpeed.SPEECH_SPEED_MULTIPLE_2) {
                            speechService.setSpeed(Speechor.SpeechorSpeed.SPEECH_SPEED_MULTIPLE_2);
                        }
                        break;
                }
            }
        });

        renderRolePortraitFromService();
        renderSoundSpeechFromService();
        renderPortraitLoaddingState(true);
    }


    private void onInitTimeSettingView(View timeSettingView) {
        SpeechService speechService = speechServiceWeakReference.get();
        if(speechService == null) {
            return;
        }
        countDowns = new RadioButton[5];
        countDowns[0] = timeSettingView.findViewById(R.id.rb_count_down_0);
        countDowns[1] = timeSettingView.findViewById(R.id.rb_count_down_1);
        countDowns[2] = timeSettingView.findViewById(R.id.rb_count_down_2);
        countDowns[3] = timeSettingView.findViewById(R.id.rb_count_down_3);
        countDowns[4] = timeSettingView.findViewById(R.id.rb_count_down_4);
        timeSettingView.findViewById(R.id.btn_time_setting_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });

        //set countdown options's onchecked event.
        ((RadioGroup)timeSettingView.findViewById(R.id.rg_time_setting_options)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SpeechService speechService = speechServiceWeakReference.get();
                if(speechService == null) {
                    return;
                }
                switch (checkedId) {
                    case R.id.rb_count_down_0:
                        speechService.cancelCountDown();
                        break;
                    case R.id.rb_count_down_1:
                        speechService.setCountDown(SpeechService.CountDownMode.NumberCount, 1);
                        break;
                    case R.id.rb_count_down_2:
                        speechService.setCountDown(SpeechService.CountDownMode.MinuteCount, 10);
                        break;
                    case R.id.rb_count_down_3:
                        speechService.setCountDown(SpeechService.CountDownMode.MinuteCount, 20);
                        break;
                    case R.id.rb_count_down_4:
                        speechService.setCountDown(SpeechService.CountDownMode.MinuteCount, 30);
                        break;
                }
            }
        });

        renderCountDownOptionsFromService();
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

    public void renderRolePortraitFromService() {
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

    protected void resetAllPortaitLoaddingState() {
        for (int i = 0; i < portraitMasks.length; i++) {
            portraitProgress[i].setVisibility(View.INVISIBLE);
        }
    }

    /*

     */
    protected void renderPortraitLoaddingState(boolean isInit) {
        resetAllPortaitLoaddingState();
        SpeechService speechService = speechServiceWeakReference.get();
        if (speechService == null || (isInit && speechService.getState() != SpeechService.SpeechServiceState.Loadding)) {
            return;
        }
        switch (speechService.getRole()) {
            case XiaoIce:
                portraitProgress[0].setVisibility(View.VISIBLE);
                break;
            case YaYa:
                portraitProgress[1].setVisibility(View.VISIBLE);
                break;
            case XiaoYu:
                portraitProgress[2].setVisibility(View.VISIBLE);
                break;
            case XiaoYao:
                portraitProgress[3].setVisibility(View.VISIBLE);
                break;
        }
    }

    protected void renderSoundSpeechFromService() {
        SpeechService speechService = speechServiceWeakReference.get();
        if (speechService == null) {
            return;
        }
        RadioGroup radioGroup = soundSettingView.findViewById(R.id.rg_sound_setting_vibrate);
        switch (speechService.getSpeed()) {
            case SPEECH_SPEED_HALF:
                vibrates[0].setChecked(true);
                break;
            case SPEECH_SPEED_NORMAL:
                vibrates[1].setChecked(true);
                break;
            case SPEECH_SPEED_MULTIPLE_1_POINT_5:
                vibrates[2].setChecked(true);
                break;
            case SPEECH_SPEED_MULTIPLE_2:
                vibrates[3].setChecked(true);
                break;
        }
    }


    protected void renderCountDownOptionsFromService() {
        SpeechService speechService = speechServiceWeakReference.get();
        if(speechService == null) {
            return;
        }
        switch (speechService.getCountDownMode()) {
            case None:
                countDowns[0].setChecked(true);
                break;
            case NumberCount:
                countDowns[1].setChecked(true);
                break;
            case MinuteCount:
                switch (speechService.getCountdownThresholdValue()) {
                    case 10:
                        countDowns[2].setChecked(true);
                        break;
                    case 20:
                        countDowns[3].setChecked(true);
                        break;
                    case 30:
                        countDowns[4].setChecked(true);
                        break;
                }
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
            if(speechService.getState() != SpeechService.SpeechServiceState.Loadding) {
                icoFavor.setImageResource(article.getStore() == 1 ? R.mipmap.ico_dialog_favor : R.mipmap.ico_dialog_unfavor);
            }
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
                displayLoaddingAnim(speechService.isBuffering());
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
            resetAllPortaitLoaddingState();
            seekBar.setEnabled(true);
        }
        else if (event instanceof SpeechBufferingEvent) {
            //如果是SpeechStartEvent 或者 BufferEvent，就显示loadding
            renderPortraitLoaddingState(false);
            displayLoaddingAnim(true);
        }
        else if (event instanceof SpeechStopEvent) {
            if (((SpeechStopEvent) event).getStopReason() == SpeechStopEvent.StopReason.ListIsNull) {
                seekBar.setProgress(0);
                seekBar.setEnabled(false);
            }
            else if(((SpeechStopEvent) event).getStopReason() == SpeechStopEvent.StopReason.CountDownToZero) {
                renderCountDownOptionsFromService();
            }
            this.dismiss();
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
