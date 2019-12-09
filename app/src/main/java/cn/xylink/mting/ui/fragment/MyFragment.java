package cn.xylink.mting.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.UserInfo;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.ui.activity.BroadcastActivity;
import cn.xylink.mting.ui.activity.LoginActivity;
import cn.xylink.mting.ui.activity.PersonalInfoActivity;
import cn.xylink.mting.ui.activity.SettingSystemActivity;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.L;

public class MyFragment extends BaseFragment {
    @BindView(R.id.ll_setting_system)
    LinearLayout llSettingSystem;
    @BindView(R.id.imv_head)
    ImageView mHeadImageView;
    @BindView(R.id.tv_nick_name)
    TextView mNickName;


    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initData() {
        setUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    private void setUserInfo() {
        UserInfo info = ContentManager.getInstance().getUserInfo();
        L.e(info);
        if (ContentManager.getInstance().getVisitor().equals("0")) {
            L.v("游客登录");
        } else {
            if (null != info) {
                if (!TextUtils.isEmpty(info.getHeadImg()))
                    ImageUtils.get().loadCircle(mHeadImageView, info.getHeadImg());
                if (!TextUtils.isEmpty(info.getNickName()))
                    mNickName.setText(info.getNickName());
            }
        }
    }

    @OnClick({R.id.ll_click_login, R.id.ll_setting_system, R.id.tv_out_account, R.id.tv_out_application, R.id.ll_collect, R.id.ll_read, R.id.ll_my_create})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_click_login:
                if (ContentManager.getInstance().getVisitor().equals("0")) {
                    Intent intent = new Intent(new Intent(getActivity(), LoginActivity.class));
                    intent.putExtra(LoginActivity.LOGIN_ACTIVITY, "MyFragment");
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
                }
                break;
            case R.id.ll_setting_system:
                startActivity(new Intent(getActivity(), SettingSystemActivity.class));
                break;
            case R.id.tv_out_account:
                TCAgent.onEvent(getActivity(), "account_exit");
                ContentManager.getInstance().setUserInfo(null);
                ContentManager.getInstance().setLoginToken("");
                ContentManager.getInstance().setVisitorToken("0");//设置成游客
                Intent intents = new Intent(getActivity(), LoginActivity.class);
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intents.putExtra(LoginActivity.LOGIN_ACTIVITY, "outAccount");
                startActivity(intents);
                getActivity().finish();
                break;
            case R.id.tv_out_application:
                getActivity().sendBroadcast(new Intent("action2exit"));
                break;
            case R.id.ll_collect:
                openBroadcast(Const.SystemBroadcast.SYSTEMBROADCAST_STORE, "收藏");
                break;
            case R.id.ll_read:
                openBroadcast(Const.SystemBroadcast.SYSTEMBROADCAST_READED, "已读");
                break;
            case R.id.ll_my_create:
                openBroadcast(Const.SystemBroadcast.SYSTEMBROADCAST_MY_CREATE_ARTICLE, "我创建的文章");
                break;
        }
    }

    private void openBroadcast(String id, String name) {
        Intent intent = new Intent(getActivity(), BroadcastActivity.class);
        intent.putExtra(BroadcastActivity.EXTRA_BROADCASTID, id);
        intent.putExtra(BroadcastActivity.EXTRA_TITLE, name);
        getActivity().startActivity(intent);
    }
}
