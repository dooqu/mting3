package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.SubscribeRequest;
import cn.xylink.mting.contract.SubscribeContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/11/15 17:28 : Create SubscribePresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SubscribePresenter extends BasePresenter<SubscribeContact.ISubscribeView> implements SubscribeContact.Presenter {
    @Override
    public void subscribe(SubscribeRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getSubscribeUrl(), new Gson().toJson(request),
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
                            mView.onSubscribeSuccess(baseResponse);
                        } else {
                            mView.onSubscribeError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onSubscribeError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
