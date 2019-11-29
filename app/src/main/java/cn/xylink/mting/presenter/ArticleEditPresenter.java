package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleCreateInputInfo;
import cn.xylink.mting.bean.ArticleCreateInputRequest;
import cn.xylink.mting.bean.ArticleEditRequest;
import cn.xylink.mting.contract.ArticleCreateContact;
import cn.xylink.mting.contract.ArticleEditContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.utils.L;

/**
 * @author wjn
 * @date 2019/11/22
 */
public class ArticleEditPresenter extends BasePresenter<ArticleEditContact.IArticleEditView> implements ArticleEditContact.Presenter {
    @Override
    public void onArticleEdit(ArticleEditRequest articleEditRequest) {
        String json = new Gson().toJson(articleEditRequest);
        L.v(json);
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getArticleEditUrl(), json, new TypeToken<BaseResponse<String>>() {

        }.getType(), new OkGoUtils.ICallback() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onSuccess(Object data) {
                BaseResponse<String> baseResponse = (BaseResponse<String>) data;
                L.v("baseResponse", baseResponse.toString());
                int code = baseResponse.code;
                if (200 == code) {
                    mView.onArticleEditSuccess(baseResponse);
                } else {
                    mView.onArticleEditError(code, baseResponse.message);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.onArticleEditError(code, errorMsg);
            }

            @Override
            public void onComplete() {
                mView.hideLoading();
            }
        });
    }
}
