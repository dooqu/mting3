package cn.xylink.mting.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

import java.util.List;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.TingInfo;
import cn.xylink.mting.contract.TingListContact;
import cn.xylink.mting.presenter.TingListPresenter;
import cn.xylink.mting.ui.adapter.TingAdapter;
import cn.xylink.mting.widget.TingRefreshView;

/**
 * @author JoDragon
 */
public class TingFragment extends BasePresenterFragment implements TingListContact.ITingListView {

    @BindView(R.id.rv_tab_ting)
    JRecycleView mRecyclerView;
    private TingListPresenter mTingListPresenter;
    private TingAdapter mAdapter;
    private JRefreshAndLoadMoreAdapter mJAdapter;

    public static TingFragment newInstance() {
        return new TingFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.ting_fragment;
    }

    @Override
    protected void initView(View view) {
        mTingListPresenter = (TingListPresenter) createPresenter(TingListPresenter.class);
        mTingListPresenter.attachView(this);
        mAdapter = new TingAdapter(getActivity());
        mJAdapter = new JRefreshAndLoadMoreAdapter(getActivity(),mAdapter);
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mJAdapter);
        mJAdapter.setIsOpenLoadMore(false);
        mJAdapter.setOnRefreshListener(() -> {
            //do something for refresh data
            BaseRequest request = new BaseRequest();
            request.doSign();
            mTingListPresenter.getTingList(request);
        });
        mJAdapter.setRefreshLoadView(new TingRefreshView(getActivity()));
        mJAdapter.setRefreshComplete();
    }

    @Override
    protected void initData() {
        BaseRequest request = new BaseRequest();
        request.doSign();
        mTingListPresenter.getTingList(request);
    }


    @Override
    public void onTingListSuccess(BaseResponseArray<TingInfo> response) {
        List<TingInfo> sss = response.data;
        mJAdapter.setRefreshComplete();
        if (sss != null && sss.size() > 0) {
            mAdapter.clearData();
            mAdapter.setData(sss);
        }
    }

    @Override
    public void onTingListError(int code, String errorMsg) {

    }
}
