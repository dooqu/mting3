package cn.xylink.mting.ui.activity;

import android.content.Intent;
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
import cn.xylink.mting.bean.ArticleIdsRequest;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastListRequest;
import cn.xylink.mting.bean.DelBroadcastArticleRequest;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.contract.BroadcastAllDelContact;
import cn.xylink.mting.contract.BroadcastListContact;
import cn.xylink.mting.event.ArrangeDelNotifEvent;
import cn.xylink.mting.event.BroadcastRefreshEvent;
import cn.xylink.mting.presenter.BroadcastAllDelPresenter;
import cn.xylink.mting.presenter.BroadcastListPresenter;
import cn.xylink.mting.ui.adapter.ArrangeAdapter;
import cn.xylink.mting.ui.dialog.TipDialog;
import cn.xylink.mting.widget.EndlessRecyclerOnScrollListener;
import cn.xylink.mting.widget.HDividerItemDecoration;

public class ArrangeActivity extends BasePresenterActivity implements BroadcastListContact.IBroadcastListView,
        BroadcastAllDelContact.IBroadcastAllDelView, ArrangeAdapter.OnItemClickListener {
    public static final String EXTRA_BROADCASTID = "extra_broadcast_id";
    public static final String EXTRA_IS_MY_CREATE_BROADCAST = "extra_is_my_create_broadcast";
    @BindView(R.id.rv_arrange)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_arrange_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_arrange_all)
    TextView mAllTextView;
    @BindView(R.id.tv_arrange_addto)
    TextView mAddToTextView;
    @BindView(R.id.tv_arrange_del)
    TextView mDelTextView;
    @BindView(R.id.srl_arrange_refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private BroadcastListPresenter mBroadcastListPresenter;
    private BroadcastAllDelPresenter mBroadcastAllDelPresenter;
    private ArrangeAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void preView() {
        setContentView(R.layout.activity_arrange);
    }

    @Override
    protected void initView() {
        mBroadcastListPresenter = (BroadcastListPresenter) createPresenter(BroadcastListPresenter.class);
        mBroadcastListPresenter.attachView(this);
        mBroadcastAllDelPresenter = (BroadcastAllDelPresenter) createPresenter(BroadcastAllDelPresenter.class);
        mBroadcastAllDelPresenter.attachView(this);

        mRecyclerView.setItemAnimator(null);
        mAdapter = new ArrangeAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(endlessScrollListener);
        mRecyclerView.addItemDecoration(new HDividerItemDecoration(this));
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            loadMoreData();
        });
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        if (!getIntent().getBooleanExtra(EXTRA_IS_MY_CREATE_BROADCAST, false) && !getIntent().getStringExtra(EXTRA_BROADCASTID).startsWith("-")) {
            mDelTextView.setVisibility(View.GONE);
        }
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
        mBroadcastListPresenter.getBroadcastList(request, false);
    }


    private void loadMoreData() {
        if (mAdapter != null && mAdapter.getArticleList() != null && mAdapter.getArticleList().size() > 20) {
            BroadcastListRequest request = new BroadcastListRequest();
            request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
            request.setEvent(WorldRequest.EVENT.NEW.name().toLowerCase());
            request.setLastAt(mAdapter.getArticleList().get(mAdapter.getArticleList().size() - 1).getLastAt());
            request.doSign();
            mBroadcastListPresenter.getBroadcastList(request, true);
        }
    }

    @OnClick({R.id.tv_arrange_all, R.id.tv_arrange_close, R.id.tv_arrange_addto, R.id.tv_arrange_del})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_arrange_all:
                mAdapter.selectAll();
                break;
            case R.id.tv_arrange_close:
                this.finish();
                break;
            case R.id.tv_arrange_addto:
                Intent intent = new Intent(this, BroadcastItemAddActivity.class);
                intent.putExtra(BroadcastItemAddActivity.ARTICLE_IDS_EXTRA, mAdapter.getSelectArticleIDs());
                startActivity(intent);
                this.finish();
                break;
            case R.id.tv_arrange_del:
                TipDialog alertDialog = new TipDialog(this);
                alertDialog.setMsg("确定删除所选文章吗？", "取消", "确定", new TipDialog.OnTipListener() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        doDel();
                    }
                });
                alertDialog.show();
                break;
            default:
        }
    }

    private void doDel() {
        ArticleIdsRequest request = new ArticleIdsRequest();
        request.setArticleIds(mAdapter.getSelectArticleIDs());
        request.doSign();
        BroadcastInfo info = new BroadcastInfo();
        info.setArticleId(mAdapter.getSelectArticleIDs());
        if (getIntent().getStringExtra(EXTRA_BROADCASTID).startsWith("-")) {
            switch (getIntent().getStringExtra(EXTRA_BROADCASTID)) {
                case Const.SystemBroadcast.SYSTEMBROADCAST_UNREAD:
                    mBroadcastAllDelPresenter.delUnread(request, info);
                    break;
                case Const.SystemBroadcast.SYSTEMBROADCAST_READED:
                    mBroadcastAllDelPresenter.delReaded(request, info);
                    break;
                case Const.SystemBroadcast.SYSTEMBROADCAST_STORE:
                    mBroadcastAllDelPresenter.delStore(request, info);
                    break;
                case Const.SystemBroadcast.SYSTEMBROADCAST_MY_CREATE_ARTICLE:
                    mBroadcastAllDelPresenter.delMyCreateArticle(request, info);
                    break;
                default:
            }
        } else {
            DelBroadcastArticleRequest articleRequest = new DelBroadcastArticleRequest();
            articleRequest.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
            articleRequest.setArticleIds(mAdapter.getSelectArticleIDs());
            articleRequest.doSign();
            mBroadcastAllDelPresenter.delMyCreateBroadcastArticle(articleRequest, info);
        }
    }

    @Override
    public void onBroadcastAllDelSuccess(BaseResponse response, BroadcastInfo info) {
        EventBus.getDefault().post(new BroadcastRefreshEvent());
        EventBus.getDefault().post(new ArrangeDelNotifEvent(getIntent().getStringExtra(EXTRA_BROADCASTID),info.getArticleId()));
        this.finish();
    }

    @Override
    public void onBroadcastAllDelError(int code, String errorMsg) {

    }

    @Override
    public void onBroadcastListSuccess(BaseResponseArray<BroadcastInfo> baseResponse, boolean isLoadMore) {
        List<BroadcastInfo> data = baseResponse.data;
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

    }

    private EndlessRecyclerOnScrollListener endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int current_page) {
            loadMoreData();
        }
    };

    @Override
    public void itemCheckChanged(int selectCount) {
        if (selectCount == mAdapter.getItemCount()) {
            mAllTextView.setTextColor(getResources().getColor(R.color.c488def));
        } else {
            mAllTextView.setTextColor(getResources().getColor(R.color.c333333));
        }
        if (selectCount > 0) {
            mAddToTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.mipmap.icon_addto), null, null);
            mDelTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.mipmap.icon_del), null, null);
        } else {
            mAddToTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.mipmap.icon_addto_light), null, null);
            mDelTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.mipmap.icon_del_light), null, null);
        }
    }
}
