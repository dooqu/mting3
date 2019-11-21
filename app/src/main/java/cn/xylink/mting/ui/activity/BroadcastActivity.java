package cn.xylink.mting.ui.activity;


import android.graphics.drawable.Drawable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.ScrollBoundaryDecider;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.util.DesignUtil;

import java.util.List;

import butterknife.BindView;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastDetailRequest;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastListRequest;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.contract.BroadcastDetailContact;
import cn.xylink.mting.contract.BroadcastListContact;
import cn.xylink.mting.presenter.BroadcastDetailPresenter;
import cn.xylink.mting.presenter.BroadcastListPresenter;
import cn.xylink.mting.ui.adapter.BroadcastAdapter;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.widget.TingHeaderView;

/**
 * @author JoDragon
 */
public class BroadcastActivity extends BasePresenterActivity implements BroadcastListContact.IBroadcastListView,
        BroadcastDetailContact.IBroadcastDetailView, NestedScrollView.OnScrollChangeListener {

    public static final String EXTRA_BROADCASTID = "extra_broadcast_id";
    public static final String EXTRA_TITLE = "extra_title";
    @BindView(R.id.ll_titlebar)
    LinearLayout mTitleBarLayout;
    @BindView(R.id.srl_refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.iv_titlebar_back)
    ImageView mBackImageView;
    @BindView(R.id.iv_titlebar_share)
    ImageView mShareImageView;
    @BindView(R.id.iv_titlebar_menu)
    ImageView mMenuImageView;
    @BindView(R.id.tv_titlebar_title)
    TextView mTableBarTitleTextView;
    @BindView(R.id.rv_broadcast)
    RecyclerView mRecyclerView;
    @BindView(R.id.nsv_broadcast)
    NestedScrollView mScrollView;
    private BroadcastListPresenter mPresenter;
    private BroadcastAdapter mAdapter;
    /**
     * bg_top
     */
    @BindView(R.id.rl_broadcast_top_layout)
    RelativeLayout mTopLayout;
    @BindView(R.id.iv_broadcast_img)
    ImageView mImageView;
    @BindView(R.id.tv_broadcast_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_broadcast_description)
    TextView mDesTextView;
    @BindView(R.id.tv_broadcast_subscribed)
    TextView mSubscribedTextView;
    @BindView(R.id.tv_broadcast_share2world)
    TextView mShare2worldTextView;
    private BroadcastDetailPresenter mBroadcastDetailPresenter;


    @Override
    protected void preView() {
        setContentView(R.layout.activity_broadcast);
    }

    @Override
    protected void initView() {
        mPresenter = (BroadcastListPresenter) createPresenter(BroadcastListPresenter.class);
        mPresenter.attachView(this);
        mBroadcastDetailPresenter = (BroadcastDetailPresenter) createPresenter(BroadcastDetailPresenter.class);
        mBroadcastDetailPresenter.attachView(this);
        mRecyclerView.setItemAnimator(null);
        mAdapter = new BroadcastAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            initList();
        });
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            loadMoreData();
        });
        mRefreshLayout.setRefreshHeader(new TingHeaderView(this).setIsWrite(false));
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale).setAnimatingColor(getResources().getColor(R.color.c488def)));
        mRefreshLayout.setDragRate(2f);
//        mRefreshLayout.setFooterInsetStart(90);
//        mRefreshLayout.setFooterMaxDragRate(0);
//        mRefreshLayout.setFooterTriggerRate(0);
        mRefreshLayout.setEnableAutoLoadMore(false);
        mTableBarTitleTextView.setText(getIntent().getStringExtra(EXTRA_TITLE));
        initList();
        if (getIntent().getStringExtra(EXTRA_BROADCASTID).startsWith("-")) {
            initSysBroadcast();
        } else {
            initDetail();
        }
        drawable = getResources().getDrawable(R.color.white);
        mScrollView.setOnScrollChangeListener(this);

