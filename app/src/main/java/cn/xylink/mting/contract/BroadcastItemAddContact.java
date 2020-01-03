package cn.xylink.mting.contract;

import java.util.List;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastItemAddInfo;
import cn.xylink.mting.bean.BroadcastItemAddRequest;

/**
 * @author wjn
 * @date 2019/11/26
 */
public interface BroadcastItemAddContact {
    interface IBroadcastItemAddView extends IBaseView {
        void onBroadcastItemAddListSuccess(List<BroadcastItemAddInfo> data);//播单列表

        void onBroadcastItemAddListError(int code, String errorMsg);

        void onBroadcastItemAddSuccess(BaseResponse<String> baseResponse);//播单文章

        void onBroadcastItemAddError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void getBroadcastItemAddList(BaseRequest request);

        void getBroadcastItemAdd(BroadcastItemAddRequest request);
    }
}
