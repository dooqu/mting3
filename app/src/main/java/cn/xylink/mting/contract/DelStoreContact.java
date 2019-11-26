package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.DelStoreRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/26 14:28 : Create DelStoreContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface DelStoreContact {
    interface IDelStoreView extends IBaseView {
        void onDelStoreSuccess(BaseResponse response);

        void onDelStoreError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void delStore(DelStoreRequest request);
    }
}
