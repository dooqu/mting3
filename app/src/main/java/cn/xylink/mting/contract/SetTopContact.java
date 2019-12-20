package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.SetTopRequest;

/**
 * -----------------------------------------------------------------
 * 2019/11/15 17:16 : Create SetTopContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface SetTopContact {

    interface ISetTopView extends IBaseView {
        void onSetTopSuccess(BaseResponse response,String event);

        void onSetTopError(int code, String errorMsg,String event);
    }

    interface Presenter<T> {
        void setTop(SetTopRequest request);
    }
}
