package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.view.View;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tendcloud.tenddata.TCAgent;
import com.tendcloud.tenddata.TDAccount;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import butterknife.OnClick;
import cn.xylink.mting.MTing;
import cn.xylink.mting.MainActivity;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.UserInfo;
import cn.xylink.mting.bean.VisitorRegisterInfo;
import cn.xylink.mting.bean.WXQQDataBean;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.contract.ThirdLoginContact;
import cn.xylink.mting.contract.VisitorRegisterContact;
import cn.xylink.mting.model.ThirdLoginRequset;
import cn.xylink.mting.model.VisitorRegisterRequest;
import cn.xylink.mting.model.data.HttpConst;
import cn.xylink.mting.openapi.QQApi;
import cn.xylink.mting.openapi.WXapi;
import cn.xylink.mting.presenter.ThirdLoginPresenter;
import cn.xylink.mting.presenter.VisitorRegisterPresenter;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.utils.NetworkUtil;
import cn.xylink.mting.utils.SharedPreHelper;

/**
 * @author wjn
 * @date 2019/10/21
 */
public class LoginActivity extends BasePresenterActivity implements ThirdLoginContact.IThirdLoginView, VisitorRegisterContact.IVisitorRegisterView {
    private Tencent mTencent;

    private ThirdLoginPresenter thirdLoginPresenter;
    private VisitorRegisterPresenter visitorRegisterPresenter;

    private String platform;

    @Override
    protected void preView() {
        MTing.getActivityManager().pushActivity(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_login);
        mTencent = QQApi.getInstance();
        L.v("mTencent", mTencent);
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Const.QQ_ID, getApplicationContext());
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected boolean enableVersionUpgrade() {
        return true;
    }

    @Override
    protected void initData() {
        thirdLoginPresenter = (ThirdLoginPresenter) createPresenter(ThirdLoginPresenter.class);
        thirdLoginPresenter.attachView(this);
        visitorRegisterPresenter = (VisitorRegisterPresenter) createPresenter(VisitorRegisterPresenter.class);
        visitorRegisterPresenter.attachView(this);
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.imv_login_weChat, R.id.imv_login_qq, R.id.tv_phone, R.id.tv_user_protocol, R.id.lv_top_view})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_protocol: {
                int netWorkStates = NetworkUtil.getNetWorkStates(context);
                if (netWorkStates == NetworkUtil.TYPE_NONE) {
                    toastShort(HttpConst.NO_NETWORK);
                    return;
                }
                Intent mIntent = new Intent(this, PlayerActivity.class);
                mIntent.putExtra(PlayerActivity.EXTRA_HTML, Const.USERPROTOCOL_URL);
                mIntent.putExtra(PlayerActivity.EXTRA_TITLE, "用户使用协议");
                startActivity(mIntent);
                break;
            }
