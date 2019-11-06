package cn.xylink.mting.contract;

import java.util.List;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.TingInfo;
import cn.xylink.mting.bean.WorldInfo;
import cn.xylink.mting.bean.WorldRequest;

/**
 * 世界列表
 * -----------------------------------------------------------------
 * 2019/11/6 11:59 : Create WorldListContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface WorldListContact {
    interface IWorldListView extends IBaseView {
        void onWorldListSuccess(List<WorldInfo> data);
        void onWorldListError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void getWorldList(WorldRequest request);
    }

}
