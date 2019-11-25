package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleCreateInputInfo;
import cn.xylink.mting.bean.ArticleCreateInputRequest;
import cn.xylink.mting.contract.ArticleCreateContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.L;

/**
 * @author wjn
 * @date 2019/11/22
 */
public class ArticleCreatePresenter extends BasePresenter<ArticleCreateContact.IArticleCreateView> implements ArticleCreateContact.Presenter {
    @Override
    public void onArticleCreate(ArticleCreateInputRequest articleCreateRequest) {
        String json = new Gson().toJson(articleCreateRequest);
        L.v(json);
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getArticleCreateInputUrl(), json, new TypeToken<BaseResponse<ArticleCreateInputInfo>>() {

        }.getType(), new OkGoUtils.ICallback() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onSuccess(Object data) {
                BaseResponse<ArticleCreateInputInfo> baseResponse = (BaseResponse<ArticleCreateInputInfo>) data;
                L.v("baseResponse", baseResponse.toString());
                int code = baseResponse.code;
                if (200 == code) {
                    mView.onArticleCreateSuccess(baseResponse);
                } else {
                    mView.onArticleCreateError(code, baseResponse.message);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.onArticleCreateError(code, errorMsg);
            }

            @Override
            public void onComplete() {
                mView.hideLoading();
            }
        });
    }
}
