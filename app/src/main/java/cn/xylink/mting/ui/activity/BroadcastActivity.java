package cn.xylink.mting.ui.activity;


import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.util.List;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastDetailRequest;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastListRequest;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.contract.BroadcastDetailContact;
import cn.xylink.mting.contract.BroadcastListContact;
import cn.xylink.mting.presenter.BroadcastDetailPresenter;
import cn.xylink.mting.presenter.BroadcastListPresenter;
import cn.xylink.mting.ui.adapter.BroadcastAdapter;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.widget.EndlessRecyclerOnScrollListener;

/**
 * @author JoDragon
 */
public class BroadcastActivity extends BasePresenterActivity implements BroadcastListContact.IBroadcastListView,
        BroadcastDetailContact.IBroadcastDetailView ,BroadcastAdapter.OnItemClickListener{

    public static final String EXTRA_BROADCASTID = "extra_broadcast_id";
    public static final String EXTRA_TITLE = "extra_title";
    @BindView(R.id.ll_titlebar)
    LinearLayout mTitleBarLayout;
    @BindView(R.id.srl_refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.iv_titlebar_back)
    ImageView mBackImageView;
    @BindView(R.id.iv_titlebar_share)
    ImageView mShareImageView;
    @BindView(R.id.iv_titlebar_menu)
    ImageView mMenuImageView;
    @BindView(R.id.tv_titlebar_title)
    TextView mTableBarTitleTextView;
    @BindView(R.id.rv_broadcast)
    RecyclerView mRecyclerView;
    private BroadcastListPresenter mPresenter;
    private BroadcastAdapter mAdapter;
    private BroadcastDetailPresenter mBroadcastDetailPresenter;


    @Override
    protected void preView() {
        setContentView(R.layout.activity_broadcast);
    }

    @Override
    protected void initView() {
        mPresenter = (BroadcastListPresenter) createPresenter(BroadcastListPresenter.class);
        mPresenter.attachView(this);
        mBroadcastDetailPresenter = (BroadcastDetailPresenter) createPresenter(BroadcastDetailPresenter.class);
        mBroadcastDetailPresenter.attachView(this);
        mRecyclerView.setItemAnimator(null);
        mAdapter = new BroadcastAdapter(this, getIntent().getStringExtra(EXTRA_BROADCASTID));
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(endlessScrollListener);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            loadMoreData();
        });
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mTableBarTitleTextView.setText(getIntent().getStringExtra(EXTRA_TITLE));
        initList();
        if (!getIntent().getStringExtra(EXTRA_BROADCASTID).startsWith("-")) {
            initDetail();
        }
        drawable = getResources().getDrawable(R.color.white);

//        mRefreshLayout.setRefreshContent(this.getLayoutInflater().inflate(R.layout.dialog_tip,null));
//        mRefreshLayout.setRefreshContent(mRecyclerView);
    }

    Drawable drawable;

    private void initList() {
        BroadcastListRequest request = new BroadcastListRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mPresenter.getBroadcastList(request, false);
    }

    private void initDetail() {
        BroadcastDetailRequest request = new BroadcastDetailRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mBroadcastDetailPresenter.getBroadcastDetail(request);
    }

    private void loadMoreData() {
        if (mAdapter != null && mAdapter.getArticleList() != null && mAdapter.getArticleList().size() > 20) {
            BroadcastListRequest request = new BroadcastListRequest();
            request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
            request.setEvent(WorldRequest.EVENT.NEW.name().toLowerCase());
            request.setLastAt(mAdapter.getArticleList().get(mAdapter.getArticleList().size() - 1).getLastAt());
            request.doSign();
            mPresenter.getBroadcastList(request, true);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    protected boolean enableSpeechService() {
        return true;
    }

    @Override
    public void onBroadcastListSuccess(List<BroadcastInfo> data, boolean isLoadMore) {
        if (isLoadMore) {
            mRefreshLayout.finishLoadMore(true);
            if (data.size() < 20) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        } else {
            mRefreshLayout.finishRefresh(true);
        }
        if (data != null && data.size() > 0) {
            if (!isLoadMore) {
                mAdapter.clearData();
            }
            mAdapter.setData(data);
        }
    }

    @Override
    public void onBroadcastListError(int code, String errorMsg, boolean isLoadMore) {
    }

    @Override
    public void onBroadcastDetailSuccess(BroadcastDetailInfo data) {
        if (mAdapter != null) {
            mAdapter.setDetailInfo(data);
        }
    }

    @Override
    public void onBroadcastDetailError(int code, String errorMsg) {

    }

    private int mDY = 0;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    EndlessRecyclerOnScrollListener endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int current_page) {
            loadMoreData();
            L.v("----------------------------------------------------------------------------------");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mDY += dy;
            float pro = mDY / 358f;
            if (pro <= 1) {
                drawable.setAlpha((int) (pro * 255));
                mTitleBarLayout.setBackground(drawable);
            } else {
                drawable.setAlpha(255);
                mTitleBarLayout.setBackground(drawable);
            }
            if (pro > 0.7) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                mBackImageView.setImageResource(R.mipmap.icon_back_b);
                mShareImageView.setImageResource(R.mipmap.icon_share_b);
                mMenuImageView.setImageResource(R.mipmap.icon_menu_b);
                mTableBarTitleTextView.setTextColor(getResources().getColor(R.color.c333333));
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                mBackImageView.setImageResource(R.mipmap.icon_back_w);
                mShareImageView.setImageResource(R.mipmap.icon_share_w);
                mMenuImageView.setImageResource(R.mipmap.icon_menu_w);
                mTableBarTitleTextView.setTextColor(getResources().getColor(R.color.white));
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(endlessScrollListener);
        }
    }

    @Override
    public void onItemClick(BroadcastInfo article) {
        Article article1 =new Article();
        article1.setTitle(article.getTitle());
        article1.setArticleId(article.getArticleId());
        article1.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        article1.setBroadcastTitle(getIntent().getStringExtra(EXTRA_TITLE));
        postToSpeechService(article1);
    }
}
