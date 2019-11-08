package cn.xylink.mting.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

import java.util.List;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.WorldInfo;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.contract.WorldListContact;
import cn.xylink.mting.presenter.WorldListPresenter;
import cn.xylink.mting.ui.adapter.WorldAdapter;

/**
 * @author JoDragon
 */
public class WorldFragment extends BasePresenterFragment implements WorldListContact.IWorldListView {

    @BindView(R.id.rv_tab_world)
    JRecycleView mRecyclerView;
    private WorldListPresenter mWorldListPresenter;
    private WorldAdapter mAdapter;
    private JRefreshAndLoadMoreAdapter mJAdapter;

    public static WorldFragment newInstance() {
        return new WorldFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.world_fragment;
    }

    @Override
    protected void initView(View view) {
        mWorldListPresenter = (WorldListPresenter) createPresenter(WorldListPresenter.class);
        mWorldListPresenter.attachView(this);
        mAdapter = new WorldAdapter(getActivity());
        mJAdapter = new JRefreshAndLoadMoreAdapter(getActivity(), mAdapter);
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mJAdapter);
        mJAdapter.setOnRefreshListener(() -> {
            //do something for refresh data
            WorldRequest request = new WorldRequest();
            request.doSign();
            mWorldListPresenter.getWorldList(request);
        });
        mJAdapter.setOnLoadMoreListener(() -> {

        });
    }

    @Override
    protected void initData() {
        WorldRequest request = new WorldRequest();
        request.doSign();
        mWorldListPresenter.getWorldList(request);
    }

    @Override
    public void onWorldListSuccess(List<WorldInfo> data) {
        mJAdapter.setRefreshComplete();
        if (data != null && data.size() > 0) {
            mAdapter.clearData();
            mAdapter.setData(data);
        }
    }

    @Override
    public void onWorldListError(int code, String errorMsg) {

    }
}
