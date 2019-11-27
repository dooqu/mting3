package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleIdsRequest;
import cn.xylink.mting.bean.BroadcastIdRequest;

/**
 * 播放删除接口集合
 * -----------------------------------------------------------------
 * 2019/11/27 16:18 : Create BroadcastAllDelContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface BroadcastAllDelContact {

    interface IBroadcastAllDelView extends IBaseView {
        void onBroadcastAllDelSuccess(BaseResponse response);

        void onBroadcastAllDelError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void delStore(ArticleIdsRequest request);

        void delUnread(ArticleIdsRequest request);

        void delReaded(ArticleIdsRequest request);

        void delMyCreateArticle(ArticleIdsRequest request);

        void delMyCreateBroadcastArticle(ArticleIdsRequest request);

        void delBroadcast(BroadcastIdRequest request);
    }
}
