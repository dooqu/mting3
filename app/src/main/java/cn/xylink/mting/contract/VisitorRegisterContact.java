package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.VisitorRegisterInfo;
import cn.xylink.mting.model.VisitorRegisterRequest;

/**
 * @author wjn
 * @date 2019/11/14
 */
public interface VisitorRegisterContact {
    interface IVisitorRegisterView extends IBaseView {
        void onVisitorRegisterSuccess(BaseResponse<VisitorRegisterInfo> visitorRegisterInfoBaseResponse);

        void onVisitorRegisterError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void onVisitorRegister(VisitorRegisterRequest registerRequest);
    }
}
