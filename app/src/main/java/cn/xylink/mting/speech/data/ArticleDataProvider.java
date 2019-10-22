package cn.xylink.mting.speech.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

import cn.xylink.mting.bean.Article;
import cn.xylink.mting.contract.IBaseView;
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
        request.setCreateAt(createAt);
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
                        List<Article> list = response.getData();
                        for (Article article : list) {
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

    public void loadArticleAndList(Article article, boolean needSoundPlay, boolean isFirst, boolean isLast, ArticleLoader<ArticleListArgument> callback) {
        final long tickCountAtTime = ++tickcount;
        currentArticle = article;
        if (needSoundPlay) {
            soundEffector.playSwitch(null);
        }

        ArticleListArgument responseResult = new ArticleListArgument();
        responseResult.article = article;
        article.setContent("你好世界");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //模拟网络加载
                    Thread.sleep(1500);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                if (isFirst || isLast) {
                    getSpeechListNearBy(article.getBroadcastId(), article.getCreateAt(), (isFirst) ? "old" : "new", new ArticleLoader<List<Article>>() {
                        @Override
                        public void invoke(int errorCode, List<Article> data) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    responseResult.list = data;
                                    callback.invoke(0, responseResult);
                                }
                            });
                        }
                    });
                    return;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tickCountAtTime == tickcount && callback != null) {
                            callback.invoke(0, responseResult);
                        }
                        else {
                            Log.d("xylink", "取消");
                        }
                    }
                });
            }
        }).start();
    }
}