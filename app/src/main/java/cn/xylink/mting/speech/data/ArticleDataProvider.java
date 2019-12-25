package cn.xylink.mting.speech.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.contract.IBaseView;
import cn.xylink.mting.model.ArticleInfoRequest;
import cn.xylink.mting.model.ArticleInfoResponse;
import cn.xylink.mting.model.ArticleReadRequest;
import cn.xylink.mting.model.FavoriteArticleRequest;
import cn.xylink.mting.model.SpeechListRequest;
import cn.xylink.mting.model.SpeechListResponse;
import cn.xylink.mting.model.data.SpeechListNearByRequest;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.OkGoUtils;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.speech.SoundEffector;
import cn.xylink.mting.utils.GsonUtil;


/*
文章正文内容的网络加载类
 */
public class ArticleDataProvider {

    static String TAG = ArticleDataProvider.class.getSimpleName();
    /*
    文章正文加载的回调，采用函数接口形式
    invoke(errorCode, Article article)
    errorCode: 0时无错误
    article:是返回的正文已经加载完成的Article
     */

    public class ArticleListArgument {
        public Article article;
        public List<Article> list;
    }

    @FunctionalInterface
    public static interface ArticleLoaderCallback {
        void invoke(int errorCode, Article article);
    }

    @FunctionalInterface
    public static interface ArticleLoader<T> {
        void invoke(int errorCode, T data);
    }

    SoundEffector soundEffector;
    Article currentArticle;
    long tickcount = 0;
    Handler handler;

    public ArticleDataProvider(Context context) {
        soundEffector = new SoundEffector(context);
        currentArticle = null;
        handler = new Handler(Looper.getMainLooper());
    }

    public void getSpeechListNearBy(Article article, String event, ArticleLoader<List<Article>> callback) {
        SpeechListNearByRequest request = new SpeechListNearByRequest();
        request.setLastAt(article.getCreateAt());
        request.setBroadcastId(article.getBroadcastId());
        request.setEvent(event);
        request.setToken(ContentManager.getInstance().getLoginToken());
        request.doSign();
        OkGoUtils.getInstance().postData(
                new IBaseView() {
                    @Override
                    public void showLoading() {
                    }

                    @Override
                    public void hideLoading() {
                    }
                },
                RemoteUrl.getSpeechListNearByUrl(),
                GsonUtil.GsonString(request), SpeechListResponse.class,
                new OkGoUtils.ICallback<SpeechListResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (callback != null) {
                            callback.invoke(code, null);
                        }
                    }

                    @Override
                    public void onSuccess(SpeechListResponse response) {
                        if (response.getCode() == 200) {
                            List<Article> list = response.getData();
                            for (Article articleFinded : list) {
                                articleFinded.setBroadcastId(article.getBroadcastId());
                                articleFinded.setBroadcastTitle(article.getBroadcastTitle());
                            }
                            if (callback != null) {
                                callback.invoke(0, response.getData());
                            }
                        }
                        else {
                            onFailure(response.getCode(), response.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });
    }

    public void getSpeechList(Article article, ArticleLoader<List<Article>> callback) {
        SpeechListRequest request = new SpeechListRequest();
        request.setBroadcastId(article.getBroadcastId());
        request.setArticleId(article.getArticleId());
        request.setToken(ContentManager.getInstance().getLoginToken());
        request.doSign();
        OkGoUtils.getInstance().postData(
                new IBaseView() {
                    @Override
                    public void showLoading() {
                    }

                    @Override
                    public void hideLoading() {
                    }
                },
                RemoteUrl.getSpeechListUrl(),
                GsonUtil.GsonString(request), SpeechListResponse.class,
                new OkGoUtils.ICallback<SpeechListResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (callback != null) {
                            callback.invoke(code, null);
                        }
                    }

                    @Override
                    public void onSuccess(SpeechListResponse response) {
                        if (response.getCode() == 200) {
                            List<Article> list = response.getData();
                            for (Article articleFinded : list) {
                                articleFinded.setBroadcastId(article.getBroadcastId());
                                articleFinded.setBroadcastTitle(article.getBroadcastTitle());
                            }
                            if (callback != null) {
                                callback.invoke(0, response.getData());
                            }
                        }
                        else {
                            onFailure(response.getCode(), response.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });
    }


    public void getUnreadSpeechList(ArticleLoader<List<Article>> callback) {
        SpeechListRequest request = new SpeechListRequest();
        request.setBroadcastId("-1");
        request.setToken(ContentManager.getInstance().getLoginToken());
        request.doSign();
        OkGoUtils.getInstance().postData(
                new IBaseView() {
                    @Override
                    public void showLoading() {
                    }

                    @Override
                    public void hideLoading() {
                    }
                },
                RemoteUrl.getBroadcastlListUrl(),
                GsonUtil.GsonString(request), SpeechListResponse.class,
                new OkGoUtils.ICallback<SpeechListResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (callback != null) {
                            callback.invoke(code, null);
                        }
                    }

                    @Override
                    public void onSuccess(SpeechListResponse response) {
                        if (response.getCode() == 200) {
                            List<Article> list = response.getData();
                            for (Article article : list) {
                                article.setBroadcastId("-1");
                            }
                            if (callback != null) {
                                callback.invoke(0, response.getData());
                            }
                        }
                        else {
                            onFailure(response.getCode(), response.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });
    }

    public void favorArticle(Article article, ArticleLoader<Article> callback) {
        FavoriteArticleRequest request = new FavoriteArticleRequest(article.getArticleId());
        request.setToken(ContentManager.getInstance().getLoginToken());
        request.doSign();

        OkGoUtils.getInstance().postData(
                new IBaseView() {
                    @Override
                    public void showLoading() {
                    }

                    @Override
                    public void hideLoading() {
                    }
                },
                RemoteUrl.getAddStoreUrl(),
                GsonUtil.GsonString(request), BaseResponse.class,
                new OkGoUtils.ICallback<BaseResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (callback != null) {
                            callback.invoke(code, null);
                        }
                    }

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getCode() == 200) {
                            article.setStore(1);
                            callback.invoke(0, article);
                        }
                        else {
                            onFailure(response.getCode(), response.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });
    }


    public void unfavorArticle(Article article, ArticleLoader<Article> callback) {
        FavoriteArticleRequest.UnfavorRequest request = new FavoriteArticleRequest.UnfavorRequest(article.getArticleId());
        request.setToken(ContentManager.getInstance().getLoginToken());
        request.doSign();

        OkGoUtils.getInstance().postData(
                new IBaseView() {
                    @Override
                    public void showLoading() {
                    }

                    @Override
                    public void hideLoading() {
                    }
                },
                RemoteUrl.getDelStoreUrl(),
                GsonUtil.GsonString(request), BaseResponse.class,
                new OkGoUtils.ICallback<BaseResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (callback != null) {
                            callback.invoke(code, null);
                        }
                    }

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getCode() == 200) {
                            article.setStore(0);
                            callback.invoke(0, article);
                        }
                        else {
                            onFailure(response.getCode(), response.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });
    }

    public void readArticle(Article article, ArticleLoader<Article> callback) {
        ArticleReadRequest request = new ArticleReadRequest();
        request.setArticleId(article.getArticleId());
        request.setBroadcastId(article.getBroadcastId());
        request.setProgress(article.getProgress());
        request.setRead(article.getProgress() == 1? 1 : 0);
        request.setToken(ContentManager.getInstance().getLoginToken());
        request.doSign();

        OkGoUtils.getInstance().postData(
                new IBaseView() {
                    @Override
                    public void showLoading() {
                    }

                    @Override
                    public void hideLoading() {
                    }
                },
                RemoteUrl.getArticleReadedUrl(),
                GsonUtil.GsonString(request), BaseResponse.class,
                new OkGoUtils.ICallback<BaseResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (callback != null) {
                            callback.invoke(code, null);
                        }
                    }

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getCode() == 200) {
                            callback.invoke(0, article);
                        }
                        else {
                            onFailure(response.getCode(), response.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });
    }

