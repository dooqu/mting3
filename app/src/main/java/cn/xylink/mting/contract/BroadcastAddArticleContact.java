package cn.xylink.mting.contract;


import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastItemAddRequest;

/**
 * -----------------------------------------------------------------
 * 2019/12/24 15:11 : Create BroadcastAddArticleContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface BroadcastAddArticleContact {
    interface IBroadcastAddArticleView extends IBaseView {
        void onBroadcastAddArticleSuccess(BaseResponse baseResponse);

        void onBroadcastAddArticleError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void broadcastAddArticle(BroadcastItemAddRequest request);
    }
}
