package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastItemAddRequest;
import cn.xylink.mting.contract.BroadcastAddArticleContact;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.OkGoUtils;

/**
 * -----------------------------------------------------------------
 * 2019/12/24 15:14 : Create BroadcastAddArticlePresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastAddArticlePresenter  extends BasePresenter<BroadcastAddArticleContact.IBroadcastAddArticleView> implements BroadcastAddArticleContact.Presenter {

    @Override
    public void broadcastAddArticle(BroadcastItemAddRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.addBroadcastItemUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        baseResponse.setData(request);
                        if (code == 200) {
                            mView.onBroadcastAddArticleSuccess(baseResponse);
                        } else {
                            mView.onBroadcastAddArticleError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastAddArticleError(code, errorMsg);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}