package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.QrInfo;

/**
 * -----------------------------------------------------------------
 * 2020/4/26 14:23 : Create QrContract.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface QrContract {

    interface IQrView extends IBaseView {
        void onQrSuccess(QrInfo response);

        void onQrError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void qr(String url, BaseRequest request);
    }
}
