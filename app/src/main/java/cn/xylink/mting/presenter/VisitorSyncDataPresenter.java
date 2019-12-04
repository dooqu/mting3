package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.UserInfo;
import cn.xylink.mting.contract.SmsLoginContact;
import cn.xylink.mting.contract.VisitorSyncDataContact;
import cn.xylink.mting.model.SmsLoginRequset;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.model.data.VisitorSyncDataRequest;
import cn.xylink.mting.utils.L;

public class VisitorSyncDataPresenter extends BasePresenter<VisitorSyncDataContact.IVisitorSyncDataView> implements VisitorSyncDataContact.Presenter {
    @Override
    public void onVisitorSyncData(VisitorSyncDataRequest request) {
        L.v("request", request);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(request);
        L.v(json);
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getVisitorSyncDataUrl(), json, new TypeToken<BaseResponse<String>>() {

        }.getType(), new OkGoUtils.ICallback() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onSuccess(Object data) {
                BaseResponse<String> baseResponse = (BaseResponse<String>) data;
                int code = baseResponse.code;
                if (code == 200) {
                    mView.onVisitorSyncDataSuccess(baseResponse);
                } else {
                    mView.onVisitorSyncDataError(code, baseResponse.message);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.onVisitorSyncDataError(code, errorMsg);
            }

            @Override
            public void onComplete() {
                mView.hideLoading();
            }
        });
    }
}
