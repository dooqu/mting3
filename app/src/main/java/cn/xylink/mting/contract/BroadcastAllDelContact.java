package cn.xylink.mting.contract;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleIdsRequest;
import cn.xylink.mting.bean.BroadcastIdRequest;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.DelBroadcastArticleRequest;

/**
 * 播放删除接口集合
 * -----------------------------------------------------------------
 * 2019/11/27 16:18 : Create BroadcastAllDelContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface BroadcastAllDelContact {

    interface IBroadcastAllDelView extends IBaseView {
        void onBroadcastAllDelSuccess(BaseResponse response, BroadcastInfo info);

        void onBroadcastAllDelError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void delStore(ArticleIdsRequest request, BroadcastInfo info);

        void delUnread(ArticleIdsRequest request, BroadcastInfo info);

        void delReaded(ArticleIdsRequest request, BroadcastInfo info);

        void delMyCreateArticle(ArticleIdsRequest request, BroadcastInfo info);

        void delMyCreateBroadcastArticle(DelBroadcastArticleRequest request, BroadcastInfo info);

        void delBroadcast(BroadcastIdRequest request);
    }
}
