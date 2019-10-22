package cn.xylink.mting.speech.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.xylink.mting.bean.Article;
import cn.xylink.mting.contract.IBaseView;
import cn.xylink.mting.model.ArticleInfoRequest;
import cn.xylink.mting.model.ArticleInfoResponse;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.speech.SoundEffector;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.GsonUtil;
import cn.xylink.mting.utils.OkGoUtils;

public class ArticleDataProviderFake {
    @FunctionalInterface
    public static interface ArticleLoaderCallback {
        void invoke(int errorCode, Article article);
    }


    @FunctionalInterface
    public static interface ArticleListLoaderCallback {
        void invoke(int errorCode, List<Article> list);
    }


    @FunctionalInterface
    public static interface ArticleContentAndListLoaderCallback {
        void invoke(int errorCode, Article article, List<Article> list);
    }



    SoundEffector soundEffector;
    Handler handler;
    public void release() {

    }

    public ArticleDataProviderFake(Context context) {
        soundEffector = new SoundEffector(context);
        handler = new Handler(Looper.getMainLooper());
    }

    static int articleId = 0;
    public static  Article createArticle(Article article) {
        article.setTitle("秋到了，秋有文" + articleId++);
        article.setArticleId(UUID.randomUUID().toString());
        article.setTextBody("秋的万盏灯火旖旎风景，天地的万里江河婆娑星月。\n" +
                "\n" +
                "秋到了，秋有文。\n" +
                "\n" +
                "有感的花开成情思楼，有想的叶落出心灵山，有思的风翻了世界，有云的波登上舟。\n" +
                "\n" +
                "浪掀意境着帆，虹带桥霜入境。\n" +
                "\n" +
                "来了客是品，去了赏是阅，横于时间的门窗，语生一枝垂钓，乐于空间的繁华港。\n" +
                "\n" +
                "行走在秋的小巷，看那把百态伞生别来无恙，望那闲地长出的桃源流动萤草香。");
        article.setContent("秋的万盏灯火旖旎风景，天地的万里江河婆娑星月。\n" +
                "\n" +
                "秋到了，秋有文。\n" +
                "\n" +
                "有感的花开成情思楼，有想的叶落出心灵山，有思的风翻了世界，有云的波登上舟。\n" +
                "\n" +
                "浪掀意境着帆，虹带桥霜入境。\n" +
                "\n" +
                "来了客是品，去了赏是阅，横于时间的门窗，语生一枝垂钓，乐于空间的繁华港。\n" +
                "\n" +
                "行走在秋的小巷，看那把百态伞生别来无恙，望那闲地长出的桃源流动萤草香。");
        return article;
    }

    public void loadArticleBeforeId(Article article, ArticleDataProviderFake.ArticleListLoaderCallback callback) {

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

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if ( callback != null) {

                            List<Article> list = new ArrayList<>();
                            for(int i = 0; i < 10; i++) {
                                list.add(createArticle(new Article()));
                            }
                            callback.invoke(0, list);

                        }
                        else {
                            Log.d("xylink", "取消");
                        }
                    }
                });
            }
        }).start();
    }


    public void loadArticleAfterId(Article article, ArticleDataProviderFake.ArticleListLoaderCallback callback) {

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

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if ( callback != null) {

                            List<Article> list = new ArrayList<>();
                            for(int i = 0; i < 10; i++) {
                                list.add(createArticle(new Article()));
                            }
                            callback.invoke(0, list);

                        }
                        else {
                            Log.d("xylink", "取消");
                        }
                    }
                });
            }
        }).start();
    }


    public void loadArticleContent(Article article, boolean needSoundPlay, ArticleDataProviderFake.ArticleLoaderCallback callback) {
        if (needSoundPlay) {
            soundEffector.playSwitch(null);
        }
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

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if ( callback != null) {
                            callback.invoke(0, createArticle(article));

                        }
                        else {
                            Log.d("xylink", "取消");
                        }
                    }
                });
            }
        }).start();
    }

    public void loadArticleContentAndList(Article article, boolean needSoundPlay, boolean isFirst, boolean isLast, ArticleDataProviderFake.ArticleContentAndListLoaderCallback callback) {
        if (needSoundPlay) {
            soundEffector.playSwitch(null);
        }
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

                if(isFirst || isLast) {
                    loadArticleAfterId(null, new ArticleListLoaderCallback() {
                        @Override
                        public void invoke(int errorCode, List<Article> list) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if ( callback != null) {
                                        callback.invoke(0, createArticle(article), list);
                                        return;

                                    }
                                    else {
                                        Log.d("xylink", "取消");
                                    }
                                }
                            });
                        }
                    });
                    return;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if ( callback != null) {
                            callback.invoke(0, createArticle(article), null);

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
