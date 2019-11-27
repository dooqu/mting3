package cn.xylink.mting.contract;

import com.tencent.mm.opensdk.modelbase.BaseResp;

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
        void onBroadcastItemAddListSuccess(List<BroadcastItemAddInfo> data);

        void onBroadcastItemAddListError(int code, String errorMsg);

        void onBroadcastItemAddSuccess(BaseResponse<String> baseResponse);

        void onBroadcastItemAddError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void getBroadcastItemAddList(BaseRequest request);
        void getBroadcastItemAdd(BroadcastItemAddRequest request);
    }
}
