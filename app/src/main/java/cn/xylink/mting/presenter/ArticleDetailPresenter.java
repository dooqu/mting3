package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleDetail2Info;
import cn.xylink.mting.bean.ArticleDetailRequest;
import cn.xylink.mting.contract.ArticleDetailContract;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * @author wjn
 * @date 2019/11/28
 */
public class ArticleDetailPresenter extends BasePresenter<ArticleDetailContract.IArticleDetailView> implements ArticleDetailContract.Presenter {
    @Override
    public void createArticleDetail(ArticleDetailRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getArticleDetailUrl(), new Gson().toJson(request), new TypeToken<BaseResponse<ArticleDetail2Info>>() {
        }.getType(), new OkGoUtils.ICallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                BaseResponse<ArticleDetail2Info> baseResponse = (BaseResponse<ArticleDetail2Info>) data;
                int code = baseResponse.code;
                if (code == 200) {
                    mView.onSuccessArticleDetail(baseResponse.data);
                } else {
                    mView.onErrorArticleDetail(code, baseResponse.message);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                mView.onErrorArticleDetail(code, errorMsg);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
