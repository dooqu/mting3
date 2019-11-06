package cn.xylink.mting;


import android.support.v4.view.ViewPager;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.SpeechServiceProxy;
import cn.xylink.mting.speech.Speechor;
import cn.xylink.mting.ui.activity.BasePresenterActivity;
import cn.xylink.mting.ui.adapter.MainFragmentAdapter;
import cn.xylink.mting.utils.L;

public class MainActivity extends BasePresenterActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_main)
    ViewPager mViewPager;
    @BindView(R.id.rb_tab_ting)
    RadioButton mTingButton;
    @BindView(R.id.rb_tab_world)
    RadioButton mWorldButton;
    @BindView(R.id.rb_tab_my)
    RadioButton mMyButton;
    private MainFragmentAdapter mTabAdapter;
    SpeechServiceProxy proxy;

    @Override
    protected void initView() {
        proxy = new SpeechServiceProxy(this) {
            @Override
            protected void onConnected(boolean connected, SpeechService service) {
                if (connected) {
//                    service.setRole(Speechor.SpeechorRole.XiaoIce);
//                    onSpeechServiceReady(service);
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

    protected void onSpeechServiceReady(SpeechService service) {
        service.loadAndPlay("2019102118414971152446751", "2019102211541422454428823");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (proxy != null) {
            proxy.unbind();
        }
    }

    @Override
    protected void preView() {
        setContentView(R.layout.activity_main);
    }

    @OnCheckedChanged({R.id.rb_tab_ting, R.id.rb_tab_world, R.id.rb_tab_my})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        L.v(isChecked);
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_tab_ting:
                    mViewPager.setCurrentItem(0, false);
                    break;
                case R.id.rb_tab_world:
                    mViewPager.setCurrentItem(1, false);
                    break;
                case R.id.rb_tab_my:
                    mViewPager.setCurrentItem(2, false);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
            switch (i) {
                case 0:
                    mTingButton.setChecked(true);
                    break;
                case 1:
                    mWorldButton.setChecked(true);
                    break;
                case 2:
                    mMyButton.setChecked(true);
                    break;
            }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
