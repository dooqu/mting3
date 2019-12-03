package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import cn.xylink.mting.contract.AddStoreContact;
import cn.xylink.mting.contract.ArticleDetailContract;
import cn.xylink.mting.contract.DelStoreContact;
import cn.xylink.mting.event.ArticleDetailScrollEvent;
import cn.xylink.mting.event.TingRefreshEvent;
import cn.xylink.mting.presenter.AddStorePresenter;
import cn.xylink.mting.presenter.ArticleDetailPresenter;
import cn.xylink.mting.presenter.DelStorePreesenter;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.L;

/**
 * @author wjn
 * @date 2019/11/28
 */
public class ArticleDetailActivity extends BasePresenterActivity implements ArticleDetailContract.IArticleDetailView, AddStoreContact.IAddStoreView, DelStoreContact.IDelStoreView {
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

    private ArticleDetailPresenter mArticleDetailPresenter;
    //    public static String USER_ID_DETAIL = "USER_ID_DETAIL";
    public static String BROADCAST_ID_DETAIL = "BROADCAST_ID_DETAIL";
    public static String ARTICLE_ID_DETAIL = "ARTICLE_ID_DETAIL";

    private String broadcastId;//用来查询播放进度。待读传-1，已读历史传-2，收藏传-3，我创建的传-4。
    private String articleId;//文章id
    private String userId;
    private boolean isFavor;
    private AddStorePresenter mAddStorePresenter;
    private DelStorePreesenter mDelStorePresenter;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_article_detail);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        broadcastId = intent.getStringExtra(BROADCAST_ID_DETAIL);
        articleId = intent.getStringExtra(ARTICLE_ID_DETAIL);
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
                if (scrollY > oldScrollY) {
                    L.v("下滑......");
                    EventBus.getDefault().post(new ArticleDetailScrollEvent("glide"));
                }
                if (scrollY < oldScrollY) {
                    L.v("上滑......");
                    EventBus.getDefault().post(new ArticleDetailScrollEvent("upGlide"));
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void doGetArticleDetail() {
        ArticleDetailRequest request = new ArticleDetailRequest();
        request.setArticleId(articleId);
        if (!TextUtils.isEmpty(broadcastId)) {
            request.setBroadcastId(broadcastId);
        }
        request.doSign();
        mArticleDetailPresenter.createArticleDetail(request);
        showLoading();

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
        tvArticleTitle.setText(info.getTitle());
        tvArticleContent.setText(info.getContent());
        userId = info.getUserId();
        //articleId应该跟其他activity接收过来的是一致的，不然就有问题，哈哈~
        this.articleId = info.getArticleId();
        //由此判断该文章是否是用户自己创建的,如果获取的文章的用户id跟userInfo中的一致，则表明该文章是自己创建的
        if (ContentManager.getInstance().getUserInfo().getUserId().equals(userId)) {
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
}
