package cn.xylink.mting.contract;

import cn.xylink.mting.bean.ArticleDetail2Info;
import cn.xylink.mting.bean.ArticleDetailRequest;

/**
 * @author wjn
 * @date 2019/11/28
 */
public interface ArticleDetailContract {
    interface IArticleDetailView extends IBaseView {
        void onSuccessArticleDetail(ArticleDetail2Info info);

        void onErrorArticleDetail(int code, String errorMsg);
    }

    interface Presenter<T> {
        void createArticleDetail(ArticleDetailRequest request);
    }
}
