package cn.xylink.mting.ui.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastItemAddRequest;
import cn.xylink.mting.bean.BroadcastListRequest;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.contract.BroadcastAddArticleContact;
import cn.xylink.mting.contract.BroadcastListContact;
import cn.xylink.mting.event.BroadcastRefreshEvent;
import cn.xylink.mting.event.TingRefreshEvent;
import cn.xylink.mting.presenter.BroadcastAddArticlePresenter;
import cn.xylink.mting.presenter.BroadcastItemAddPresenter;
import cn.xylink.mting.presenter.BroadcastListPresenter;
import cn.xylink.mting.ui.adapter.SelectArticleAddAdapter;
import cn.xylink.mting.widget.EndlessRecyclerOnScrollListener;
import cn.xylink.mting.widget.HDividerItemDecoration;

/**
 * @author JoDragon
 */
public class SelectArticleAddActivity extends BasePresenterActivity implements BroadcastListContact.IBroadcastListView
        , SelectArticleAddAdapter.OnItemClickListener, BroadcastAddArticleContact.IBroadcastAddArticleView {

    public static final String EXTRA_BROADCASTID = "extra_broadcast_id";
    public static final String EXTRA_BROADCASTID_TO = "extra_broadcast_id_to";
    public static final String EXTRA_TITLE = "extra_title";
    @BindView(R.id.srl_refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_titlebar_title)
    TextView mTableBarTitleTextView;
    @BindView(R.id.rv_select_article_add)
    RecyclerView mRecyclerView;
    private BroadcastListPresenter mPresenter;
    private SelectArticleAddAdapter mAdapter;
    private BroadcastAddArticlePresenter mBroadcastAddArticlePresenter;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_select_article_add);
    }

    @Override
    protected void initView() {
        mPresenter = (BroadcastListPresenter) createPresenter(BroadcastListPresenter.class);
        mPresenter.attachView(this);
        mBroadcastAddArticlePresenter = (BroadcastAddArticlePresenter) createPresenter(BroadcastAddArticlePresenter.class);
        mBroadcastAddArticlePresenter.attachView(this);
        mAdapter = new SelectArticleAddAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new HDividerItemDecoration(this));
        mRecyclerView.addOnScrollListener(endlessScrollListener);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            loadMoreData();
        });
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mTableBarTitleTextView.setText(getIntent().getStringExtra(EXTRA_TITLE));

        initList();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {

    }

    private void initList() {
        BroadcastListRequest request = new BroadcastListRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mPresenter.getBroadcastList(request, false);
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


    @OnClick({R.id.iv_titlebar_back, R.id.tv_titlebar_close})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titlebar_back:
            case R.id.tv_titlebar_close:
                finish();
                break;
        }
    }

    private boolean isAdded= false;
    @Override
    public void onBroadcastListSuccess(BaseResponseArray<BroadcastInfo> baseResponse, boolean isLoadMore) {
        List<BroadcastInfo> data = baseResponse.data;
        if (!isLoadMore && data.size() == 0) {
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
        }
        if (isLoadMore) {
            mRefreshLayout.finishLoadMore(true);
        } else {
            mRefreshLayout.finishRefresh(true);
        }
        if (data != null && data.size() > 0) {
            if (!isLoadMore) {
                mAdapter.clearData();
            }
            mAdapter.setData(data);
        }
        if (data.size() < 20) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    @Override
    public void onBroadcastListError(int code, String errorMsg, boolean isLoadMore) {
        mRefreshLayout.finishLoadMore(false);
        if (isLoadMore) {

        } else {
//            if (code == 9999) {
//                showNetworlError();
//            } else {
//                showLoadFail();
//            }
        }
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    EndlessRecyclerOnScrollListener endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int current_page) {
            loadMoreData();
        }
    };

    @Override
    public void onItemClick(BroadcastInfo article) {
        BroadcastItemAddRequest request =new BroadcastItemAddRequest();
        request.setArticleIds(article.getArticleId());
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID_TO));
        request.doSign();
        mBroadcastAddArticlePresenter.broadcastAddArticle(request);
    }

    @Override
    public void onBroadcastAddArticleSuccess(BaseResponse baseResponse) {
        isAdded = true;
        BroadcastItemAddRequest request = (BroadcastItemAddRequest) baseResponse.getData();
        mAdapter.changeItemChecked(request.getArticleIds());
    }

    @Override
    public void onBroadcastAddArticleError(int code, String errorMsg) {

    }

    @Override
    protected void onDestroy() {
        if (isAdded){
            EventBus.getDefault().post(new BroadcastRefreshEvent());
            EventBus.getDefault().post(new TingRefreshEvent());
        }
        super.onDestroy();
    }
}
