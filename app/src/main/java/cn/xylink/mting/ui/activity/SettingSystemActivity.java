package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;

/**
 * @author wjn
 * @date 2019/10/30
 */
public class SettingSystemActivity extends BasePresenterActivity {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_setting_sysytem);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("系统设置");
    }

    @OnClick({R.id.btn_left, R.id.ll_voice, R.id.ll_time, R.id.ll_font, R.id.ll_add})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ll_voice:
                startActivity(new Intent(this, SettingVoiceActivity.class));
                break;
            case R.id.ll_time:
                startActivity(new Intent(this, SettingTimerActivity.class));
                break;
            case R.id.ll_font:
                startActivity(new Intent(this, SettingFontActivity.class));
                break;
            case R.id.ll_add:

                break;
            default:

                break;
        }
    }

}
