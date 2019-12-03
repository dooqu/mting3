package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.SearchInfo;
import cn.xylink.mting.bean.SearchRequest;
import cn.xylink.mting.bean.TingInfo;
import cn.xylink.mting.contract.SearchContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/12/2 11:51 : Create SearchPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SearchPresenter extends BasePresenter<SearchContact.ISearchView> implements SearchContact.Presenter {
    @Override
    public void searchArticle(SearchRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getSearchArticleUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponseArray<SearchInfo>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponseArray<SearchInfo> baseResponse = (BaseResponseArray<SearchInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onSearchArticleSuccess(baseResponse.data);
                        } else {
                            mView.onSearchArticleError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onSearchArticleError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void searchBroadcast(SearchRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getSearchBroadcastUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponseArray<SearchInfo>>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponseArray<SearchInfo> baseResponse = (BaseResponseArray<SearchInfo>) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onSearchBroadcastSuccess(baseResponse.data);
                        } else {
                            mView.onSearchBroadcastError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onSearchBroadcastError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
