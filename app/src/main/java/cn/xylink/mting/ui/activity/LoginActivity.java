package cn.xylink.mting.ui.activity;

import cn.xylink.mting.MTing;
import cn.xylink.mting.R;

/**
 * @author wjn
 * @date 2019/10/21
 */
public class LoginActivity extends BasePresenterActivity {
    @Override
    protected void preView() {
        MTing.getActivityManager().pushActivity(this);
        setContentView(R.layout.activity_login);
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
}
