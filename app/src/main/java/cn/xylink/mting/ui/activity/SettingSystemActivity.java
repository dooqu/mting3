package cn.xylink.mting.ui.activity;

import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;

/**
 * @author wjn
 * @date 2019/10/30
 */
public class SettingSystemActivity extends BasePresenterActivity {

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

    }

    @OnClick({R.id.ll_voice, R.id.ll_time, R.id.ll_font, R.id.ll_add})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_voice:

                break;
            case R.id.ll_time:

                break;
            case R.id.ll_font:

                break;
            case R.id.ll_add:

                break;
            default:

                break;
        }
    }

}
