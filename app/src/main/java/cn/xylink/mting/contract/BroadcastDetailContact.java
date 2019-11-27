package cn.xylink.mting.contract;

import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastIdRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/19 17:24 : Create BroadcastDetailContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface BroadcastDetailContact {

    interface IBroadcastDetailView extends IBaseView {
        void onBroadcastDetailSuccess(BroadcastDetailInfo data);

        void onBroadcastDetailError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void getBroadcastDetail(BroadcastIdRequest request);
    }
}
