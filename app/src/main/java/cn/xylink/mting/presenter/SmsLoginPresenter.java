package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.contract.SmsLoginContact;
import cn.xylink.mting.model.SmsLoginRequset;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.model.data.SmsLoginResponse;
import cn.xylink.mting.utils.L;

public class SmsLoginPresenter extends BasePresenter<SmsLoginContact.ISmsLoginView> implements SmsLoginContact.Presenter {
    @Override
    public void onSmsLogin(SmsLoginRequset request) {
        L.v("request", request);


        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(request);
        L.v(json);
        OkGoUtils.getInstance().postData(mView, RemoteUrl.smsLogin(), json, new TypeToken<SmsLoginResponse>() {

        }.getType(), new OkGoUtils.ICallback() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onSuccess(Object data) {
                SmsLoginResponse baseResponse = (SmsLoginResponse) data;
                int code = baseResponse.getCode();
                if (code == 200) {
                    mView.onSmsLoginSuccess(baseResponse);
                } else {
                    mView.onSmsLoginError(code, baseResponse.getMessage());
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.onSmsLoginError(code, errorMsg);
            }

            @Override
            public void onComplete() {
                mView.hideLoading();
            }
        });
    }
}
