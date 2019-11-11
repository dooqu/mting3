package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.TingInfo;

/**
 *享听列表
 *
 * -----------------------------------------------------------------
 * 2019/11/5 15:40 : Create TingListContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface TingListContact {
    interface ITingListView extends IBaseView {
        void onTingListSuccess(BaseResponseArray<TingInfo> response);

        void onTingListError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void getTingList(BaseRequest request);
    }
}
