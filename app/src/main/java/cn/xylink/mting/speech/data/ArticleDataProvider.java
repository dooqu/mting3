package cn.xylink.mting.speech.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.contract.IBaseView;
import cn.xylink.mting.model.ArticleInfoRequest;
import cn.xylink.mting.model.ArticleInfoResponse;
import cn.xylink.mting.model.SpeechListRequest;
import cn.xylink.mting.model.SpeechListResponse;
import cn.xylink.mting.model.data.SpeechListNearByRequest;
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

    public void getSpeechListNearBy(String broadcastId, long createAt, String event, ArticleLoader<List<Article>> callback) {
        SpeechListNearByRequest request = new SpeechListNearByRequest();
        request.setLastAt(createAt);
        request.setBroadcastId(broadcastId);
        request.setEvent(event);
        request.setToken("1");
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
                        List<Article> list = response.getData();
                        for(Article article : list) {
                            article.setBroadcastId(broadcastId);
                        }
                        if (callback != null) {
                            callback.invoke(0, response.getData());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });
    }

    public void getSpeechList(String broadcastId, String articleId, ArticleLoader<List<Article>> callback) {
        SpeechListRequest request = new SpeechListRequest();
        request.setBroadcastId(broadcastId);
        request.setArticleId(articleId);
        request.setToken("1");
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

                        new Thread(()->{
                            try {
                                Thread.sleep(3000);

                                List<Article> list = response.getData();
                                for (Article article : list) {
                                    article.setBroadcastId(broadcastId);
                                }
                                if (callback != null) {
                                    callback.invoke(0, response.getData());
                                }
                            }
                            catch (Exception ex) {}
                        }).start();

                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });
    }


    public void getUnreadSpeechList(ArticleLoader<List<Article>> callback) {
        BaseRequest request = new BaseRequest();
        request.setToken("1");
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
                RemoteUrl.getUnreadListUrl(),
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
                        List<Article> list = response.getData();
                        for (Article article : list) {
                            article.setBroadcastId("-1");
                        }
                        if (callback != null) {
                            callback.invoke(0, response.getData());
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
        request.setToken("1");
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
                        Article responseArt = response.getData();
                        article.setContent(responseArt.getContent());
                        article.setTitle(responseArt.getTitle());
                        article.setUserId(responseArt.getUserId());
                        article.setNickName(responseArt.getNickName());
                        article.setSourceName(responseArt.getSourceName());
                        article.setRead(responseArt.getRead());
                        article.setShareUrl(responseArt.getShareUrl());
                        article.setStore(responseArt.getStore());

                        ArticleListArgument argumentInner = articleListArgument;

                        if (isFirst || isLast) {
                            getSpeechListNearBy(article.getBroadcastId(), article.getCreateAt(), (isFirst) ? "old" : "new", new ArticleLoader<List<Article>>() {
                                @Override
                                public void invoke(int errorCode, List<Article> data) {
                                    argumentInner.list = data;
                                    //不管列表成功失败，都返回0;
                                    callback.invoke(0, argumentInner);
                                }
                            });
                        }
                        else {
                            callback.invoke(0, articleListArgument);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xylink", "onComplete");
                    }
                });

    }
}