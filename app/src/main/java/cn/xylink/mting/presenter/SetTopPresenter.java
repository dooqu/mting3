package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.SetTopRequest;
import cn.xylink.mting.contract.SetTopContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/11/15 17:18 : Create SetTopPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SetTopPresenter extends BasePresenter<SetTopContact.ISetTopView> implements SetTopContact.Presenter {
    @Override
    public void setTop(SetTopRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getSetTopUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onSetTopSuccess(baseResponse);
                        } else {
                            mView.onSetTopError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onSetTopError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
