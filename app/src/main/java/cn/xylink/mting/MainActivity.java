package cn.xylink.mting;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.SpeechServiceProxy;
import cn.xylink.mting.ui.activity.BasePresenterActivity;
import cn.xylink.mting.ui.adapter.MainFragmentAdapter;
import cn.xylink.mting.utils.L;

public class MainActivity extends BasePresenterActivity implements ViewPager.OnPageChangeListener {

    public static String SHARE_URL = "share_url";
    public static String SHARE_SUCCESS = "share_success";
    public static String ARTICLE_ID = "article_id";
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

    @Override
    protected void onNewIntent(Intent intent) {
        L.v("*^*^*^*^*" + intent.getIntExtra(SHARE_SUCCESS, -1));
        showShareResultDialog(intent.getIntExtra(SHARE_SUCCESS, -1), getIntent().getStringExtra(SHARE_URL));
//        String articleID = getIntent().getStringExtra(ARTICLE_ID);
//        if (!TextUtils.isEmpty(articleID)) {
//            Bundle bundle = new Bundle();
//            bundle.putString("aid", articleID);
//            this.jumpActivity(ArticleDetailActivity.class, bundle);
//        }
        super.onNewIntent(intent);
    }


    @Override
    protected void onSpeechServiceAvailable() {
        super.onSpeechServiceAvailable();
        getSpeechService().loadAndPlay("2019102118414971152446751", "2019102211541422454428823");
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
                    mViewPager.setCurrentItem(0, true);
                    break;
                case R.id.rb_tab_world:
                    mViewPager.setCurrentItem(1, true);
                    break;
                case R.id.rb_tab_my:
                    mViewPager.setCurrentItem(2, true);
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
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                mTingButton.setChecked(true);
                break;
            case 1:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                mWorldButton.setChecked(true);
                break;
            case 2:
                mMyButton.setChecked(true);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
