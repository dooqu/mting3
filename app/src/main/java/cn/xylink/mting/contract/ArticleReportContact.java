package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleReportRequest;

/**
 * @author wjn
 * @date 2019/12/10
 */
public interface ArticleReportContact {
    interface IDelStoreView extends IBaseView {
        void onArticleReportSuccess(BaseResponse response);

        void onArticleReportError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void getArticleReport(ArticleReportRequest request);
    }
}
