package cn.xylink.mting.contract;

import java.util.List;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.bean.BroadcastItemAddInfo;

/**
 * @author wjn
 * @date 2019/11/26
 */
public interface BroadcastItemAddContact {
    interface IBroadcastItemAddView extends IBaseView {
        void onBroadcastItemAddSuccess(List<BroadcastItemAddInfo> data, boolean isLoadMore);

        void onBroadcastItemAddError(int code, String errorMsg, boolean isLoadMore);
    }

    interface Presenter<T> {
        void getBroadcastItemAddList(BaseRequest request, boolean isLoadMore);
    }
}
