package cn.xylink.mting;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.contract.VisitorSyncDataContact;
import cn.xylink.mting.model.data.VisitorSyncDataRequest;
import cn.xylink.mting.presenter.GetCodePresenter;
import cn.xylink.mting.presenter.VisitorSyncDataPresenter;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.SpeechServiceProxy;
import cn.xylink.mting.ui.activity.BasePresenterActivity;
import cn.xylink.mting.ui.adapter.MainFragmentAdapter;
import cn.xylink.mting.ui.dialog.SubscribeTipDialog;
import cn.xylink.mting.ui.dialog.VisitorSyncDataDialog;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.L;

public class MainActivity extends BasePresenterActivity implements ViewPager.OnPageChangeListener, VisitorSyncDataContact.IVisitorSyncDataView, VisitorSyncDataDialog.OnTipListener {

    public static String SHARE_URL = "share_url";
    public static String SHARE_SUCCESS = "share_success";
    public static String ARTICLE_ID = "article_id";
    public static String IS_NEW_USER = "is_new_user";
    @BindView(R.id.vp_main)
    ViewPager mViewPager;
    @BindView(R.id.rb_tab_ting)
    RadioButton mTingButton;
    @BindView(R.id.rb_tab_world)
    RadioButton mWorldButton;
    @BindView(R.id.rb_tab_my)
    RadioButton mMyButton;
    private MainFragmentAdapter mTabAdapter;
    private VisitorSyncDataDialog mSyncDataDialog;
    private VisitorSyncDataPresenter mSyncDataPresenter;

    @Override
    protected void initView() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void initData() {
        mTabAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mSyncDataPresenter = (VisitorSyncDataPresenter) createPresenter(VisitorSyncDataPresenter.class);
        mSyncDataPresenter.attachView(this);
        Intent intent = getIntent();
        int newUser = intent.getIntExtra(IS_NEW_USER, 0);//0-否，1-是
        //如游客登录过并且不是新用户才手动同步数据
        if (newUser == 0&&!("".equals(ContentManager.getInstance().getVisitorToken())) ) {
            mSyncDataDialog = new VisitorSyncDataDialog(this);
            mSyncDataDialog.setMsg("是否需要同步本地数据，包括：待读、已读等", "", this);
            mSyncDataDialog.show();
        } else if (newUser == 1) {
            //新用户不用调接口同步数据
        } else {

        }
    }

    private void doVisitorSyncData() {
        VisitorSyncDataRequest request = new VisitorSyncDataRequest();
        request.setToken(ContentManager.getInstance().getLoginToken());
        request.setVstToken(ContentManager.getInstance().getVisitorToken());
        request.doSign();
        mSyncDataPresenter.onVisitorSyncData(request);
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
    protected boolean enableSpeechService() {
        return true;
    }

    @Override
    protected void onSpeechServiceAvailable() {
        super.onSpeechServiceAvailable();
        Article article = new Article();
        article.setBroadcastId("201911181652361841031964");
        article.setArticleId("2019111411493373493858111");
        //postToSpeechService(article);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    public void onVisitorSyncDataSuccess(BaseResponse<String> baseResponse) {
        L.v(baseResponse);
        ContentManager.getInstance().setVisitorToken("");
        ContentManager.getInstance().setVisitor("1");//表示不是游客登录
    }

    @Override
    public void onVisitorSyncDataError(int code, String errorMsg) {
        toastShort(errorMsg);
    }

    @Override
    public void onLeftClick(Object tag) {

    }

    @Override
    public void onRightClick(Object tag) {
        doVisitorSyncData();
    }
}
