package cn.xylink.mting.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

import java.util.List;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.WorldInfo;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.contract.WorldListContact;
import cn.xylink.mting.presenter.WorldListPresenter;
import cn.xylink.mting.ui.adapter.WorldAdapter;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.widget.TingHeaderView;

/**
 * @author JoDragon
 */
public class WorldFragment extends BasePresenterFragment implements WorldListContact.IWorldListView {

    @BindView(R.id.rv_tab_world)
    RecyclerView mRecyclerView;
    private WorldListPresenter mWorldListPresenter;
    private WorldAdapter mAdapter;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    public static WorldFragment newInstance() {
        return new WorldFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.world_fragment;
    }

    @Override
    protected void initView(View view) {
        view.setPadding(0, DensityUtil.getStatusBarHeight(getActivity()),0,0);
        mWorldListPresenter = (WorldListPresenter) createPresenter(WorldListPresenter.class);
        mWorldListPresenter.attachView(this);
        mAdapter = new WorldAdapter(getActivity());
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            initData();
        });
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
        });
        mRefreshLayout.setRefreshHeader(new TingHeaderView(getActivity()).setIsWrite(false));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
    }

    @Override
    protected void initData() {
        WorldRequest request = new WorldRequest();
        request.doSign();
        mWorldListPresenter.getWorldList(request);
    }

    @Override
    public void onWorldListSuccess(List<WorldInfo> data) {
        mRefreshLayout.finishRefresh(true);
        if (data != null && data.size() > 0) {
            mAdapter.clearData();
            mAdapter.setData(data);
        }
    }

    @Override
    public void onWorldListError(int code, String errorMsg) {

    }
}
