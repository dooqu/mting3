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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.AddStoreRequest;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.bean.ArticleDetail2Info;
import cn.xylink.mting.bean.ArticleDetailRequest;
import cn.xylink.mting.bean.ArticleIdsRequest;
import cn.xylink.mting.bean.ArticleReportRequest;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastIdRequest;
import cn.xylink.mting.bean.BroadcastItemAddInfo;
import cn.xylink.mting.bean.BroadcastItemAddRequest;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.contract.AddStoreContact;
import cn.xylink.mting.contract.ArticleDetailContract;
import cn.xylink.mting.contract.ArticleReportContact;
import cn.xylink.mting.contract.BroadcastDetailContact;
import cn.xylink.mting.contract.BroadcastItemAddContact;
import cn.xylink.mting.contract.DelStoreContact;
import cn.xylink.mting.event.ArticleDetailScrollEvent;
import cn.xylink.mting.event.StoreRefreshEvent;
import cn.xylink.mting.presenter.AddStorePresenter;
import cn.xylink.mting.presenter.ArticleDetailPresenter;
import cn.xylink.mting.presenter.ArticleReportPresenter;
import cn.xylink.mting.presenter.BroadcastDetailPresenter;
import cn.xylink.mting.presenter.BroadcastItemAddPresenter;
import cn.xylink.mting.presenter.DelStorePreesenter;
import cn.xylink.mting.speech.SpeechServiceProxy;
import cn.xylink.mting.speech.SpeechSettingService;
import cn.xylink.mting.speech.Speechor;
import cn.xylink.mting.ui.dialog.ArticleDetailShareDialog;
import cn.xylink.mting.ui.dialog.BottomArticleReportDialog;
import cn.xylink.mting.ui.dialog.BottomTingDialog;
import cn.xylink.mting.ui.dialog.BottomTingItemModle;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.L;

/**
 * @author wjn
 * @date 2019/11/28
 */
