package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.AddFeedbackRequest;

public interface AddFeedbackContact {
    interface IAddFeedBackView extends IBaseView {
        void onAddFeedBackSuccess(BaseResponse<String> response);

        void onBindCheckError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void onFeedBack(AddFeedbackRequest request);
    }
}
