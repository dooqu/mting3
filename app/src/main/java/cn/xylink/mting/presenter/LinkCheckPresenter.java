package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.LinkArticle;
import cn.xylink.mting.bean.LinkCheckRequest;
import cn.xylink.mting.contract.LinkCheckContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/12/19 14:47 : Create LinkCheckPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class LinkCheckPresenter extends BasePresenter<LinkCheckContact.ILinkCheckView> implements LinkCheckContact.Presenter {
    @Override
    public void checkLink(LinkCheckRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.checkLinkUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse<LinkArticle>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse<LinkArticle> baseResponse = (BaseResponse<LinkArticle>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onLinkCheckSuccess(baseResponse.data);
                        } else {
                            mView.onLinkCheckError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onLinkCheckError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
