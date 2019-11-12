package cn.xylink.mting.contract;

import java.util.List;

import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastListRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/11 15:59 : Create BroadcastListContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface BroadcastListContact {
    interface IBroadcastListView extends IBaseView {
        void onBroadcastListSuccess(List<BroadcastInfo> data);

        void onBroadcastListError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void getBroadcastList(BroadcastListRequest request);
    }
}