    public void loadArticleAndList(Article article, boolean needSoundPlay, boolean isFirst, boolean isLast, ArticleLoader<ArticleListArgument> callback) {
        final long tickCountAtTime = ++tickcount;
        currentArticle = article;
        if (needSoundPlay) {
            soundEffector.playSwitch(null);
        }

        ArticleInfoRequest request = new ArticleInfoRequest();
        request.setArticleId(article.getArticleId());
        request.setBroadcastId(article.getBroadcastId());
        request.setToken(ContentManager.getInstance().getLoginToken());
        request.doSign();

        ArticleListArgument articleListArgument = new ArticleListArgument();
        articleListArgument.article = article;

        OkGoUtils.getInstance().postData(
                new IBaseView() {
                    @Override
                    public void showLoading() {
                    }

                    @Override
                    public void hideLoading() {
                    }
                },
                RemoteUrl.getArticleInfoUrl(),
                GsonUtil.GsonString(request), ArticleInfoResponse.class,
                new OkGoUtils.ICallback<ArticleInfoResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {
                        if (callback != null) {
                            callback.invoke(code, null);
                        }
                    }

                    @Override
                    public void onSuccess(ArticleInfoResponse response) {
                        if (response.getCode() == 200) {
                            Article responseArt = response.getData();
                            article.setContent(responseArt.getContent());
                            article.setTitle(responseArt.getTitle());
                            article.setUserId(responseArt.getUserId());
                            article.setNickName(responseArt.getNickName());
                            article.setSourceName(responseArt.getSourceName());
                            article.setRead(responseArt.getRead());
                            article.setShareUrl(responseArt.getShareUrl());
                            article.setStore(responseArt.getStore());
                            article.setProgress(responseArt.getProgress());

                            ArticleListArgument argumentInner = articleListArgument;

                            if (isFirst || isLast || "-1".equals(article.getBroadcastId()) == false) {
                                getSpeechListNearBy(article, (isFirst) ? "old" : "new", new ArticleLoader<List<Article>>() {
                                    @Override
                                    public void invoke(int errorCode, List<Article> data) {
                                        if (errorCode == 0) {
                                            argumentInner.list = data;
                                        }
                                        //不管列表成功失败，都返回0;
                                        callback.invoke(0, argumentInner);
                                    }
                                });
                            }
                            else {
                                callback.invoke(0, articleListArgument);
                            }
                        }
                        else {
                            onFailure(response.getCode(), response.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });

    }
}