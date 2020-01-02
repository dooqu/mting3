package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.LinkArticle;
import cn.xylink.mting.bean.LinkCreateRequest;
import cn.xylink.mting.contract.LinkCreateContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.L;

public class LinkCreatePresenter extends BasePresenter<LinkCreateContact.IPushView> implements LinkCreateContact.Presenter {
    @Override
    public void onPush(LinkCreateRequest request) {
        L.v("request", request);
//        Gson gs = new GsonBuilder()
//                .setPrettyPrinting()
//                .disableHtmlEscaping()
//                .create();
//        String json = gs.toJson(request);
        OkGoUtils.getInstance().postData(mView, RemoteUrl.linkCreateUrl(), new Gson().toJson(request), new TypeToken<BaseResponse<LinkArticle>>() {

        }.getType(), new OkGoUtils.ICallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(Object data) {
                BaseResponse<LinkArticle> baseResponse = (BaseResponse<LinkArticle>) data;
                int code = baseResponse.code;
                if (code == 200) {
                    mView.onPushSuccess(baseResponse);
                } else {
                    if (null != mView) {
                        mView.onPushError(code, baseResponse.message);
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                if (null != mView) {
                    mView.onPushError(code, errorMsg);
                }
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
