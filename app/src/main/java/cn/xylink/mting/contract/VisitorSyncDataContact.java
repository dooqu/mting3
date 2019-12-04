package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.UserInfo;
import cn.xylink.mting.model.SmsLoginRequset;
import cn.xylink.mting.model.data.VisitorSyncDataRequest;

public interface VisitorSyncDataContact {
    interface IVisitorSyncDataView extends IBaseView {
        void onVisitorSyncDataSuccess(BaseResponse<String> baseResponse);

        void onVisitorSyncDataError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void onVisitorSyncData(VisitorSyncDataRequest visitorSyncDataRequest);
    }
}