public class ArticleDetailActivity extends BasePresenterActivity implements ArticleDetailContract.IArticleDetailView, AddStoreContact.IAddStoreView, DelStoreContact.IDelStoreView, ArticleReportContact.IDelStoreView, BottomTingDialog.OnBottomTingListener, BroadcastDetailContact.IBroadcastDetailView, BroadcastItemAddContact.IBroadcastItemAddView {
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
    @BindView(R.id.rv_broadcast_detail)
    RelativeLayout rvBroadcastDetail;
    @BindView(R.id.imv_broadcast_detail)
    ImageView imgBroadcast;
    @BindView(R.id.tv_broadcast_title)
    TextView tvBroadcastTitle;
    @BindView(R.id.tv_broadcast_author)
    TextView tvBroadcastAuthor;


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
    private BroadcastDetailPresenter mBroadcastDetailPresenter;
    private BroadcastItemAddPresenter mBroadcastItemAddPresenter;
    private ArticleReportPresenter mArticleReportPresenter;
    private BottomTingDialog mBottomTingDialog;
    private int inType;
    private SpeechServiceProxy proxy;
    private SpeechSettingService service;
    private BottomArticleReportDialog mReportDialog;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_article_detail);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mBroadcastDetailPresenter = (BroadcastDetailPresenter) createPresenter(BroadcastDetailPresenter.class);
        mBroadcastDetailPresenter.attachView(this);
        mArticleDetailPresenter = (ArticleDetailPresenter) createPresenter(ArticleDetailPresenter.class);
        mArticleDetailPresenter.attachView(this);
        mAddStorePresenter = (AddStorePresenter) createPresenter(AddStorePresenter.class);
        mAddStorePresenter.attachView(this);
        mDelStorePresenter = (DelStorePreesenter) createPresenter(DelStorePreesenter.class);
        mDelStorePresenter.attachView(this);
        mBroadcastItemAddPresenter = (BroadcastItemAddPresenter) createPresenter(BroadcastItemAddPresenter.class);
        mBroadcastItemAddPresenter.attachView(this);
        mArticleReportPresenter = (ArticleReportPresenter) createPresenter(ArticleReportPresenter.class);
        mArticleReportPresenter.attachView(this);
        Intent intent = getIntent();
        broadcastId = intent.getStringExtra(BROADCAST_ID_DETAIL);
        articleId = intent.getStringExtra(ARTICLE_ID_DETAIL);
        broadcastTitle = intent.getStringExtra(BROADCAST_TITLE_DETAIL);
        //显示栏 显示的条件: 有broadcastId&&不是-1234
        if (broadcastId != null) {
            if (!broadcastId.equals("-1") && !broadcastId.equals("-2") && !broadcastId.equals("-3") && !broadcastId.equals("-4")) {
                rvBroadcastDetail.setVisibility(View.VISIBLE);
                doGetBroadcastDetail(broadcastId);
            }
        } else rvBroadcastDetail.setVisibility(View.GONE);

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


    @OnClick({R.id.btn_left, R.id.btn_share, R.id.btn_more, R.id.btn_edit, R.id.view_detail_panel_favor, R.id.view_detail_panel_play, R.id.view_detail_panel_add_to, R.id.btn_broadcast_enter})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_share:
                if (articleDetail2Info != null) {
                    ArticleDetailShareDialog dialog = new ArticleDetailShareDialog(this);
                    dialog.setDetailInfo(articleDetail2Info);
                    dialog.show();
                }
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
                mBottomTingDialog.show();
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
                //播放器播放， 如果有broadcastId,就跟之前一样，没有，就添加待读，然后broadcastid=-1传进去
                if (null != broadcastId) {
                    Article article = new Article();
                    article.setBroadcastId(broadcastId);
                    article.setArticleId(articleId);
                    article.setBroadcastTitle(broadcastTitle);
                    article.setTitle(articleTitle);
                    postToSpeechService(article);
                } else {
                    doAdd2Unread();
                }

                break;
            case R.id.view_detail_panel_add_to:
                Intent intent2 = new Intent(this, BroadcastItemAddActivity.class);
                intent2.putExtra(BroadcastItemAddActivity.ARTICLE_IDS_EXTRA, articleId);
                startActivity(intent2);
                this.finish();
                break;
            case R.id.btn_broadcast_enter:
                Intent intent3 = new Intent(ArticleDetailActivity.this, BroadcastActivity.class);
                intent3.putExtra(BroadcastActivity.EXTRA_BROADCASTID, broadcastId);
                intent3.putExtra(BroadcastActivity.EXTRA_TITLE, broadcastTitle);
                ArticleDetailActivity.this.startActivity(intent3);
                ArticleDetailActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private ArticleDetail2Info articleDetail2Info;

    @Override
    public void onSuccessArticleDetail(ArticleDetail2Info info) {
        articleDetail2Info = info;
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
        StoreRefreshEvent event = new StoreRefreshEvent();
        event.setArticleID(articleId);
        event.setStroe(1);
        EventBus.getDefault().post(event);

    }

    @Override
    public void onDelStoreSuccess(BaseResponse response) {
        StoreRefreshEvent event = new StoreRefreshEvent();
        event.setArticleID(articleId);
        event.setStroe(0);
        EventBus.getDefault().post(event);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStoreState(StoreRefreshEvent storeRefreshEvent) {
        int storeState = storeRefreshEvent.getStroe();
        switch (storeState) {
            case 0:
                isFavor = false;
                icoFavor.setImageResource(R.mipmap.ico_dialog_unfavor);
                break;
            case 1:
                isFavor = true;
                icoFavor.setImageResource(R.mipmap.ico_dialog_favor);
                break;
        }
    }

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
        EventBus.getDefault().unregister(this);
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
                int sound = 0;
                service = getSpeechService();
                if (null != service) {
                    Speechor.SpeechorRole role = service.getRole();
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
                }
                bundle.putInt("sound", sound);
                float sp = 0;
                if (null != service) {
                    Speechor.SpeechorSpeed speed = service.getSpeed();
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
                }
                bundle.putFloat("speed", sp);
                jumpActivity(FeedBackActivity.class, bundle);
                TCAgent.onEvent(this, "articleDetails_feedback");
                break;
            case Const.BottomDialogItem.REPORT:
                showReportDialog();
                break;
        }
    }

    private void showReportDialog() {
        BottomArticleReportDialog dialog = new BottomArticleReportDialog(this);
        dialog.onClickListener(new BottomArticleReportDialog.OnBottomSelectDialogListener() {
            @Override
            public void onFirstClick() {
                dialog.dismiss();
                doArticleReport("低俗色情", "");
            }

            @Override
            public void onSecondClick() {
                dialog.dismiss();
                doArticleReport("涉嫌违法犯罪", "");

            }

            @Override
            public void onThirdClick() {
                dialog.dismiss();
                doArticleReport("低俗色内容不实情", "");

            }

            @Override
            public void onCommitClick(String content) {
                dialog.dismiss();
                doArticleReport("", content);
            }
        });
        dialog.show();
    }

    private void doArticleReport(String type, String content) {
        ArticleReportRequest reportRequest = new ArticleReportRequest();
        reportRequest.setArticleId(articleId);
        reportRequest.setContent(content);
        reportRequest.setType(type);
        reportRequest.doSign();
        mArticleReportPresenter.getArticleReport(reportRequest);
    }

    private void doGetBroadcastDetail(String broadcastId) {
        showLoading();
        BroadcastIdRequest request = new BroadcastIdRequest();
        request.setBroadcastId(broadcastId);
        request.doSign();
        mBroadcastDetailPresenter.getBroadcastDetail(request);
    }

    @Override
    public void onBroadcastDetailSuccess(BroadcastDetailInfo data) {
        hideLoading();
        String picture = data.getPicture();
        String name = data.getName();
        String createName = data.getCreateName();
        if (TextUtils.isEmpty(picture)) {
            imgBroadcast.setImageResource(R.mipmap.cjbd_img_fm_default);
        } else {
            ImageUtils.get().load(imgBroadcast, picture);
        }
        tvBroadcastTitle.setText(name);
        tvBroadcastAuthor.setText(createName);

    }

    @Override
    public void onBroadcastDetailError(int code, String errorMsg) {
        //如果获取播单详情失败 就不显示播单这块儿的布局
        rvBroadcastDetail.setVisibility(View.GONE);

    }

    private void doAdd2Unread() {
        showLoading();
        BroadcastItemAddRequest request = new BroadcastItemAddRequest();
        request.setBroadcastId("-1");
        request.setArticleIds(articleId);
        request.doSign();
        mBroadcastItemAddPresenter.getBroadcastItemAdd(request);
    }

    @Override
    public void onBroadcastItemAddListSuccess(List<BroadcastItemAddInfo> data) {

    }

    @Override
    public void onBroadcastItemAddListError(int code, String errorMsg) {

    }

    @Override
    public void onBroadcastItemAddSuccess(BaseResponse<String> baseResponse) {
        hideLoading();
        Article article = new Article();
        article.setBroadcastId(broadcastId);
        article.setArticleId(articleId);
        article.setBroadcastTitle(broadcastTitle);
        article.setTitle(articleTitle);
        postToSpeechService(article);

    }

    @Override
    public void onBroadcastItemAddError(int code, String errorMsg) {

    }

    @Override
    public void onArticleReportSuccess(BaseResponse response) {

    }

    @Override
    public void onArticleReportError(int code, String errorMsg) {

    }
}
