package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastIdRequest;

/**
 * -----------------------------------------------------------------
 * 2019/12/3 14:45 : Create Share2WorldContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface Share2WorldContact {

    interface ISetTopView extends IBaseView {
        void onShare2WorldSuccess(BaseResponse response);

        void onShare2WorldError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void share2World(BroadcastIdRequest request);
    }
}
