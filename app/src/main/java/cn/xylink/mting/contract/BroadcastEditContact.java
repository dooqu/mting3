package cn.xylink.mting.contract;

import java.io.File;
import java.util.Map;

import cn.xylink.mting.base.BaseResponse;

/**
 * @author wjn
 * @date 2019/11/18
 */
public interface BroadcastEditContact {
    interface IBroadcastEditView extends IBaseView {

        void onSuccessBroadcastEdit(BaseResponse baseResponse);

        void onErrorBroadcastEdit(int code, String errorMsg);
    }

    interface Presenter<T> {
        void onBroadcastEdit(Map<String, String> data, File file);
        void onBroadcastEdit(Map<String, String> data);
    }
}
