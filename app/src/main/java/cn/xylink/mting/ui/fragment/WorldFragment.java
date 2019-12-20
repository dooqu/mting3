package cn.xylink.mting.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.bean.WorldInfo;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.contract.WorldListContact;
import cn.xylink.mting.event.ArticleDetailScrollEvent;
import cn.xylink.mting.presenter.WorldListPresenter;
import cn.xylink.mting.ui.activity.ArticleCreateActivity;
import cn.xylink.mting.ui.activity.ArticleDetailActivity;
import cn.xylink.mting.ui.activity.BroadcastCreateActivity;
import cn.xylink.mting.ui.activity.SearchActivity;
import cn.xylink.mting.ui.adapter.WorldAdapter;
import cn.xylink.mting.ui.dialog.InputDialog;
import cn.xylink.mting.ui.dialog.MainAddMenuPop;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.widget.EndlessRecyclerOnScrollListener;
import cn.xylink.mting.widget.HDividerItemDecoration;
import cn.xylink.mting.widget.TingHeaderView;

/**
 * @author JoDragon
 */
public class WorldFragment extends BasePresenterFragment implements WorldListContact.IWorldListView, MainAddMenuPop.OnMainAddMenuListener
        , WorldAdapter.OnItemClickListener {

    @BindView(R.id.rv_tab_world)
    RecyclerView mRecyclerView;
    private WorldListPresenter mWorldListPresenter;
    private WorldAdapter mAdapter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.iv_world_add)
    ImageView mMenuImageView;
    @BindView(R.id.ll_empty)
    LinearLayout mEmptylayout;
    @BindView(R.id.iv_empty)
    ImageView mEmptyImageView;
    @BindView(R.id.tv_empty)
    TextView mEmptyTextView;

    public static WorldFragment newInstance() {
        return new WorldFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.world_fragment;
    }

    @Override
    protected void initView(View view) {
        view.setPadding(0, DensityUtil.getStatusBarHeight(getActivity()), 0, 0);
        mWorldListPresenter = (WorldListPresenter) createPresenter(WorldListPresenter.class);
        mWorldListPresenter.attachView(this);
        mAdapter = new WorldAdapter(getActivity(), this);
        mRecyclerView.setItemAnimator(null);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(endlessScrollListener);
        mRecyclerView.addItemDecoration(new HDividerItemDecoration(getActivity()));
        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            initData();
        });
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            loadMoreData();
        });
        mRefreshLayout.setEnableAutoLoadMore(true);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setRefreshHeader(new TingHeaderView(getActivity()).setIsWrite(false));
//        mRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(getResources()
//        .getColor(R.color.c488def)));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
    }

    @Override
    protected void initData() {
        WorldRequest request = new WorldRequest();
        request.doSign();
        mWorldListPresenter.getWorldList(request, false);
        mRefreshLayout.resetNoMoreData();
    }

    private void loadMoreData() {
        if (mAdapter.getArticleList() != null && mAdapter.getArticleList().size() > 0) {
            WorldRequest request = new WorldRequest();
            request.setEvent(WorldRequest.EVENT.OLD.name().toLowerCase());
            request.setLastAt(mAdapter.getArticleList().get(mAdapter.getArticleList().size() - 1).getLastAt());
            request.doSign();
            mWorldListPresenter.getWorldList(request, true);
        }
    }

    @OnClick({R.id.iv_world_add, R.id.tv_world_search, R.id.ll_empty})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_world_add:
                MainAddMenuPop pop = new MainAddMenuPop(getActivity(), this);
                pop.showAsRight(mMenuImageView);
                break;
            case R.id.tv_world_search:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.ll_empty:
                initData();
                mEmptylayout.setEnabled(false);
                break;
            default:
        }
    }

    @Override
    public void onWorldListSuccess(List<WorldInfo> data, boolean isLoadMore) {
        L.v(data.size() + "******************************************");
        mEmptylayout.setVisibility(View.GONE);
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
    public void onWorldListError(int code, String errorMsg, boolean isLoadMore) {
        mRefreshLayout.finishLoadMore(false);
        mRefreshLayout.finishRefresh(true);
        mEmptylayout.setVisibility(View.VISIBLE);
        mEmptylayout.setEnabled(true);
        mAdapter.clearData();
        mAdapter.notifyDataSetChanged();
        if (code == 9999) {
            showNetworlError();
        } else {
            showLoadFail();
        }

    }

    @Override
    public void onCreateArticle() {
        startActivity(new Intent(getActivity(), ArticleCreateActivity.class));
    }

    @Override
    public void onPut() {
        InputDialog dialog = new InputDialog(getActivity());
        dialog.show();
    }

    @Override
    public void onCreateBroadcast() {
        startActivity(new Intent(getActivity(), BroadcastCreateActivity.class));
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    EndlessRecyclerOnScrollListener endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int current_page) {
            loadMoreData();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy>2){
                EventBus.getDefault().post(new ArticleDetailScrollEvent("upGlide"));
            }else if (dy<-2){
                EventBus.getDefault().post(new ArticleDetailScrollEvent("glide"));
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
    public void onItemClick(WorldInfo article) {
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.BROADCAST_ID_DETAIL, article.getBroadcastId());
        intent.putExtra(ArticleDetailActivity.ARTICLE_ID_DETAIL, article.getArticleId());
        startActivity(intent);
    }

    private void showLoadFail() {
        mEmptylayout.setVisibility(View.VISIBLE);
        mEmptyImageView.setImageResource(R.mipmap.bg_load_fail);
        mEmptyTextView.setText("加载失败");
    }

    private void showNetworlError() {
        mEmptylayout.setVisibility(View.VISIBLE);
        mEmptyImageView.setImageResource(R.mipmap.bg_network_error);
        mEmptyTextView.setText("网络开小差了，等会试试吧");
    }

}
