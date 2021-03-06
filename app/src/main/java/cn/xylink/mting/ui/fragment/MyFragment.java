package cn.xylink.mting.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;
import com.tendcloud.tenddata.TCAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.bean.QrInfo;
import cn.xylink.mting.bean.UserInfo;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.contract.QrContract;
import cn.xylink.mting.event.ArticleDetailScrollEvent;
import cn.xylink.mting.model.CheckTokenRequest;
import cn.xylink.mting.presenter.QrPresenter;
import cn.xylink.mting.ui.activity.AboutVersion;
import cn.xylink.mting.ui.activity.ArticleDetailActivity;
import cn.xylink.mting.ui.activity.BroadcastActivity;
import cn.xylink.mting.ui.activity.FeedBackActivity;
import cn.xylink.mting.ui.activity.LoginActivity;
import cn.xylink.mting.ui.activity.PersonalInfoActivity;
import cn.xylink.mting.ui.activity.PlayerActivity;
import cn.xylink.mting.ui.activity.SettingSystemActivity;
import cn.xylink.mting.ui.dialog.BottomAccountLogoutDialog;
import cn.xylink.mting.ui.dialog.BroadcastItemMenuDialog;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.utils.TingUtils;
import cn.xylink.mting.widget.MyScrollView;

import static android.app.Activity.RESULT_OK;
import static org.greenrobot.eventbus.util.ErrorDialogManager.KEY_TITLE;

public class MyFragment extends BasePresenterFragment implements QrContract.IQrView {
    @BindView(R.id.ll_setting_system)
    LinearLayout llSettingSystem;
    @BindView(R.id.imv_head)
    ImageView mHeadImageView;
    @BindView(R.id.tv_nick_name)
    TextView mNickName;
    @BindView(R.id.msv_my)
    MyScrollView mScrollView;
    private QrPresenter mQrPresenter;


    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        mQrPresenter  = (QrPresenter) createPresenter(QrPresenter.class);
        mQrPresenter.attachView(this);
        mScrollView.setOnScrollListener(new MyScrollView.OnScrollListener(){
            @Override
            public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (oldScrollY - scrollY > DensityUtil.dip2sp(getActivity(),5)) {
                    ArticleDetailScrollEvent event = new ArticleDetailScrollEvent("glide");
                    event.setActivity(getActivity());
                    EventBus.getDefault().post(event);
                }
                if (scrollY - oldScrollY > DensityUtil.dip2sp(getActivity(),5)) {
                    ArticleDetailScrollEvent event = new ArticleDetailScrollEvent("upGlide");
                    event.setActivity(getActivity());
                    EventBus.getDefault().post(event);
                }
            }
        });
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

    @OnLongClick(R.id.ll_about_ting)
    public boolean onLongClick(){
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 123);
        }else {
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            intent.putExtra("key_title","扫码登陆");
            intent.putExtra("key_continuous_scan",true);
            getActivity().startActivityForResult(intent,123,null);
        }

        return true;
    }

    @OnClick({R.id.ll_my_share, R.id.ll_click_login, R.id.ll_setting_system, R.id.tv_out_account, R.id.tv_out_application,
            R.id.ll_collect, R.id.ll_read, R.id.ll_my_create, R.id.ll_app_get_fun, R.id.ll_feedback, R.id.ll_app_star_grade, R.id.ll_about_ting, R.id.tv_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_click_login:
                if (ContentManager.getInstance().getVisitor().equals("0")) {
                    Intent intent = new Intent(new Intent(getActivity(), LoginActivity.class));
                    intent.putExtra(LoginActivity.LOGIN_ACTIVITY, "MyFragment");
                    startActivity(intent);
//                    getActivity().finish();
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
                ContentManager.getInstance().setVisitor("0");//设置成游客
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
                openBroadcast(Const.SystemBroadcast.SYSTEMBROADCAST_READED, "历史");
                break;
            case R.id.ll_my_create:
                openBroadcast(Const.SystemBroadcast.SYSTEMBROADCAST_MY_CREATE_ARTICLE, "我创建的文章");
                break;
            case R.id.ll_app_get_fun:
                mHeadImageView.postDelayed(() -> {
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    intent.putExtra(PlayerActivity.EXTRA_HTML, PlayerActivity.PROTOCOL_URL);
                    intent.putExtra(PlayerActivity.EXTRA_TITLE, getResources().getString(R.string.player_mting));
                    startActivity(intent);
                }, 200);

                break;
            case R.id.ll_feedback:
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            case R.id.ll_my_share:
                BroadcastItemMenuDialog shareDialog = new BroadcastItemMenuDialog(getActivity());
                shareDialog.setAppinfo();
                shareDialog.show();
                break;
            case R.id.ll_app_star_grade:
                TingUtils.goToMarket(getContext());
                break;
            case R.id.ll_about_ting:
                mHeadImageView.postDelayed(() -> startActivity(new Intent(getActivity(), AboutVersion.class)), 200);
                break;
            case R.id.tv_out:
                if (ContentManager.getInstance().getVisitor().equals("0")) {//表示是游客登录
                    getActivity().sendBroadcast(new Intent("action2exit"));
                } else {
                    showOutAccountDialog();
                }
                break;
        }
    }

    private void showOutAccountDialog() {
        BottomAccountLogoutDialog dialog = new BottomAccountLogoutDialog(getActivity());
        dialog.onClickListener(new BottomAccountLogoutDialog.OnBottomSelectDialogListener() {
            @Override
            public void onFirstClick() {
                getActivity().sendBroadcast(new Intent("action2exit"));
            }

            @Override
            public void onSecondClick() {
                TCAgent.onEvent(getActivity(), "account_exit");
                ContentManager.getInstance().setUserInfo(null);
                ContentManager.getInstance().setLoginToken("");
                ContentManager.getInstance().setVisitor("0");//设置成游客
                Intent intents = new Intent(getActivity(), LoginActivity.class);
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intents.putExtra(LoginActivity.LOGIN_ACTIVITY, "outAccount");
                startActivity(intents);
                getActivity().finish();
            }
        });
        dialog.show();
    }

    private void openBroadcast(String id, String name) {
        Intent intent = new Intent(getActivity(), BroadcastActivity.class);
        intent.putExtra(BroadcastActivity.EXTRA_BROADCASTID, id);
        intent.putExtra(BroadcastActivity.EXTRA_TITLE, name);
        getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("-------=========================="+requestCode+resultCode+data.getStringExtra(Intents.Scan.RESULT));
        if(resultCode == RESULT_OK && data!=null){
            switch (requestCode){
                case 123:
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    if (!TextUtils.isEmpty(result)){
                        BaseRequest request = new BaseRequest();
                        request.doSign();
                        mQrPresenter.qr(result,request);
                    }
                    break;
            }

        }
    }

    @Override
    public void onQrSuccess(QrInfo response) {
        Toast.makeText(getActivity(),"登陆成功！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQrError(int code, String errorMsg) {
        Toast.makeText(getActivity(),"请重试！",Toast.LENGTH_SHORT).show();
    }
}
