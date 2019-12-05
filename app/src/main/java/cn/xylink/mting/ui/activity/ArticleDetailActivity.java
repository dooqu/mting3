package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tendcloud.tenddata.TCAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.AddStoreRequest;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.bean.ArticleDetail2Info;
import cn.xylink.mting.bean.ArticleDetailRequest;
import cn.xylink.mting.bean.ArticleIdsRequest;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.contract.AddStoreContact;
import cn.xylink.mting.contract.ArticleDetailContract;
import cn.xylink.mting.contract.DelStoreContact;
import cn.xylink.mting.event.ArticleDetailScrollEvent;
import cn.xylink.mting.presenter.AddStorePresenter;
import cn.xylink.mting.presenter.ArticleDetailPresenter;
import cn.xylink.mting.presenter.DelStorePreesenter;
import cn.xylink.mting.speech.SpeechService;
import cn.xylink.mting.speech.Speechor;
import cn.xylink.mting.ui.dialog.BottomTingDialog;
import cn.xylink.mting.ui.dialog.BottomTingItemModle;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.L;

/**
 * @author wjn
 * @date 2019/11/28
 */
public class ArticleDetailActivity extends BasePresenterActivity implements ArticleDetailContract.IArticleDetailView, AddStoreContact.IAddStoreView, DelStoreContact.IDelStoreView, BottomTingDialog.OnBottomTingListener {
    @BindView(R.id.btn_edit)
    ImageButton btnEdit;
    @BindView(R.id.tv_article_title)
    TextView tvArticleTitle;
    @BindView(R.id.tv_article_source)
    TextView tvArticleSource;
    @BindView(R.id.tv_article_content)
    TextView tvArticleContent;
    @BindView(R.id.ico_favor)
    ImageView icoFavor;
    @BindView(R.id.img_dialog_panel_play)
    ImageView icoPlay;
    @BindView(R.id.ico_detail_addto)
    ImageView icoAddTo;
    @BindView(R.id.scrollView_detail)
    NestedScrollView scrollView;
    @BindView(R.id.rv_play)
    RelativeLayout rvPlay;

    private ArticleDetailPresenter mArticleDetailPresenter;
    public static String BROADCAST_TITLE_DETAIL = "BROADCAST_TITLE_DETAIL";
    public static String BROADCAST_ID_DETAIL = "BROADCAST_ID_DETAIL";
    public static String ARTICLE_ID_DETAIL = "ARTICLE_ID_DETAIL";