//        mRefreshLayout.setRefreshContent(this.getLayoutInflater().inflate(R.layout.dialog_tip,null));
//        mRefreshLayout.setRefreshContent(mRecyclerView);
    }

    /**
     * 待读传-1，已读历史传-2，收藏传-3，我创建的传-4。
     */
    private void initSysBroadcast() {
        String id = getIntent().getStringExtra(EXTRA_BROADCASTID);
        mTitleTextView.setText("简介");
        if ("-1".equals(id)) {
            mImageView.setImageResource(R.mipmap.icon_head_unread);
            mDesTextView.setText("待读会自动存放您添加到轩辕 听内的文章");
        } else if ("-2".equals(id)) {
            mImageView.setImageResource(R.mipmap.icon_head_readed);
            mDesTextView.setText("这里显示您读过的所有文章");
        } else if ("-3".equals(id)) {
            mImageView.setImageResource(R.mipmap.icon_head_love);
            mDesTextView.setText("这里显示您收藏的所有文章");
        } else if ("-4".equals(id)) {
            mImageView.setImageResource(R.mipmap.icon_head_mycreate);
            mDesTextView.setText("这里显示您创建的所有文章");
        }
    }

    Drawable drawable;

    private void initList() {
        BroadcastListRequest request = new BroadcastListRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mPresenter.getBroadcastList(request, false);
    }

    private void initDetail() {
        BroadcastDetailRequest request = new BroadcastDetailRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mBroadcastDetailPresenter.getBroadcastDetail(request);
    }

    private void loadMoreData() {
        if (mAdapter != null && mAdapter.getArticleList() != null && mAdapter.getArticleList().size() > 0) {
            BroadcastListRequest request = new BroadcastListRequest();
            request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
            request.setEvent(WorldRequest.EVENT.NEW.name().toLowerCase());
            request.setLastAt(mAdapter.getArticleList().get(mAdapter.getArticleList().size() - 1).getLastAt());
            request.doSign();
            mPresenter.getBroadcastList(request, true);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {

    }

    @Override
    public void onBroadcastListSuccess(List<BroadcastInfo> data, boolean isLoadMore) {
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
    public void onBroadcastListError(int code, String errorMsg, boolean isLoadMore) {

    }

    @Override
    public void onBroadcastDetailSuccess(BroadcastDetailInfo data) {
        ViewGroup.LayoutParams lp = mTopLayout.getLayoutParams();
        lp.height = DensityUtil.dip2pxComm(this, 253f);
        mTopLayout.setLayoutParams(lp);
        mTitleTextView.setText(data.getCreateName());
        mDesTextView.setText(data.getInfo());
        ImageUtils.get().load(mImageView, 0, 0, 8, data.getPicture());
        ImageUtils.get().load(mTopLayout, data.getPicture());
        if (data.getCreateName().equals(ContentManager.getInstance().getUserInfo().getUserId())) {
            if (data.getShare() == 0) {
                mShare2worldTextView.setVisibility(View.VISIBLE);
            } else {
                mSubscribedTextView.setVisibility(View.VISIBLE);
                mSubscribedTextView.setText("已定阅：" + getSubscribedNum(data.getSubscribeTotal()));
            }
        } else {
            mSubscribedTextView.setVisibility(View.VISIBLE);
            mSubscribedTextView.setText("已定阅：" + getSubscribedNum(data.getSubscribeTotal()));
        }
    }

    @Override
    public void onBroadcastDetailError(int code, String errorMsg) {

    }

    /**
     * 客户端显示的订阅数规则：
     * 小于1000，<1千
     * 1000-10000，显示具体数字，如：6725
     * 10000以上，1万+
     */
    private String getSubscribedNum(int total) {
        if (total < 1000) {
            return "<1千";
        } else if (total > 10000) {
            return "1万+";
        }
        return total + "";
    }

    @Override
    public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
        L.v(i1);
        float pro = i1 / 358f;
        if (pro <= 1) {
            drawable.setAlpha((int) (pro * 255));
            mTitleBarLayout.setBackground(drawable);
        } else {
            drawable.setAlpha(255);
            mTitleBarLayout.setBackground(drawable);
        }
        if (pro > 0.7) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            mBackImageView.setImageResource(R.mipmap.icon_back_b);
            mShareImageView.setImageResource(R.mipmap.icon_share_b);
            mMenuImageView.setImageResource(R.mipmap.icon_menu_b);
            mTableBarTitleTextView.setTextColor(getResources().getColor(R.color.c333333));
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            mBackImageView.setImageResource(R.mipmap.icon_back_w);
            mShareImageView.setImageResource(R.mipmap.icon_share_w);
            mMenuImageView.setImageResource(R.mipmap.icon_menu_w);
            mTableBarTitleTextView.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
