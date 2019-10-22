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
import cn.xylink.mting.speech.data.ArticleDataProvider;
import cn.xylink.mting.speech.data.ArticleDataProviderFake;

public class MainActivity extends AppCompatActivity {

    SpeechServiceProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proxy = new SpeechServiceProxy(this) {
            @Override
            protected void onConnected(boolean connected, SpeechService service) {
                if(connected) {
                    service.setRole(Speechor.SpeechorRole.XiaoIce);
                    onSpeechServiceReady(service);
                }
            }
        };
        proxy.bind();
    }


    protected  void onSpeechServiceReady(SpeechService service) {
        service.loadAndPlay("2019102118414971152446751", "2019102211541422454428823");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(proxy != null) {
            proxy.unbind();
        }
    }
}
