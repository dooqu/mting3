package cn.xylink.mting.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.ArticleDetail2Info;
import cn.xylink.mting.bean.ArticleDetailRequest;
import cn.xylink.mting.contract.ArticleDetailContract;
import cn.xylink.mting.presenter.ArticleDetailPresenter;
import cn.xylink.mting.utils.ContentManager;

/**
 * @author wjn
 * @date 2019/11/28
 */
public class ArticleDetailActivity extends BasePresenterActivity implements ArticleDetailContract.IArticleDetailView {
    @BindView(R.id.btn_edit)
    ImageButton btnEdit;
    @BindView(R.id.tv_article_title)
    TextView tvArticleTitle;
    @BindView(R.id.tv_article_source)
    TextView tvArticleSource;
    @BindView(R.id.tv_article_content)
    TextView tvArticleContent;
    private ArticleDetailPresenter mArticleDetailPresenter;
    public static String USER_ID_DETAIL = "USER_ID_DETAIL";
    public static String BROADCAST_ID_DETAIL = "BROADCAST_ID_DETAIL";
    public static String ARTICLE_ID_DETAIL = "ARTICLE_ID_DETAIL";

    private String broadcastId;//用来查询播放进度。待读传-1，已读历史传-2，收藏传-3，我创建的传-4。
    private String articleId;//文章id

    @Override
    protected void preView() {
        setContentView(R.layout.activity_article_detail);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String userId = intent.getStringExtra(USER_ID_DETAIL);
        broadcastId = intent.getStringExtra(BROADCAST_ID_DETAIL);
        articleId = intent.getStringExtra(ARTICLE_ID_DETAIL);
        //由此判断该文章是否是用户自己创建的,如果获取的文章的用户id跟userInfo中的一致，则表明该文章是自己创建的
        if (ContentManager.getInstance().getUserInfo().getUserId().equals(userId)) {
            btnEdit.setVisibility(View.VISIBLE);
        } else {
            btnEdit.setVisibility(View.GONE);
        }
        mArticleDetailPresenter = (ArticleDetailPresenter) createPresenter(ArticleDetailPresenter.class);
        mArticleDetailPresenter.attachView(this);
        doGetArticleDetail();
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

    @OnClick({R.id.btn_left, R.id.btn_share, R.id.btn_more, R.id.btn_edit})
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
            default:
                break;
        }
    }

    @Override
    public void onSuccessArticleDetail(ArticleDetail2Info info) {
        tvArticleTitle.setText(info.getTitle());
        tvArticleContent.setText(info.getContent());
        if (TextUtils.isEmpty(String.valueOf(info.getSourceName())) || String.valueOf(info.getSourceName()).equals("null")) {
            tvArticleSource.setText("");
        } else {
            tvArticleSource.setText(String.valueOf(info.getSourceName()));
        }
        hideLoading();
    }

    @Override
    public void onErrorArticleDetail(int code, String errorMsg) {

    }
}
