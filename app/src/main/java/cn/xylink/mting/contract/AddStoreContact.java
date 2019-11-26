package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.AddStoreRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/26 14:28 : Create AddStoreContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface AddStoreContact {
    interface IAddStoreView extends IBaseView {
        void onAddStoreSuccess(BaseResponse response);

        void onAddStoreError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void addStore(AddStoreRequest request);
    }
}
