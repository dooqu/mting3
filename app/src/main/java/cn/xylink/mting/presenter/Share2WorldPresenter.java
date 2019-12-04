package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastIdRequest;
import cn.xylink.mting.contract.Share2WorldContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/12/3 14:46 : Create Share2WorldPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class Share2WorldPresenter extends BasePresenter<Share2WorldContact.ISetTopView> implements Share2WorldContact.Presenter{
    @Override
    public void share2World(BroadcastIdRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getShare2WorldUrl(), new Gson().toJson(request),
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
                            mView.onShare2WorldSuccess(baseResponse);
                        } else {
                            mView.onShare2WorldError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onShare2WorldError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
