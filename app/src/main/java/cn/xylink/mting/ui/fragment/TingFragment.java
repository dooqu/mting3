package cn.xylink.mting.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.SetTopRequest;
import cn.xylink.mting.bean.SubscribeRequest;
import cn.xylink.mting.bean.TingInfo;
import cn.xylink.mting.contract.SetTopContact;
import cn.xylink.mting.contract.SubscribeContact;
import cn.xylink.mting.contract.TingListContact;
import cn.xylink.mting.presenter.SetTopPresenter;
import cn.xylink.mting.presenter.SubscribePresenter;
import cn.xylink.mting.presenter.TingListPresenter;
import cn.xylink.mting.ui.activity.BroadcastActivity;
import cn.xylink.mting.ui.activity.CreateBroadcastActivity;
import cn.xylink.mting.ui.adapter.TingAdapter;
import cn.xylink.mting.ui.dialog.BottomTingDialog;
import cn.xylink.mting.ui.dialog.BottomTingItemModle;
import cn.xylink.mting.ui.dialog.MainAddMenuPop;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.widget.TingHeaderView;

/**
 * @author JoDragon
 */
public class TingFragment extends BasePresenterFragment implements TingListContact.ITingListView, TingAdapter.OnItemClickListener,
        MainAddMenuPop.OnMainAddMenuListener, BottomTingDialog.OnBottomTingListener, SetTopContact.ISetTopView, SubscribeContact.ISubscribeView {

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
    private BottomTingDialog mBottomTingDialog;
    private SetTopPresenter mSetTopPresenter;
    private SubscribePresenter mSubscribePresenter;

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
        lp.topMargin = DensityUtil.getStatusBarHeight(getActivity());
        mTitleLayout.setLayoutParams(lp);
        mTingListPresenter = (TingListPresenter) createPresenter(TingListPresenter.class);
        mTingListPresenter.attachView(this);
        mSetTopPresenter = (SetTopPresenter) createPresenter(SetTopPresenter.class);
        mSetTopPresenter.attachView(this);
        mSubscribePresenter = (SubscribePresenter) createPresenter(SubscribePresenter.class);
        mSubscribePresenter.attachView(this);
        mAdapter = new TingAdapter(getActivity(), this);
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
        mBottomTingDialog = new BottomTingDialog(getActivity(), this);
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
        Intent intent = new Intent(getActivity(), BroadcastActivity.class);
        intent.putExtra(BroadcastActivity.EXTRA_BROADCASTID, article.getBroadcastId());
        intent.putExtra(BroadcastActivity.EXTRA_TITLE, article.getName());
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemLongClick(TingInfo article) {
        if ("-1".equals(article.getBroadcastId())) {
            mBottomTingDialog.setItemModle(new BottomTingItemModle("置顶", "取消置顶", getActivity().getResources().getDrawable(R.mipmap.icon_set_top),
                    getActivity().getResources().getDrawable(R.mipmap.icon_cancel_top), article.getTop() == 1, article.getBroadcastId()));
        } else {
            mBottomTingDialog.setItemModle(new BottomTingItemModle("置顶", "取消置顶", getActivity().getResources().getDrawable(R.mipmap.icon_set_top),
                            getActivity().getResources().getDrawable(R.mipmap.icon_cancel_top), article.getTop() == 1, article.getBroadcastId())
                    , new BottomTingItemModle("取消订阅", getActivity().getResources().getDrawable(R.mipmap.icon_cancel_top), article.getBroadcastId()));
        }
        mBottomTingDialog.show();
    }

    @OnClick({R.id.iv_ting_menu})
    void onClick(View view) {
        MainAddMenuPop pop = new MainAddMenuPop(getActivity(), this);
        pop.showAsRight(mMenuImageView);
    }

    private void setTop(String bid, String event) {
        SetTopRequest request = new SetTopRequest();
        request.setBroadcastId(bid);
        request.setEvent(event);
        request.doSign();
        mSetTopPresenter.setTop(request);
    }

    private void subscribe(String bid, String event) {
        SubscribeRequest request = new SubscribeRequest();
        request.setBroadcastId(bid);
        request.setEvent(event);
        request.doSign();
        mSubscribePresenter.subscribe(request);
    }

    @Override
    public void onCreateArticle() {

    }

    @Override
    public void onPut() {

    }

    @Override
    public void onCreateBroadcast() {
        startActivity(new Intent(getActivity(), CreateBroadcastActivity.class));
    }

    @Override
    public void onBottomTingItemClick(BottomTingItemModle modle) {
        if ("置顶".equals(modle.getName())) {
            setTop(modle.getId(), modle.isTwo() ? SetTopRequest.EVENT.CANCEL.name().toLowerCase() : SetTopRequest.EVENT.TOP.name().toLowerCase());
        } else if ("订阅".equals(modle.getName())) {

        }
    }

    @Override
    public void onSetTopSuccess(BaseResponse response) {
        initData();
        Toast.makeText(getActivity(), "置顶成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetTopError(int code, String errorMsg) {
        Toast.makeText(getActivity(), "置顶失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubscribeSuccess(BaseResponse response) {

    }

    @Override
    public void onSubscribeError(int code, String errorMsg) {

    }
}
