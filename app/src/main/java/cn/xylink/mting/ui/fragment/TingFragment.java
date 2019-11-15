package cn.xylink.mting.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.TingInfo;
import cn.xylink.mting.contract.TingListContact;
import cn.xylink.mting.presenter.TingListPresenter;
import cn.xylink.mting.ui.activity.BroadcastActivity;
import cn.xylink.mting.ui.adapter.TingAdapter;
import cn.xylink.mting.ui.dialog.MainAddMenuPop;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.widget.TingHeaderView;

/**
 * @author JoDragon
 */
public class TingFragment extends BasePresenterFragment implements TingListContact.ITingListView ,TingAdapter.OnItemClickListener, MainAddMenuPop.OnMainAddMenuListener {

    @BindView(R.id.rv_tab_ting)
    RecyclerView mRecyclerView;
    private TingListPresenter mTingListPresenter;
    private TingAdapter mAdapter;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.ll_title)
    LinearLayout mTitleLayout;
    @BindView(R.id.iv_ting_menu)
    ImageView mMenuImageView;

    public static TingFragment newInstance() {
        return new TingFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.ting_fragment;
    }

    @Override
    protected void initView(View view) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTitleLayout.getLayoutParams();
        lp.topMargin=DensityUtil.getStatusBarHeight(getActivity());
        mTitleLayout.setLayoutParams(lp);
        mTingListPresenter = (TingListPresenter) createPresenter(TingListPresenter.class);
        mTingListPresenter.attachView(this);
        mAdapter = new TingAdapter(getActivity(),this);
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
        mRefreshLayout.setRefreshHeader(new TingHeaderView(getActivity()).setIsWrite(true));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setEnableLoadMore(false);
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
        mRefreshLayout.finishRefresh(true);
        if (sss != null && sss.size() > 0) {
            mAdapter.clearData();
            mAdapter.setData(sss);
        }
    }

    @Override
    public void onTingListError(int code, String errorMsg) {

    }

    @Override
    public void onItemClick(TingInfo article) {
        Intent intent =new Intent(getActivity(), BroadcastActivity.class);
        intent.putExtra(BroadcastActivity.EXTRA_BROADCASTID,article.getBroadcastId());
        intent.putExtra(BroadcastActivity.EXTRA_TITLE,article.getName());
        getActivity().startActivity(intent);
    }

    @OnClick({R.id.iv_ting_menu})
    void onClick(View view){
        MainAddMenuPop pop = new MainAddMenuPop(getActivity(), this);
        pop.showAsRight(mMenuImageView);
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
}
