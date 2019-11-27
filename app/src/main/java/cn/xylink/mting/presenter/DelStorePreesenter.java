package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleIdsRequest;
import cn.xylink.mting.contract.DelStoreContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/11/26 14:32 : Create DelStorePreesenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class DelStorePreesenter extends BasePresenter<DelStoreContact.IDelStoreView> implements DelStoreContact.Presenter {
    @Override
    public void delStore(ArticleIdsRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getDelStoreUrl(), new Gson().toJson(request),
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
                            mView.onDelStoreSuccess(baseResponse);
                        } else {
                            mView.onDelStoreError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onDelStoreError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
