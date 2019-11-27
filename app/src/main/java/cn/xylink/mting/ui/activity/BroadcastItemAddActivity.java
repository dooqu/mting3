package cn.xylink.mting.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.bean.BroadcastItemAddInfo;
import cn.xylink.mting.contract.BroadcastItemAddContact;
import cn.xylink.mting.presenter.BroadcastItemAddPresenter;
import cn.xylink.mting.ui.adapter.BroadcastItemAddAdapter;

/**
 * @author wjn
 * @date 2019/11/25
 */
public class BroadcastItemAddActivity extends BasePresenterActivity implements BroadcastItemAddContact.IBroadcastItemAddView, BroadcastItemAddAdapter.OnItemClickListener {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.btn_left)
    ImageButton btnLeft;
    //    @BindView(R.id.refreshLayout)
//    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_broadcast_list)
    RecyclerView mRecyclerView;
    private BroadcastItemAddAdapter mAdapter;
    private BroadcastItemAddPresenter mBroadcastItemAddPresenter;
    private List<BroadcastItemAddInfo> mDataBean;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_broadcast_item_add);
    }

    @Override
    protected void initView() {
        mBroadcastItemAddPresenter = (BroadcastItemAddPresenter) createPresenter(BroadcastItemAddPresenter.class);
        mBroadcastItemAddPresenter.attachView(this);
        mAdapter = new BroadcastItemAddAdapter(this);
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
//            initList();
//        });
//        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
//            loadMoreData();
//        });
//        mRefreshLayout.setRefreshHeader(new TingHeaderView(this).setIsWrite(false));
//        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(getResources().getColor(R.color.c488def)));
//        mRefreshLayout.setDragRate(2f);
        initList();
        mAdapter.setmOnItemClickListener(this);

    }

    @Override
    protected void initData() {

    }

    private void initList() {
        BaseRequest request = new BaseRequest();
        request.doSign();
        mBroadcastItemAddPresenter.getBroadcastItemAddList(request, false);
    }

    private void loadMoreData() {
        if (null != mAdapter && mAdapter.getBroadcastItemList() != null && mAdapter.getBroadcastItemList().size() > 0) {
            BaseRequest request = new BaseRequest();
            request.doSign();
            mBroadcastItemAddPresenter.getBroadcastItemAddList(request, true);
        }
    }

    @Override
    protected void initTitleBar() {
        btnLeft.setVisibility(View.INVISIBLE);
        tvRight.setText("关闭");
        tvRight.setTextColor(getResources().getColor(R.color.c333333));
        tvTitle.setText("添加到播单");
    }

    @Override
    public void onBroadcastItemAddSuccess(List<BroadcastItemAddInfo> data, boolean isLoadMore) {
/*//        if (isLoadMore) {
//            mRefreshLayout.finishLoadMore(true);
//            if (data.size() < 20) {
//                mRefreshLayout.finishLoadMoreWithNoMoreData();
//            }
//        } else {
//            mRefreshLayout.finishRefresh(true);
//        }
//        if (data != null && data.size() > 0) {
//            if (!isLoadMore) {
//                mAdapter.clearData();
//            }
//            mAdapter.setData(data);
//        }*/
        this.mDataBean = data;
        if (null != data && data.size() > 0) {
            mAdapter.clearData();
            mAdapter.setData(data);
        }
    }

    @Override
    public void onBroadcastItemAddError(int code, String errorMsg, boolean isLoadMore) {

    }

    @Override
    public void onItemClick(int position) {
        toastLong("您点击了第" + (position + 1) + "项的broadcastId是：" + mDataBean.get(position).getBroadcastId());
    }

    @OnClick({R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_right:
                finish();
                break;
            default:
                break;
        }
    }
}
