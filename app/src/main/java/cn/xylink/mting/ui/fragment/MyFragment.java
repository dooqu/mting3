package cn.xylink.mting.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.ui.activity.SettingSystemActivity;

public class MyFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_setting_system)
    LinearLayout llSettingSystem;


    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        llSettingSystem.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_system:
                startActivity(new Intent(getActivity(), SettingSystemActivity.class));
                break;
        }
    }
}
