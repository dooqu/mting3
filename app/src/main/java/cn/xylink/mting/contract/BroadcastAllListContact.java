package cn.xylink.mting.contract;

import java.util.List;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.bean.BroadcastAllListInfo;

/**
 * -----------------------------------------------------------------
 * 2019/12/25 11:09 : Create BroadcastAllListContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface BroadcastAllListContact {

    interface IBroadcastAllListView extends IBaseView {
        void onBroadcastAllListSuccess(List<BroadcastAllListInfo> infos);

        void onBroadcastAllListError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void getAllList(BaseRequest request);
    }
}
