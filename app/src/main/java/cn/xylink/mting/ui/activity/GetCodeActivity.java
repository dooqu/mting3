package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;
import com.tendcloud.tenddata.TDAccount;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.MTing;
import cn.xylink.mting.MainActivity;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.CodeInfo;
import cn.xylink.mting.contract.GetCodeContact;
import cn.xylink.mting.contract.SmsLoginContact;
import cn.xylink.mting.model.GetCodeRequest;
import cn.xylink.mting.model.SmsLoginRequset;
import cn.xylink.mting.model.data.Const;
import cn.xylink.mting.model.data.HttpConst;
import cn.xylink.mting.model.data.SmsLoginResponse;
import cn.xylink.mting.presenter.GetCodePresenter;
import cn.xylink.mting.presenter.SmsLoginPresenter;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.utils.SafeUtils;
import cn.xylink.mting.utils.SharedPreHelper;
import cn.xylink.mting.widget.PhoneCode;
import cn.xylink.mting.widget.ZpPhoneEditText;

public class GetCodeActivity extends BasePresenterActivity implements GetCodeContact.IGetCodeView, SmsLoginContact.ISmsLoginView {

    private GetCodePresenter codePresenter;
    private SmsLoginPresenter smsLoginPresenter;
    private CodeInfo codeInfo;
    public static final String EXTRA_TICKET = "extra_ticket";
    public static final String EXTRA_PHONE = "extra_phone";
    public static final String EXTRA_SOURCE = "extra_source";
    public static final String EXTRA_platform = "extra_platform";
    public static final String EXTRA_CODE = "extra_code";

    private static final int ONE_MINUTE = 60 * 1000;

    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    @BindView(R.id.et_phone)
    ZpPhoneEditText etPhone;
    @BindView(R.id.pc_code)
    PhoneCode pCcode;
    @BindView(R.id.tv_include_title)
    TextView tvTitle;


    private boolean isFinished;
    private String phone;
    private String codeID;
    private String ticket;
    private String source;
    private String platform;

    CountDownTimer timer;

    private int codeLength;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_get_code);
        MTing.getActivityManager().pushActivity(this);
    }


    public void resetDownTimer(long minute) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(minute, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isFinished = false;
                tvCountDown.setTextColor(getResources().getColorStateList(R.color.color_login_text_gray));
                tvCountDown.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                isFinished = true;

                tvCountDown.setTextColor(getResources().getColorStateList(R.color.color_blue));
                tvCountDown.setText("重新获取");
            }
        }.start();
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
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }

    @Override
    protected void initView() {

        L.v("source", source);
        resetDownTimer(ONE_MINUTE);
        pCcode.setOnCompleteListener(new PhoneCode.Listener() {
            @Override
            public void onComplete(String content) {
                L.v("content", content);
                codeLength = content.length();
                if (TextUtils.isEmpty(phone))
                    return;
                smsLogin(content);
            }
        });

        etPhone.setText(phone);
        tvTitle.setText("手机号验证");
    }


    private void smsLogin(String smsContent) {
        L.v(smsContent.length());
        SmsLoginRequset requset = new SmsLoginRequset();
        try {
            requset.code = SafeUtils.getRsaString(smsContent, Const.publicKey);
            L.v("code", requset.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(ContentManager.getInstance().getLoginToken())) {
            requset.token = ContentManager.getInstance().getLoginToken();
        }
        requset.phone = phone.replaceAll(" ", "");
        requset.codeId = codeID;
        requset.doSign();
        smsLoginPresenter.onSmsLogin(requset);
        requset = null;
    }

    @Override
    protected void initData() {

        phone = getIntent().getStringExtra(PhoneLoginActivity.EXTRA_PHONE);
        codeID = getIntent().getStringExtra(PhoneLoginActivity.EXTRA_CODE);
        source = getIntent().getStringExtra(EXTRA_SOURCE);
        platform = getIntent().getStringExtra(BindingPhoneActivity.EXTRA_PLATFORM);


        L.v("phone", phone, "ticket", ticket, "codeID", codeID);
        codePresenter = (GetCodePresenter) createPresenter(GetCodePresenter.class);
        codePresenter.attachView(this);


        smsLoginPresenter = (SmsLoginPresenter) createPresenter(SmsLoginPresenter.class);
        smsLoginPresenter.attachView(this);
//        if (!"register".equals(source))
//            requsetCode();
    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    public void onCodeSuccess(BaseResponse<CodeInfo> response) {
        L.v(response.code);
        toastShort(response.message);
        if (response.data != null) {
            codeID = response.data.getCodeId();
        }
        resetDownTimer(ONE_MINUTE);
    }

    private static final String PAUSE_TIME = "code_pause_time";

    @Override
    protected void onPause() {
        super.onPause();
        long pauseTime = SystemClock.elapsedRealtime();
        SharedPreHelper.getInstance(this).put(PAUSE_TIME, pauseTime);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onCodeError(int code, String errorMsg) {
        switch (code) {
            case HttpConst.STATUS_910:
                toastShort(errorMsg);
                break;
        }
        if (code == -910) {
//            long resumeTime = SystemClock.elapsedRealtime();
//            long pauseTime = (long) SharedPreHelper.getInstance(this).getSharedPreference(PAUSE_TIME, 0l);
//            L.v("resumeTime", resumeTime, "pauseTime", pauseTime);
//            long endTime = resumeTime - pauseTime;
//            L.v("endTime", endTime, "ONE_MINUTE", ONE_MINUTE);
//            if (endTime < ONE_MINUTE) {
////                resetDownTimer(endTime);
//            }
        }
    }


    public void requsetCode() {
        if (TextUtils.isEmpty(phone))
            return;
        GetCodeRequest requset = new GetCodeRequest();
        requset.phone = phone.replaceAll(" ", "");
//        requset.source = source;
        requset.source = "";
        requset.doSign();
        codePresenter.onGetCode(requset);
    }

    @OnClick({R.id.tv_count_down, R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_count_down:
                if (isFinished) {
                    pCcode.clearText();
                    requsetCode();

                }
                break;
            case R.id.btn_left:
                finish();
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer.onFinish();
        }
        if (pCcode != null) {
            pCcode.clearText();
        }

    }

    @Override
    public void onSmsLoginSuccess(SmsLoginResponse response) {
        if (response.getData() != null) {
            L.v("token", response.getData().getToken());
            ContentManager.getInstance().setLoginToken(response.getData().getToken());
            ContentManager.getInstance().setUserInfo(response.getData());
            TCAgent.onRegister(ContentManager.getInstance().getUserInfo().getUserId(), TDAccount.AccountType.ANONYMOUS, "");
            TCAgent.onLogin(ContentManager.getInstance().getUserInfo().getUserId(), TDAccount.AccountType.ANONYMOUS, "");
            Intent mIntent = new Intent(this, MainActivity.class);
            int newUser = response.getExt().getNewUser();
            //之前是游客身份并且后来登录的手机号是新用户就需要同步数据
            if (newUser == 1) {//1-是新用户 0-不是新用户
                ContentManager.getInstance().setVisitorToken("");
            }
            mIntent.putExtra(MainActivity.IS_NEW_USER, newUser);//判断是不是新用户
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            ContentManager.getInstance().setVisitor("1");//表示不是游客登录
            finish();
            MTing.getActivityManager().popAllActivitys();
        }
    }

    @Override
    public void onSmsLoginError(int code, String errorMsg) {
        switch (code) {
            case -3:
                pCcode.clearText();
                toastShort("验证码输入错误，请重新输入");
                break;
            default:
                pCcode.clearText();
                toastShort(errorMsg);
                break;
        }
    }
}
