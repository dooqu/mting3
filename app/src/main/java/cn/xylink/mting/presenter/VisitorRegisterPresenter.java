package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.MTing;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.CodeInfo;
import cn.xylink.mting.bean.VisitorRegisterInfo;
import cn.xylink.mting.contract.VisitorRegisterContact;
import cn.xylink.mting.model.VisitorRegisterRequest;
import cn.xylink.mting.model.data.Const;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.FileUtil;
import cn.xylink.mting.utils.L;

/**
 * @author wjn
 * @date 2019/11/15
 */
public class VisitorRegisterPresenter extends BasePresenter<VisitorRegisterContact.IVisitorRegisterView> implements VisitorRegisterContact.Presenter {
    @Override
    public void onVisitorRegister(VisitorRegisterRequest registerRequest) {
        L.v("registerRequest", registerRequest);
        String json = new Gson().toJson(registerRequest);
        L.v("json", json);
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getVisitorRegisterUrl(), json, new TypeToken<BaseResponse<VisitorRegisterInfo>>() {

        }.getType(), new OkGoUtils.ICallback() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onSuccess(Object data) {
                cn.xylink.mting.base.BaseResponse<VisitorRegisterInfo> baseResponse = (BaseResponse<VisitorRegisterInfo>) data;
                int code = baseResponse.code;
                L.v("coce", code);
                if (code == 200) {
                    mView.onVisitorRegisterSuccess(baseResponse);
                } else {
                    mView.onVisitorRegisterError(code, baseResponse.message);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.onVisitorRegisterError(code, errorMsg);
            }

            @Override
            public void onComplete() {
                mView.hideLoading();
            }
        });
    }
}