    private String broadcastId;//用来查询播放进度。待读传-1，已读历史传-2，收藏传-3，我创建的传-4。
    private String articleId;//文章id
    private String userId;
    private String articleTitle;
    private String broadcastTitle;
    private boolean isFavor;
    private AddStorePresenter mAddStorePresenter;
    private DelStorePreesenter mDelStorePresenter;
    private BottomTingDialog mBottomTingDialog;
    private int inType;
    private SpeechService service;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_article_detail);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        broadcastId = intent.getStringExtra(BROADCAST_ID_DETAIL);
        articleId = intent.getStringExtra(ARTICLE_ID_DETAIL);
        broadcastTitle = intent.getStringExtra(BROADCAST_TITLE_DETAIL);
        mArticleDetailPresenter = (ArticleDetailPresenter) createPresenter(ArticleDetailPresenter.class);
        mArticleDetailPresenter.attachView(this);
        mAddStorePresenter = (AddStorePresenter) createPresenter(AddStorePresenter.class);
        mAddStorePresenter.attachView(this);
        mDelStorePresenter = (DelStorePreesenter) createPresenter(DelStorePreesenter.class);
        mDelStorePresenter.attachView(this);
        doGetArticleDetail();
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) {
                    L.v("手指上滑......");
                    EventBus.getDefault().post(new ArticleDetailScrollEvent("glide"));
                }
                if (scrollY > oldScrollY) {
                    L.v("手指下滑......");
                    EventBus.getDefault().post(new ArticleDetailScrollEvent("upGlide"));
                }
            }
        });
        mBottomTingDialog = new BottomTingDialog(this, this);
    }

    @Override
    protected void initData() {

    }

    private void doGetArticleDetail() {
        showLoading();
        ArticleDetailRequest request = new ArticleDetailRequest();
        request.setArticleId(articleId);
        if (!TextUtils.isEmpty(broadcastId)) {
            request.setBroadcastId(broadcastId);
        }
        request.doSign();
        mArticleDetailPresenter.createArticleDetail(request);
    }

    @Override
    protected void initTitleBar() {

    }


    @OnClick({R.id.btn_left, R.id.btn_share, R.id.btn_more, R.id.btn_edit, R.id.view_detail_panel_favor, R.id.view_detail_panel_play, R.id.view_detail_panel_add_to})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_share:

                break;
            case R.id.btn_more:
                if (inType == 1) {
                    if (ContentManager.getInstance().getUserInfo().getUserId().equals(userId)) {//平台内自己创建
                        mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.FEEDBACK, getResources().getDrawable(R.mipmap.icon_feedback_bar)));
                    } else {
                        mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.FEEDBACK, getResources().getDrawable(R.mipmap.icon_feedback_bar)),
                                new BottomTingItemModle(Const.BottomDialogItem.REPORT, getResources().getDrawable(R.mipmap.icon_report_bar)));
                    }
                } else {
                    mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.BROWER, getResources().getDrawable(R.mipmap.icon_browser_bar)),
                            new BottomTingItemModle(Const.BottomDialogItem.FEEDBACK, getResources().getDrawable(R.mipmap.icon_feedback_bar)),
                            new BottomTingItemModle(Const.BottomDialogItem.REPORT, getResources().getDrawable(R.mipmap.icon_report_bar)));
                }
                break;
            case R.id.btn_edit:
                Intent intent = new Intent(ArticleDetailActivity.this, ArticleEditActivity.class);
                intent.putExtra(ArticleEditActivity.ARTICLE_ID_EDIT, articleId);
                intent.putExtra(ArticleEditActivity.ARTICLE_TITLE_EDIT, tvArticleTitle.getText().toString());
                intent.putExtra(ArticleEditActivity.ARTICLE_CONTENT_EDIT, tvArticleContent.getText().toString());
                startActivity(intent);
                ArticleDetailActivity.this.finish();
                break;
            case R.id.view_detail_panel_favor:
                isFavor = !isFavor;
                if (isFavor) {
                    icoFavor.setImageResource(R.mipmap.ico_dialog_favor);
                    addStore(articleId);
                } else {
                    icoFavor.setImageResource(R.mipmap.ico_dialog_unfavor);
                    delStore(articleId);
                }
                break;
            case R.id.view_detail_panel_play:
                Article article = new Article();
                article.setBroadcastId(broadcastId);
                article.setArticleId(articleId);
                article.setBroadcastTitle(broadcastTitle);
                article.setTitle(articleTitle);
                postToSpeechService(article);
                break;
            case R.id.view_detail_panel_add_to:
                Intent intent2 = new Intent(this, BroadcastItemAddActivity.class);
                intent2.putExtra(BroadcastItemAddActivity.ARTICLE_IDS_EXTRA, articleId);
                startActivity(intent2);
                this.finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onSuccessArticleDetail(ArticleDetail2Info info) {
        initFontSize();
        rvPlay.setVisibility(View.VISIBLE);
        tvArticleTitle.setText(info.getTitle());
        tvArticleContent.setText(info.getContent());
        userId = info.getUserId();
        articleTitle = info.getTitle();
        //1手动创建,2录入链接创建,3分享链接创建
        inType = info.getInType();
        //articleId应该跟其他activity接收过来的是一致的，不然就有问题，哈哈~
        this.articleId = info.getArticleId();
        //由此判断该文章是否是用户自己创建的,如果获取的文章的用户id跟userInfo中的一致，则表明该文章是自己创建的
        if (ContentManager.getInstance().getUserInfo().getUserId().equals(userId) && inType == 1) {
            btnEdit.setVisibility(View.VISIBLE);
        } else {
            btnEdit.setVisibility(View.GONE);
        }
        if (info.getStore() == 1) {//0未收藏,1已收藏
            isFavor = true;
            icoFavor.setImageResource(R.mipmap.ico_dialog_favor);
        } else {
            isFavor = false;
            icoFavor.setImageResource(R.mipmap.ico_dialog_unfavor);
        }
        if (TextUtils.isEmpty(String.valueOf(info.getSourceName())) || String.valueOf(info.getSourceName()).equals("null")) {
            tvArticleSource.setText("");
            tvArticleSource.setVisibility(View.GONE);
        } else {
            tvArticleSource.setText(String.valueOf(info.getSourceName()));
        }
        hideLoading();
    }

    @Override
    public void onErrorArticleDetail(int code, String errorMsg) {

    }

    @Override
    protected boolean enableSpeechService() {
        return true;
    }

    @Override
    public void onAddStoreSuccess(BaseResponse response) {

    }

    @Override
    public void onAddStoreError(int code, String errorMsg) {

    }

    @Override
    public void onDelStoreSuccess(BaseResponse response) {

    }

    @Override
    public void onDelStoreError(int code, String errorMsg) {

    }

    private void addStore(String articleId) {
        AddStoreRequest request = new AddStoreRequest();
        request.setArticleId(articleId);
        request.doSign();
        mAddStorePresenter.addStore(request);
    }

    private void delStore(String articleId) {
        ArticleIdsRequest request = new ArticleIdsRequest();
        request.setArticleIds(articleId);
        request.doSign();
        mDelStorePresenter.delStore(request);
    }

  /*  @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeFontSize(ArticleFontSizeEvent fontSizeEvent) {
        String fontSize = fontSizeEvent.getFontSize();
        switch (fontSize) {
            case "default":
                tvArticleTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                tvArticleSource.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
            case "middle":
                tvArticleTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
                tvArticleSource.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                break;
            case "big":
                tvArticleTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                tvArticleSource.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                break;
        }
//        EventBus.getDefault().post(new ArticleFontSizeEvent("default"));//默认字号
//        EventBus.getDefault().post(new ArticleFontSizeEvent("middle"));//中字号
//        EventBus.getDefault().post(new ArticleFontSizeEvent("big"));//大字号
    }*/

    private void initFontSize() {
        int size = ContentManager.getInstance().getTextSize();
        switch (size) {
            case 0:
                tvArticleTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                tvArticleSource.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
            case 1:
                tvArticleTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
                tvArticleSource.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                break;
            case 2:
                tvArticleTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                tvArticleSource.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvArticleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBottomTingItemClick(BottomTingItemModle modle) {
        switch (modle.getName()) {
            case Const.BottomDialogItem.BROWER:

                break;
            case Const.BottomDialogItem.FEEDBACK:
                //点击反馈按钮，传参数到反馈页面
                Bundle bundle = new Bundle();
                bundle.putString("type", "detail");
                bundle.putString("aid", articleId);
                Speechor.SpeechorRole role = service.getRole();
                int sound = 0;
                switch (role) {
                    case XiaoIce:
                        sound = 1;
                        break;
                    case XiaoMei:
                        sound = 2;
                        break;
                    case XiaoYao:
                        sound = 3;
                        break;
                    case XiaoYu:
                        sound = 4;
                        break;
                }
                bundle.putInt("sound", sound);
                Speechor.SpeechorSpeed speed = service.getSpeed();
                float sp = 0;
                switch (speed) {
                    case SPEECH_SPEED_HALF:
                        sp = 0.5f;
                        break;
                    case SPEECH_SPEED_NORMAL:
                        sp = 1f;
                        break;
                    case SPEECH_SPEED_MULTIPLE_1_POINT_5:
                        sp = 1.5f;
                        break;
                    case SPEECH_SPEED_MULTIPLE_2:
                        sp = 2f;
                        break;
                }
                bundle.putFloat("speed", sp);
                jumpActivity(FeedBackActivity.class, bundle);
                TCAgent.onEvent(this, "articleDetails_feedback");
                break;
            case Const.BottomDialogItem.REPORT:

                break;
        }
    }
}
