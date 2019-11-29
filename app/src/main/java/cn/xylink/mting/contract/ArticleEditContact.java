package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleCreateInputInfo;
import cn.xylink.mting.bean.ArticleCreateInputRequest;
import cn.xylink.mting.bean.ArticleEditRequest;

/**
 * @author wjn
 * @date 2019/11/22
 */
public interface ArticleEditContact {
    interface IArticleEditView extends IBaseView {
        void onArticleEditSuccess(BaseResponse<String> baseResponse);

        void onArticleEditError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void onArticleEdit(ArticleEditRequest articleEditRequest);
    }
}
