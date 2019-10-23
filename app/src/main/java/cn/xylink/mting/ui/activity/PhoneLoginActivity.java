package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.MTing;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.CodeInfo;
import cn.xylink.mting.contract.GetCodeContact;
import cn.xylink.mting.model.GetCodeRequest;
import cn.xylink.mting.model.data.HttpConst;
import cn.xylink.mting.presenter.GetCodePresenter;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.utils.NetworkUtil;
import cn.xylink.mting.utils.PhoneNumberUtils;
import cn.xylink.mting.utils.TingUtils;
import cn.xylink.mting.widget.ZpPhoneEditText;

public class PhoneLoginActivity extends BasePresenterActivity implements GetCodeContact.IGetCodeView {

    public static final String EXTRA_PHONE = "extra_phone";
    public static final String EXTRA_SOURCE = "extra_source";
    public static final String EXTRA_CODE = "extra_code";
    @BindView(R.id.et_phone)
    ZpPhoneEditText etPhone;
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.btn_next)
    Button mBtnNext;

    @BindView(R.id.iv_del_et)
    ImageView ivDelEt;

    private GetCodePresenter codePresenter;
    private String phone;


    @Override
    protected void preView() {
        setContentView(R.layout.activity_phone_login);
        codePresenter = (GetCodePresenter) createPresenter(GetCodePresenter.class);
        codePresenter.attachView(this);
        MTing.getActivityManager().pushActivity(this);

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
        tvTitle.setText("手机号登录");
        mBtnNext.setEnabled(false);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ivDelEt.setVisibility(View.VISIBLE);
                    mBtnNext.setBackground(getResources().getDrawable(R.drawable.bg_phone_click_btn));
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setBackground(getResources().getDrawable(R.drawable.bg_phone_default_btn));
                    ivDelEt.setVisibility(View.GONE);
                    mBtnNext.setEnabled(false);
                }
            }
        });
//        etPhone.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {

    }

    @OnClick({R.id.btn_next, R.id.iv_del_et, R.id.btn_left})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.iv_del_et:
                etPhone.setText("");
                break;
            case R.id.btn_next:
                int netWorkStates = NetworkUtil.getNetWorkStates(context);
                if (netWorkStates == NetworkUtil.TYPE_NONE) {
                    toastShort(HttpConst.NO_NETWORK);
                    return;
                }
                phone = etPhone.getText().toString();
                phone = phone.replaceAll(" ", "");
                if (phone.length() == 0) {
                    toastShort("手机号不能为空");
                    return;
                } else if (phone.length() < 11) {
                    toastShort("手机号码应该是11位数字");
                    return;
                } else if (!PhoneNumberUtils.isMobileNO(phone)) {
                    toastShort("手机号码输入有误，请重新输入");
                    return;
                }
                GetCodeRequest requset = new GetCodeRequest();
                requset.setDeviceId(TingUtils.getDeviceId(getApplicationContext()));
                requset.phone = phone;
                requset.source = "";
                requset.doSign();
                codePresenter.onGetCode(requset);

                break;
        }
    }


    @Override
    public void onCodeSuccess(BaseResponse<CodeInfo> response) {
        final int code = response.code;

//        switch (code)
//        {
//            case 200:
//            case -3:{

        Intent mIntent = new Intent(this, GetCodeActivity.class);
        mIntent.putExtra(EXTRA_PHONE, phone);
        mIntent.putExtra(EXTRA_SOURCE, "register");
        mIntent.putExtra(EXTRA_CODE, response.data.getCodeId());
        startActivity(mIntent);
//                break;
//            }
//            case -2: {
//                Intent mIntent = new Intent(this, LoginPwdActivity.class);
//                mIntent.putExtra(EXTRA_PHONE, phone);
//                startActivity(mIntent);
//                break;
//            }
//        }
    }

    @Override
    public void onCodeError(int code, String errorMsg) {
        L.v("code", code);
        if (!TextUtils.isEmpty(errorMsg)) {
            toastShort(errorMsg);
        }
//        switch (code)
//        {
//            case -3:
//
//                Intent mIntent = new Intent(this,GetCodeActivity.class);
//                mIntent.putExtra(EXTRA_PHONE,phone);
//                mIntent.putExtra(EXTRA_CODE,)
//                startActivity(mIntent);
//                break;
//        }
    }
}
