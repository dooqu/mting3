package cn.xylink.mting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.xylink.mting.bean.Article;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.SpeechServiceProxy;
import cn.xylink.mting.speech.Speechor;
import cn.xylink.mting.speech.data.ArticleDataProviderFake;

public class MainActivity extends AppCompatActivity {


    SpeechServiceProxy proxy;
    SpeechService speechService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        proxy = new SpeechServiceProxy(this) {
            @Override
            protected void onConnected(boolean connected, SpeechService service) {
                if(connected) {
                    speechService = service;
                    speechService.setRole(Speechor.SpeechorRole.XiaoIce);
                    onSpeechServiceReady();
                }
            }
        };
        proxy.bind();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(proxy != null) {
            proxy.unbind();
        }
    }

    protected  void onSpeechServiceReady() {
        List<Article> initList = new ArrayList<Article>();
        for(int i = 0; i < 5; i++) {
            initList.add(ArticleDataProviderFake.createArticle(new Article()));
        }
        speechService.resetSpeechList(initList, SpeechService.SpeechListType.Unread);


        speechService.play(initList.get(0).getArticleId());
    }
}
