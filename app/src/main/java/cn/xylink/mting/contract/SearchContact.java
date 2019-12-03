package cn.xylink.mting.contract;

import java.util.List;

import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.SearchInfo;
import cn.xylink.mting.bean.SearchRequest;
import cn.xylink.mting.bean.TingInfo;

/**
 * -----------------------------------------------------------------
 * 2019/12/2 11:44 : Create SearchContact.java (JoDragon);
 * -----------------------------------------------------------------
 */
public interface SearchContact {

    interface ISearchView extends IBaseView {
        void onSearchArticleSuccess(List<SearchInfo> response);

        void onSearchArticleError(int code, String errorMsg);

        void onSearchBroadcastSuccess(List<SearchInfo> response);

        void onSearchBroadcastError(int code, String errorMsg);
    }

    interface Presenter<T> {
        void searchArticle(SearchRequest request);

        void searchBroadcast(SearchRequest request);
    }
}
