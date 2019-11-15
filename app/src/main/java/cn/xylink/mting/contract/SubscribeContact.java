package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.SubscribeRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/15 17:26 : Create SubscribeContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface SubscribeContact {

    interface ISubscribeView extends IBaseView {
        void onSubscribeSuccess(BaseResponse response);

        void onSubscribeError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void subscribe(SubscribeRequest request);
    }
}
