package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.SpeechSettingService;
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
    @BindView(R.id.tv_count_down_10)
    TextView tvCountDown10;
    @BindView(R.id.tv_count_down_20)
    TextView tvCountDown20;
    @BindView(R.id.tv_count_down_30)
    TextView tvCountDown30;

    private SpeechService.CountDownMode countDownMode;
    private SpeechSettingService service;
    //设置定时关闭回调
    String optName = "articleDetails_timing_close";

    @Override
    protected void preView() {
        setContentView(R.layout.activity_setting_timer
        );
    }

    @Override
    protected void initView() {
        tvCountDown10.setVisibility(View.INVISIBLE);
        tvCountDown20.setVisibility(View.INVISIBLE);
        tvCountDown30.setVisibility(View.INVISIBLE);
        if (ContentManager.getInstance().getVisitor().equals("0")) {//表示是游客登录
            Intent intent = new Intent(new Intent(SettingTimerActivity.this, LoginActivity.class));
            intent.putExtra(LoginActivity.LOGIN_ACTIVITY, Const.visitor);
            startActivity(intent);
            finish();
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
//            tvTimer.setText(countDownValue + "分钟后关闭");
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
                    case R.id.rb_close:
                        service.cancelCountDown();
                        break;
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
                        tvCountDown10.setVisibility(View.VISIBLE);
                        tvCountDown20.setVisibility(View.INVISIBLE);
                        tvCountDown30.setVisibility(View.INVISIBLE);
                        tvCountDown10.setText(getCountDown());
                        optName = "articleDetails_timing_10";
                        handler.postDelayed(update_thread,1000);
                        break;
                    case R.id.rb_time20:
                        ContentManager.getInstance().setRgTime(3);
                        swCount.setChecked(true);
                        service.setCountDown(SpeechService.CountDownMode.MinuteCount, 20);
                        tvCountDown10.setVisibility(View.INVISIBLE);
                        tvCountDown20.setVisibility(View.VISIBLE);
                        tvCountDown30.setVisibility(View.INVISIBLE);
                        tvCountDown20.setText(getCountDown());
                        optName = "articleDetails_timing_20";
                        handler.postDelayed(update_thread,1000);
                        break;
                    case R.id.rb_time30:
                        ContentManager.getInstance().setRgTime(4);
                        swCount.setChecked(true);
                        service.setCountDown(SpeechService.CountDownMode.MinuteCount, 30);
                        tvCountDown10.setVisibility(View.INVISIBLE);
                        tvCountDown20.setVisibility(View.INVISIBLE);
                        tvCountDown30.setVisibility(View.VISIBLE);
                        tvCountDown30.setText(getCountDown());
                        optName = "articleDetails_timing_30";
                        handler.postDelayed(update_thread,1000);
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

    @OnClick({R.id.btn_left})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
        }
    }


    @Override
    protected void onSpeechServiceAvailable() {
        super.onSpeechServiceAvailable();
        service = getSpeechService();
        switch (service.getCountDownMode()) {
            case None:
                rgCountDown.check(R.id.rb_close);
                break;
            case NumberCount:
                rgCountDown.check(R.id.rb_current);
                break;
            case MinuteCount:
                switch (service.getCountDownThresholdValue()) {
                    case 10:
                        rgCountDown.check(R.id.rb_time10);
                        tvCountDown10.setVisibility(View.VISIBLE);
                        tvCountDown20.setVisibility(View.INVISIBLE);
                        tvCountDown30.setVisibility(View.INVISIBLE);
                        tvCountDown10.setText(getCountDown());
                        break;
                    case 20:
                        rgCountDown.check(R.id.rb_time20);
                        tvCountDown10.setVisibility(View.INVISIBLE);
                        tvCountDown20.setVisibility(View.VISIBLE);
                        tvCountDown30.setVisibility(View.INVISIBLE);
                        break;
                    case 30:
                        rgCountDown.check(R.id.rb_time30);
                        tvCountDown10.setVisibility(View.INVISIBLE);
                        tvCountDown20.setVisibility(View.INVISIBLE);
                        tvCountDown30.setVisibility(View.VISIBLE);

                        break;
                }
                break;
        }
    }

    @Override
    protected boolean enableSpeechService() {
        return true;
    }

    public String getCountDown() {

        return service.getCountDownValueOfTimeFormat();

    }

    Handler handler = new Handler();
    Runnable update_thread = new Runnable() {
        @Override
        public void run() {
            switch (service.getCountDownMode()) {
                case None:
                    rgCountDown.check(R.id.rb_close);
                    //发送消息，结束倒计时
                    Message message = new Message();
                    message.what = 1;
                    handlerStop.sendMessage(message);
                    break;
                case NumberCount:
                    rgCountDown.check(R.id.rb_current);
                    Message message2 = new Message();
                    message2.what = 1;
                    handlerStop.sendMessage(message2);
                    break;
                case MinuteCount:
                    switch (service.getCountDownThresholdValue()) {
                        case 10:
                            rgCountDown.check(R.id.rb_time10);
                            tvCountDown10.setVisibility(View.VISIBLE);
                            tvCountDown20.setVisibility(View.INVISIBLE);
                            tvCountDown30.setVisibility(View.INVISIBLE);
                            tvCountDown10.setText(getCountDown());
                            handler.postDelayed(this, 1000);
                            break;
                        case 20:
                            rgCountDown.check(R.id.rb_time20);
                            tvCountDown10.setVisibility(View.INVISIBLE);
                            tvCountDown20.setVisibility(View.VISIBLE);
                            tvCountDown30.setVisibility(View.INVISIBLE);
                            handler.postDelayed(this, 1000);
                            break;
                        case 30:
                            rgCountDown.check(R.id.rb_time30);
                            tvCountDown10.setVisibility(View.INVISIBLE);
                            tvCountDown20.setVisibility(View.INVISIBLE);
                            tvCountDown30.setVisibility(View.VISIBLE);
                            handler.postDelayed(this, 1000);
                            break;

                    }
                    break;
            }
        }
    };

    final Handler handlerStop = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    handler.removeCallbacks(update_thread);
                    break;
            }
            super.handleMessage(msg);
        }

    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        handler.removeCallbacks(update_thread);
//    }
}
