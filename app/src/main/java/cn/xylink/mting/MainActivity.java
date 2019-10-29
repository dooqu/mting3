package cn.xylink.mting;


import android.support.v4.view.ViewPager;

import butterknife.BindView;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.SpeechServiceProxy;
import cn.xylink.mting.speech.Speechor;
import cn.xylink.mting.ui.activity.BasePresenterActivity;
import cn.xylink.mting.ui.adapter.MainFragmentAdapter;

public class MainActivity extends BasePresenterActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.vp_main)
    ViewPager mViewPager;
    private MainFragmentAdapter mTabAdapter;
    SpeechServiceProxy proxy;

    @Override
    protected void initView() {
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

    @Override
    protected void initData() {
        mTabAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void initTitleBar() {

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

    @Override
    protected void preView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
