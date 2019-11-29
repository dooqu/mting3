package cn.xylink.mting.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleIdsRequest;
import cn.xylink.mting.bean.BroadcastIdRequest;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.DelBroadcastArticleRequest;
import cn.xylink.mting.contract.BroadcastAllDelContact;
import cn.xylink.mting.model.data.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;

/**
 * -----------------------------------------------------------------
 * 2019/11/27 16:27 : Create BroadcastAllDelPresenter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastAllDelPresenter extends BasePresenter<BroadcastAllDelContact.IBroadcastAllDelView> implements BroadcastAllDelContact.Presenter {
    @Override
    public void delStore(ArticleIdsRequest request, BroadcastInfo info) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getDelStoreUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastAllDelSuccess(baseResponse,info);
                        } else {
                            mView.onBroadcastAllDelError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastAllDelError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void delUnread(ArticleIdsRequest request, BroadcastInfo info ) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getDelUnreadUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastAllDelSuccess(baseResponse,info);
                        } else {
                            mView.onBroadcastAllDelError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastAllDelError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void delReaded(ArticleIdsRequest request, BroadcastInfo info ) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getDelReadedUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastAllDelSuccess(baseResponse,info);
                        } else {
                            mView.onBroadcastAllDelError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastAllDelError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void delMyCreateArticle(ArticleIdsRequest request, BroadcastInfo info ) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getDelMyCreateArticleUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastAllDelSuccess(baseResponse,info);
                        } else {
                            mView.onBroadcastAllDelError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastAllDelError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void delMyCreateBroadcastArticle(DelBroadcastArticleRequest request, BroadcastInfo info ) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getDelBroadcastArticleUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastAllDelSuccess(baseResponse,info);
                        } else {
                            mView.onBroadcastAllDelError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastAllDelError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void delBroadcast(BroadcastIdRequest request) {
        OkGoUtils.getInstance().postData(mView, RemoteUrl.getDelBroadcastUrl(), new Gson().toJson(request),
                new TypeToken<BaseResponse>() {

                }.getType(), new OkGoUtils.ICallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(Object data) {
                        BaseResponse baseResponse = (BaseResponse) data;
                        int code = baseResponse.code;
                        if (code == 200) {
                            mView.onBroadcastAllDelSuccess(baseResponse,null);
                        } else {
                            mView.onBroadcastAllDelError(code, baseResponse.message);
                        }
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        mView.onBroadcastAllDelError(code, errorMsg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
