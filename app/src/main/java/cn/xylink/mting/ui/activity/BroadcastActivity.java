package cn.xylink.mting.ui.activity;


import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

import java.util.List;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastListRequest;
import cn.xylink.mting.contract.BroadcastListContact;
import cn.xylink.mting.presenter.BroadcastListPresenter;
import cn.xylink.mting.ui.adapter.BroadcastAdapter;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.widget.TingHeaderView;

/**
 * @author JoDragon
 */
public class BroadcastActivity extends BasePresenterActivity implements BroadcastListContact.IBroadcastListView {

    public static final String EXTRA_BROADCASTID = "extra_broadcastid";
    public static final String EXTRA_TITLE = "extra_title";
    @BindView(R.id.ll_titlebar)
    LinearLayout mTitleBarLayout;
    @BindView(R.id.srl_refreshLayout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.iv_titlebar_back)
    ImageView mBackImageView;
    @BindView(R.id.iv_titlebar_share)
    ImageView mShareImageView;
    @BindView(R.id.iv_titlebar_menu)
    ImageView mMenuImageView;
    @BindView(R.id.tv_titlebar_title)
    TextView mTitleTextView;
    @BindView(R.id.rv_broadcast)
    RecyclerView mRecyclerView;
    @BindView(R.id.nsv_broadcast)
    NestedScrollView mScrollView;
    private BroadcastListPresenter mPresenter;
    private BroadcastAdapter mAdapter;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_broadcast);
    }

    @Override
    protected void initView() {
        mPresenter = (BroadcastListPresenter) createPresenter(BroadcastListPresenter.class);
        mPresenter.attachView(this);
        mRecyclerView.setItemAnimator(null);
        mAdapter = new BroadcastAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setNestedScrollingEnabled(false);
        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            initList();
        });
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
        });
        mRefreshLayout.setRefreshHeader(new TingHeaderView(this).setIsWrite(false));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setDragRate(2f);
        mTitleTextView.setText(getIntent().getStringExtra(EXTRA_TITLE));
        initList();
        drawable = getResources().getDrawable(R.color.white);
        mScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            L.v(i1);
            scrollY = i1;
            float pro = i1 / 358f;
            if (pro <= 1) {
                drawable.setAlpha((int) (pro * 255));
                mTitleBarLayout.setBackground(drawable);
            }else {
                drawable.setAlpha(255);
                mTitleBarLayout.setBackground(drawable);
            }
            if (pro>0.7){
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                mBackImageView.setImageResource(R.mipmap.icon_back_b);
                mShareImageView.setImageResource(R.mipmap.icon_share_b);
                mMenuImageView.setImageResource(R.mipmap.icon_menu_b);
                mTitleTextView.setTextColor(getResources().getColor(R.color.c333333));
            }else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                mBackImageView.setImageResource(R.mipmap.icon_back_w);
                mShareImageView.setImageResource(R.mipmap.icon_share_w);
                mMenuImageView.setImageResource(R.mipmap.icon_menu_w);
                mTitleTextView.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }

    int scrollY = 0;
    float oldRawY = 0;
    Drawable drawable;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            oldRawY = ev.getRawY();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            L.v(ev.getRawY());
            if (scrollY == 0 & ev.getRawY() - oldRawY > 0) {
//                mRecyclerView.setNestedScrollingEnabled(true);
                mRefreshLayout.setEnableRefresh(true);
            }
            if (ev.getRawY() - oldRawY < 0) {
//                mRecyclerView.setNestedScrollingEnabled(false);
                mRefreshLayout.setEnableRefresh(false);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initList() {
        BroadcastListRequest request = new BroadcastListRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mPresenter.getBroadcastList(request);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    public void onBroadcastListSuccess(List<BroadcastInfo> data) {
        mRefreshLayout.finishRefresh(true);
        mAdapter.clearData();
        mAdapter.setData(data);
    }

    @Override
    public void onBroadcastListError(int code, String errorMsg) {

    }
}
