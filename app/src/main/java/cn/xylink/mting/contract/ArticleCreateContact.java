package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleCreateInputInfo;
import cn.xylink.mting.bean.ArticleCreateInputRequest;

/**
 * @author wjn
 * @date 2019/11/22
 */
public interface ArticleCreateContact {
    interface IArticleCreateView extends IBaseView {
        void onArticleCreateSuccess(BaseResponse<ArticleCreateInputInfo> baseResponse);

        void onArticleCreateError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void onArticleCreate(ArticleCreateInputRequest articleCreateRequest);
    }
}
