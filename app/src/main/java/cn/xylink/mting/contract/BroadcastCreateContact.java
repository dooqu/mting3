package cn.xylink.mting.contract;

import java.io.File;
import java.util.Map;

import cn.xylink.mting.bean.BroadcastCreateInfo;
import cn.xylink.mting.base.BaseResponse;

/**
 * @author wjn
 * @date 2019/11/18
 */
public interface BroadcastCreateContact {
    interface ICreateBroadcastView extends IBaseView {

        void onSuccessCreateBroadcast(BaseResponse<BroadcastCreateInfo> baseResponse);

        void onErrorCreateBroadcast(int code, String errorMsg);
    }

    interface Presenter<T> {
        void onCreateBroadcast(Map<String, String> data, File file);
        void onCreateBroadcast(Map<String, String> data);
    }
}
