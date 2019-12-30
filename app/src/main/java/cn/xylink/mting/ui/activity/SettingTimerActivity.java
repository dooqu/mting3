package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tendcloud.tenddata.TCAgent;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.SpeechServiceProxy;
import cn.xylink.mting.utils.ContentManager;

/**
 * @author wjn
 * @date 2019/11/4
 */
public class SettingTimerActivity extends BasePresenterActivity {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.rg_count_down)
    RadioGroup rgCountDown;
    @BindView(R.id.sw_count)
    Switch swCount;
    @BindView(R.id.tv_timer)
    TextView tvTimer;

    private SpeechService.CountDownMode countDownMode;
    private SpeechService service;
    SpeechServiceProxy proxy;
    private int countDownValue;
    //设置定时关闭回调
    String optName = "articleDetails_timing_close";

    @Override
    protected void preView() {
        setContentView(R.layout.activity_setting_timer
        );
    }

    @Override
    protected void initView() {
        if (ContentManager.getInstance().getVisitor().equals("0")) {//表示是游客登录
            Intent intent = new Intent(new Intent(SettingTimerActivity.this, LoginActivity.class));
            intent.putExtra(LoginActivity.LOGIN_ACTIVITY, Const.visitor);
            startActivity(intent);
            finish();
        }
        proxy = new SpeechServiceProxy(this) {
            @Override
            protected void onConnected(boolean connected, SpeechService service) {
                if (connected) {
                    SettingTimerActivity.this.service = service;
                }
            }
        };
        proxy.bind();
        if (proxy.bind() == false) {
            Toast.makeText(this, "未能连接到播放服务", Toast.LENGTH_SHORT).show();
        }
        if (countDownMode == null || countDownMode == SpeechService.CountDownMode.None) {
            swCount.setChecked(false);
            rgCountDown.check(-1);
            tvTimer.setVisibility(View.INVISIBLE);
        } else if (countDownMode == SpeechService.CountDownMode.NumberCount) {
            swCount.setChecked(true);
            rgCountDown.check(R.id.rb_current);
            tvTimer.setText("读完本篇后关闭");
            tvTimer.setVisibility(View.VISIBLE);
        } else {
            int rgTime = ContentManager.getInstance().getRgTime();
            swCount.setChecked(true);
            tvTimer.setText(countDownValue + "分钟后关闭");
            tvTimer.setVisibility(View.VISIBLE);
            switch (rgTime) {
                case 2:
                    rgCountDown.check(R.id.rb_time10);
                    break;
                case 3:
                    rgCountDown.check(R.id.rb_time20);
                    break;
                case 4:
                    rgCountDown.check(R.id.rb_time30);
                    break;
            }
        }
        rgCountDown.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_current:
                        ContentManager.getInstance().setRgTime(1);
                        swCount.setChecked(true);
                        service.setCountDown(SpeechService.CountDownMode.NumberCount, 1);
                        optName = "articleDetails_timing_article";
                        break;
                    case R.id.rb_time10:
                        ContentManager.getInstance().setRgTime(2);
                        swCount.setChecked(true);
                        service.setCountDown(SpeechService.CountDownMode.MinuteCount, 10);
                        optName = "articleDetails_timing_10";
                        break;
                    case R.id.rb_time20:
                        ContentManager.getInstance().setRgTime(3);
                        swCount.setChecked(true);
                        service.setCountDown(SpeechService.CountDownMode.MinuteCount, 20);
                        optName = "articleDetails_timing_20";
                        break;
                    case R.id.rb_time30:
                        ContentManager.getInstance().setRgTime(4);
                        swCount.setChecked(true);
                        service.setCountDown(SpeechService.CountDownMode.MinuteCount, 30);
                        optName = "articleDetails_timing_30";
                        break;
                }
                TCAgent.onEvent(SettingTimerActivity.this, optName);
            }
        });
        swCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    ContentManager.getInstance().setRgTime(0);
                    rgCountDown.check(-1);
                    service.cancelCountDown();
                }
                buttonView.setChecked(isChecked);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("定时关闭");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proxy.unbind();
    }
}
