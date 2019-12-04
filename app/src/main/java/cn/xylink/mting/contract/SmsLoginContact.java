package cn.xylink.mting.contract;

import cn.xylink.mting.model.SmsLoginRequset;
import cn.xylink.mting.model.data.SmsLoginResponse;

public interface SmsLoginContact {
    interface ISmsLoginView extends IBaseView {
        void onSmsLoginSuccess(SmsLoginResponse smsLoginResponse);

        void onSmsLoginError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void onSmsLogin(SmsLoginRequset loginRequest);
    }
}