//            case R.id.btn_left:
//                finish();
//                break;
            case R.id.imv_login_weChat: {
                int netWorkStates = NetworkUtil.getNetWorkStates(context);
                if (netWorkStates == NetworkUtil.TYPE_NONE) {
                    toastShort(HttpConst.NO_NETWORK);
                    return;
                }
                if (WXapi.isInstallWX()) {
                    TCAgent.onEvent(this, "wechat_login");
                    WXapi.loginWX();
                } else {
                    toastShort("您还未安装微信客户端！");
                }
                break;
            }
            case R.id.imv_login_qq: {
                int netWorkStates = NetworkUtil.getNetWorkStates(context);
                if (netWorkStates == NetworkUtil.TYPE_NONE) {
                    toastShort(HttpConst.NO_NETWORK);
                    return;
                }
                if (mTencent.isQQInstalled(this)) {
                    TCAgent.onEvent(this, "qq_login");
                    mTencent.login(this, "all", new BaseUiListener());
                } else {
                    toastShort("您还未安装QQ客户端！");
                }
                break;
            }
            case R.id.tv_phone:
                int netWorkStates = NetworkUtil.getNetWorkStates(context);
                if (netWorkStates == NetworkUtil.TYPE_NONE) {
                    toastShort(HttpConst.NO_NETWORK);
                    return;
                }
                startActivity(new Intent(LoginActivity.this, PhoneLoginActivity.class));
                break;
            case R.id.lv_top_view:
                requestVisitorRegister();
                break;
        }

    }

    private void requestVisitorRegister() {
        VisitorRegisterRequest registerRequest = new VisitorRegisterRequest();
        String random = UUID.randomUUID().toString().replace("-", "");
        L.v(random, random.length());
        registerRequest.random = random;
        registerRequest.doSign();
        visitorRegisterPresenter.onVisitorRegister(registerRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.v("requestCode", requestCode, "resultCode", resultCode);
        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXQQDataBean event) {
        final String eventType = event.getType();
        L.v("eventType", eventType);
        String access_token = null;
        String openId = null;
        if (eventType.equals("wechat")) {
            access_token = event.getAccess_token();
            openId = event.getOpenid();
        } else if (eventType.equals("qq")) {
            access_token = event.getAccess_token();
            openId = event.getOpenid();
            mTencent.setAccessToken(access_token, event.getExpires_in());
            mTencent.setOpenId(openId);
        }
        SharedPreHelper sharedPreHelper = SharedPreHelper.getInstance(LoginActivity.this);
        sharedPreHelper.put(SharedPreHelper.SharedAttribute.OPENID, openId);
        sharedPreHelper.put(SharedPreHelper.SharedAttribute.ACCESS_TOKEN, access_token);
        thirdLogin(access_token, openId, eventType);
    }

    public void thirdLogin(String token, String openId, String platform) {
        this.platform = platform;
        ThirdLoginRequset request = new ThirdLoginRequset();
        request.setAccess_token(token);
        request.setOpenid(openId);
        request.setPlatform(platform);
        request.doSign();
        thirdLoginPresenter.onThirdLogin(request);
    }

    @Override
    public void onThirdLoginSuccess(BaseResponse<UserInfo> loginInfoBaseResponse) {
        final int code = loginInfoBaseResponse.code;
        switch (code) {
            case -5: {
                Intent intent = new Intent(this, BindingPhoneActivity.class);
                intent.putExtra(BindingPhoneActivity.EXTRA_SOURCE, "bind_phone");
                intent.putExtra(BindingPhoneActivity.EXTRA_PLATFORM, platform);
                startActivity(intent);
                break;
            }
            case 200: {
                if (platform.equals("qq")) {
                    TCAgent.onLogin(ContentManager.getInstance().getUserInfo().getUserId(), TDAccount.AccountType.QQ, "");
                } else {
                    TCAgent.onLogin(ContentManager.getInstance().getUserInfo().getUserId(), TDAccount.AccountType.WEIXIN, "");
                }
                ContentManager.getInstance().setVisitor("1");//表示不是游客登录
                ContentManager.getInstance().setLoginToken(loginInfoBaseResponse.data.getToken());
                L.e("token" + ContentManager.getInstance().getLoginToken());
                Intent mIntent = new Intent(this, MainActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
                break;
            }
        }
    }

    @Override
    public void onThirdLoginError(int code, String errorMsg) {
        switch (code) {
            case HttpConst.STATUS_910:
                toastShort(errorMsg);
                break;
        }
    }

    @Override
    public void onVisitorRegisterSuccess(BaseResponse<VisitorRegisterInfo> response) {
        L.v(response.code);
        if (null != response.data) {
            String visitorToken = response.data.getToken();
            //把游客登录的token存进去以便其他接口调用,因为接口里只会存在一个token
            ContentManager.getInstance().setLoginToken(visitorToken);
            //把游客登录的token存进去以便同步游客数据时使用
            ContentManager.getInstance().setVisitorToken(visitorToken);
            //表示游客登录
            ContentManager.getInstance().setVisitor("0");
        }
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }

    @Override
    public void onVisitorRegisterError(int code, String errorMsg) {
        switch (code) {
            case HttpConst.STATUS_910:
                toastShort(errorMsg);
                break;
        }

    }

    class BaseUiListener implements IUiListener {

        private String accessTokenQQ;
        private String openIdQQ;
        private String expires_in;

        @Override
        public void onComplete(Object response) {
            L.v("nana", "QQ登录成功");
            try {
                L.v("response", response);
                accessTokenQQ = ((JSONObject) response).getString("access_token");
                openIdQQ = ((JSONObject) response).getString("openid");
                expires_in = ((JSONObject) response).getString("expires_in");
                L.v("nana", "accessTokenQQ: " + accessTokenQQ + "\nopenIdQQ: " + openIdQQ);

                EventBus.getDefault().post(new WXQQDataBean(accessTokenQQ, openIdQQ, "qq", expires_in));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError e) {
            L.v("nana", "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            L.v("nana", "qq cancel...");

        }

    }
}
