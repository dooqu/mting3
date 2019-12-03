package cn.xylink.mting.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.WorldInfo;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.contract.WorldListContact;
import cn.xylink.mting.presenter.WorldListPresenter;
import cn.xylink.mting.ui.activity.SearchActivity;
import cn.xylink.mting.ui.adapter.WorldAdapter;
import cn.xylink.mting.ui.dialog.MainAddMenuPop;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.widget.EndlessRecyclerOnScrollListener;
import cn.xylink.mting.widget.HDividerItemDecoration;
import cn.xylink.mting.widget.TingHeaderView;

/**
 * @author JoDragon
 */
public class WorldFragment extends BasePresenterFragment implements WorldListContact.IWorldListView, MainAddMenuPop.OnMainAddMenuListener {

    @BindView(R.id.rv_tab_world)
    RecyclerView mRecyclerView;
    private WorldListPresenter mWorldListPresenter;
    private WorldAdapter mAdapter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.iv_world_add)
    ImageView mMenuImageView;

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
        mAdapter = new WorldAdapter(getActivity());
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
        WorldRequest request = new WorldRequest();
        request.setEvent(WorldRequest.EVENT.NEW.name().toLowerCase());
        request.setLastAt(mAdapter.getArticleList().get(mAdapter.getArticleList().size() - 1).getLastAt());
        request.doSign();
        mWorldListPresenter.getWorldList(request, true);
    }

    @OnClick({R.id.iv_world_add, R.id.tv_world_search})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_world_add:
                MainAddMenuPop pop = new MainAddMenuPop(getActivity(), this);
                pop.showAsRight(mMenuImageView);
                break;
            case R.id.tv_world_search:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:
        }
    }

    @Override
    public void onWorldListSuccess(List<WorldInfo> data, boolean isLoadMore) {
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
    }

    @Override
    public void onCreateArticle() {

    }

    @Override
    public void onPut() {

    }

    @Override
    public void onCreateBroadcast() {

    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    EndlessRecyclerOnScrollListener endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int current_page) {
            loadMoreData();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(endlessScrollListener);
        }
    }
}
