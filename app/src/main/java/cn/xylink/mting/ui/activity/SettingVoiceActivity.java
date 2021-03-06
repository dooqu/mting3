package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.speech.SpeechSettingService;
import cn.xylink.mting.speech.Speechor;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.SharedPreHelper;

/**
 * @author wjn
 * @date 2019/10/31
 */
public class SettingVoiceActivity extends BasePresenterActivity {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.pb_main_play_progress1)
    ProgressBar mpb1;
    @BindView(R.id.pb_main_play_progress2)
    ProgressBar mpb2;
    @BindView(R.id.pb_main_play_progress3)
    ProgressBar mpb3;
    @BindView(R.id.pb_main_play_progress4)
    ProgressBar mpb4;
    @BindView(R.id.iv_check1)
    ImageView mCheck1;
    @BindView(R.id.iv_check2)
    ImageView mCheck2;
    @BindView(R.id.iv_check3)
    ImageView mCheck3;
    @BindView(R.id.iv_check4)
    ImageView mCheck4;
    @BindView(R.id.rg_speed)
    RadioGroup rgSpeed;
    private Speechor.SpeechorRole role;
    private Speechor.SpeechorSpeed speed;
    private SpeechSettingService service;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_setting_voice);
    }

    @Override
    protected void initView() {
        if (ContentManager.getInstance().getVisitor().equals("0")) {//表示是游客登录
            Intent intent = new Intent(new Intent(SettingVoiceActivity.this, LoginActivity.class));
            intent.putExtra(LoginActivity.LOGIN_ACTIVITY, Const.visitor);
            startActivity(intent);
            finish();
        }

        rgSpeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_half:
                        service.setSpeed(Speechor.SpeechorSpeed.SPEECH_SPEED_HALF);
                        break;
                    case R.id.rb_normal:
                        service.setSpeed(Speechor.SpeechorSpeed.SPEECH_SPEED_NORMAL);
                        break;
                    case R.id.rb_1_5:
                        service.setSpeed(Speechor.SpeechorSpeed.SPEECH_SPEED_MULTIPLE_1_POINT_5);
                        break;
                    case R.id.rb_2:
                        service.setSpeed(Speechor.SpeechorSpeed.SPEECH_SPEED_MULTIPLE_2);
                        break;
                }
                SharedPreHelper.getInstance(getApplicationContext()).put("SPEECH_SPEED", String.valueOf(service.getSpeed()));
            }
        });

    }

    @Override
    protected void onSpeechServiceAvailable() {
        super.onSpeechServiceAvailable();
        service = getSpeechService();
        if (null != service) {
            role = service.getRole();
            speed = service.getSpeed();
            setCheckRole(role);
            setCheckSpeed(speed);
        } else {
            toastCenterShort("连接不到播放服务");
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("声音设置");
    }

    public void closeAll() {
        mCheck1.setVisibility(View.INVISIBLE);
        mCheck2.setVisibility(View.INVISIBLE);
        mCheck3.setVisibility(View.INVISIBLE);
        mCheck4.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.btn_left, R.id.bt_type1, R.id.bt_type2, R.id.bt_type3, R.id.bt_type4})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.bt_type1:
                service.setRole(Speechor.SpeechorRole.XiaoIce);
                closeAll();
                mCheck1.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_type2:
                service.setRole(Speechor.SpeechorRole.YaYa);
                closeAll();
                mCheck2.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_type3:
                service.setRole(Speechor.SpeechorRole.XiaoYu);
                closeAll();
                mCheck3.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_type4:
                service.setRole(Speechor.SpeechorRole.XiaoYao);
                closeAll();
                mCheck4.setVisibility(View.VISIBLE);
                break;
        }
        SharedPreHelper.getInstance(getApplicationContext()).put("SPEECH_ROLE", String.valueOf(service.getRole()));
    }

    public void setLoading(boolean tf, Speechor.SpeechorRole role) {
        mpb1.setVisibility(View.INVISIBLE);
        mpb2.setVisibility(View.INVISIBLE);
        mpb3.setVisibility(View.INVISIBLE);
        mpb4.setVisibility(View.INVISIBLE);
        if (tf) {
            switch (role) {
                case XiaoIce:
                    mpb1.setVisibility(View.VISIBLE);
                    break;
                case YaYa:
                    mpb2.setVisibility(View.VISIBLE);
                    break;
                case XiaoYu:
                    mpb3.setVisibility(View.VISIBLE);
                    break;
                case XiaoYao:
                    mpb4.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public void setCheckRole(Speechor.SpeechorRole role) {
        closeAll();
        if (null != role) {
            switch (role) {
                case XiaoIce:
                    mCheck1.setVisibility(View.VISIBLE);
                    break;
                case YaYa:
                    mCheck2.setVisibility(View.VISIBLE);
                    break;
                case XiaoYao:
                    mCheck3.setVisibility(View.VISIBLE);
                    break;
                case XiaoYu:
                    mCheck4.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void setCheckSpeed(Speechor.SpeechorSpeed speed) {
        switch (speed) {
            case SPEECH_SPEED_HALF:
                rgSpeed.check(R.id.rb_half);
                break;
            case SPEECH_SPEED_NORMAL:
                rgSpeed.check(R.id.rb_normal);
                break;
            case SPEECH_SPEED_MULTIPLE_1_POINT_5:
                rgSpeed.check(R.id.rb_1_5);
                break;
            case SPEECH_SPEED_MULTIPLE_2:
                rgSpeed.check(R.id.rb_2);
                break;
        }
    }

    @Override
    protected boolean enableSpeechService() {
        return true;
    }
}
