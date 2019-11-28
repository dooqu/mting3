package cn.xylink.mting.ui.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.AddStoreRequest;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastIdRequest;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastListRequest;
import cn.xylink.mting.bean.ArticleIdsRequest;
import cn.xylink.mting.bean.SetTopRequest;
import cn.xylink.mting.bean.SubscribeRequest;
import cn.xylink.mting.bean.WorldRequest;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.contract.AddStoreContact;
import cn.xylink.mting.contract.BroadcastAllDelContact;
import cn.xylink.mting.contract.BroadcastDetailContact;
import cn.xylink.mting.contract.BroadcastListContact;
import cn.xylink.mting.contract.DelStoreContact;
import cn.xylink.mting.contract.SetTopContact;
import cn.xylink.mting.contract.SubscribeContact;
import cn.xylink.mting.event.TingRefreshEvent;
import cn.xylink.mting.presenter.AddStorePresenter;
import cn.xylink.mting.presenter.BroadcastAllDelPresenter;
import cn.xylink.mting.presenter.BroadcastDetailPresenter;
import cn.xylink.mting.presenter.BroadcastListPresenter;
import cn.xylink.mting.presenter.DelStorePreesenter;
import cn.xylink.mting.presenter.SetTopPresenter;
import cn.xylink.mting.presenter.SubscribePresenter;
import cn.xylink.mting.ui.adapter.BroadcastAdapter;
import cn.xylink.mting.ui.dialog.BottomTingDialog;
import cn.xylink.mting.ui.dialog.BottomTingItemModle;
import cn.xylink.mting.ui.dialog.BroadcastItemMenuDialog;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.L;
import cn.xylink.mting.widget.EndlessRecyclerOnScrollListener;

/**
 * @author JoDragon
 */
public class BroadcastActivity extends BasePresenterActivity implements BroadcastListContact.IBroadcastListView,
        BroadcastDetailContact.IBroadcastDetailView, BroadcastAdapter.OnItemClickListener, BroadcastItemMenuDialog.OnBroadcastItemMenuListener
        , AddStoreContact.IAddStoreView, DelStoreContact.IDelStoreView, BottomTingDialog.OnBottomTingListener
        , SetTopContact.ISetTopView, SubscribeContact.ISubscribeView, BroadcastAllDelContact.IBroadcastAllDelView {

    public static final String EXTRA_BROADCASTID = "extra_broadcast_id";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_ISTOP = "extra_istop";
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
    private BroadcastListPresenter mPresenter;
    private BroadcastAdapter mAdapter;
    private BroadcastDetailPresenter mBroadcastDetailPresenter;
    private AddStorePresenter mAddStorePresenter;
    private DelStorePreesenter mDelStorePreesenter;
    private SetTopPresenter mSetTopPresenter;
    private SubscribePresenter mSubscribePresenter;
    private int isTopIntent;
    private BroadcastAllDelPresenter mBroadcastAllDelPresenter;

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
        mAddStorePresenter = (AddStorePresenter) createPresenter(AddStorePresenter.class);
        mAddStorePresenter.attachView(this);
        mDelStorePreesenter = (DelStorePreesenter) createPresenter(DelStorePreesenter.class);
        mDelStorePreesenter.attachView(this);
        mSetTopPresenter = (SetTopPresenter) createPresenter(SetTopPresenter.class);
        mSetTopPresenter.attachView(this);
        mSubscribePresenter = (SubscribePresenter) createPresenter(SubscribePresenter.class);
        mSubscribePresenter.attachView(this);
        mBroadcastAllDelPresenter = (BroadcastAllDelPresenter) createPresenter(BroadcastAllDelPresenter.class);
        mBroadcastAllDelPresenter.attachView(this);
        mRecyclerView.setItemAnimator(null);
        mAdapter = new BroadcastAdapter(this, getIntent().getStringExtra(EXTRA_BROADCASTID));
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(endlessScrollListener);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            loadMoreData();
        });
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mTableBarTitleTextView.setText(getIntent().getStringExtra(EXTRA_TITLE));
        if (!getIntent().getStringExtra(EXTRA_BROADCASTID).startsWith("-")) {
            initDetail();
        } else {
            mShareImageView.setVisibility(View.INVISIBLE);
            initList();
        }
        drawable = getResources().getDrawable(R.color.white);
        mBottomTingDialog = new BottomTingDialog(this, this);

//        mRefreshLayout.setRefreshContent(this.getLayoutInflater().inflate(R.layout.dialog_tip,null));
//        mRefreshLayout.setRefreshContent(mRecyclerView);
        isTopIntent = getIntent().getIntExtra(EXTRA_ISTOP, 0);
    }

    Drawable drawable;

    private void initList() {
        BroadcastListRequest request = new BroadcastListRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mPresenter.getBroadcastList(request, false);
    }

    private void initDetail() {
        BroadcastIdRequest request = new BroadcastIdRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mBroadcastDetailPresenter.getBroadcastDetail(request);
    }

    private void loadMoreData() {
        if (mAdapter != null && mAdapter.getArticleList() != null && mAdapter.getArticleList().size() > 20) {
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
    protected boolean enableSpeechService() {
        return true;
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

    private BroadcastDetailInfo mDetailInfo;

    @Override
    public void onBroadcastDetailSuccess(BroadcastDetailInfo data) {
        mDetailInfo = data;
        initList();
        if (mAdapter != null) {
            mAdapter.setDetailInfo(data);
        }
    }

    @Override
    public void onBroadcastDetailError(int code, String errorMsg) {

    }

    private int mDY = 0;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    EndlessRecyclerOnScrollListener endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int current_page) {
            loadMoreData();
            L.v("----------------------------------------------------------------------------------");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mDY += dy;
            float pro = mDY / 358f;
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
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(endlessScrollListener);
        }
    }

    @Override
    public void onItemClick(BroadcastInfo article) {
        Article article1 = new Article();
        article1.setTitle(article.getTitle());
        article1.setArticleId(article.getArticleId());
        article1.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        article1.setBroadcastTitle(getIntent().getStringExtra(EXTRA_TITLE));
        postToSpeechService(article1);
    }

    @Override
    public void onItemLongClick(BroadcastInfo article) {
        BroadcastItemMenuDialog dialog = new BroadcastItemMenuDialog(this);
        dialog.setBroadcastInfo(article);
        if (mDetailInfo != null && !mDetailInfo.getCreateUserId().equals(ContentManager.getInstance().getUserInfo().getUserId())) {
            dialog.isShowDel(false);
        }
        if ("-3".equals(getIntent().getStringExtra(EXTRA_BROADCASTID))) {
            dialog.isShowDel(false);
        }
        dialog.setListener(this);
        dialog.show();
    }

    private BottomTingDialog mBottomTingDialog;

    @OnClick({R.id.iv_titlebar_share, R.id.iv_titlebar_menu, R.id.iv_titlebar_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titlebar_back:
                this.finish();
                break;
            case R.id.iv_titlebar_share:
                if (mDetailInfo != null) {
                    BroadcastItemMenuDialog dialog = new BroadcastItemMenuDialog(this);
                    dialog.setDetailInfo(mDetailInfo);
                    dialog.show();
                }
                break;
            case R.id.iv_titlebar_menu:
                /*是否是系统播单*/
                if (getIntent().getStringExtra(EXTRA_BROADCASTID).startsWith("-")) {
                    /*是否是待读*/
                    if ("-1".equals(getIntent().getStringExtra(EXTRA_BROADCASTID))) {
                        mBottomTingDialog.setItemModle(isTopIntent == 0 ? new BottomTingItemModle(Const.BottomDialogItem.SET_TOP,
                                        getResources().getDrawable(R.mipmap.icon_set_top))
                                        : new BottomTingItemModle(Const.BottomDialogItem.CANEL_TOP,
                                        getResources().getDrawable(R.mipmap.icon_cancel_top))
                                , new BottomTingItemModle(Const.BottomDialogItem.BATCH, getResources().getDrawable(R.mipmap.icon_batch)));
                    } else {
                        mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.BATCH,
                                getResources().getDrawable(R.mipmap.icon_batch)));
                    }
                } else if (mDetailInfo != null) {
                    /*是否是我自己的播单*/
                    if (mDetailInfo.getCreateUserId().equals(ContentManager.getInstance().getUserInfo().getUserId())) {
                        mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.EDIT_BROADCAST,
                                        getResources().getDrawable(R.mipmap.icon_edit_broadcast))
                                , mDetailInfo.getTop() == 0 ? new BottomTingItemModle(Const.BottomDialogItem.SET_TOP,
                                        getResources().getDrawable(R.mipmap.icon_set_top)) :
                                        new BottomTingItemModle(Const.BottomDialogItem.CANEL_TOP,
                                                getResources().getDrawable(R.mipmap.icon_cancel_top))
                                , new BottomTingItemModle(Const.BottomDialogItem.BATCH, getResources().getDrawable(R.mipmap.icon_batch))
                                , new BottomTingItemModle(Const.BottomDialogItem.DELETE, getResources().getDrawable(R.mipmap.icon_del)));
                    } else {
                        if (mDetailInfo.getSubscribe() == 0) {
                            mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.SUBSCRIBE,
                                            getResources().getDrawable(R.mipmap.icon_subscibe))
                                    , new BottomTingItemModle(Const.BottomDialogItem.BATCH, getResources().getDrawable(R.mipmap.icon_batch))
                                    , new BottomTingItemModle(Const.BottomDialogItem.REPORT, getResources().getDrawable(R.mipmap.icon_report)));
                        } else {
                            mBottomTingDialog.setItemModle(mDetailInfo.getTop() == 0 ? new BottomTingItemModle(Const.BottomDialogItem.SET_TOP,
                                            getResources().getDrawable(R.mipmap.icon_set_top)) :
                                            new BottomTingItemModle(Const.BottomDialogItem.CANEL_TOP,
                                                    getResources().getDrawable(R.mipmap.icon_cancel_top))
                                    , new BottomTingItemModle(Const.BottomDialogItem.CANCEL_SUBSCRIBE,
                                            getResources().getDrawable(R.mipmap.icon_cancel_subscibe))
                                    , new BottomTingItemModle(Const.BottomDialogItem.BATCH, getResources().getDrawable(R.mipmap.icon_batch))
                                    , new BottomTingItemModle(Const.BottomDialogItem.REPORT, getResources().getDrawable(R.mipmap.icon_report)));
                        }
                    }
                }
                mBottomTingDialog.show();
                break;
            default:
        }
    }

    @Override
    public void onItemCollect(BroadcastInfo info) {
        if (info.getStore() == 0) {
            addStore(info.getArticleId());
        } else {
            delStore(info);
        }
    }

    @Override
    public void onItemAddTO(BroadcastInfo info) {
        Intent intent = new Intent(this, BroadcastItemAddActivity.class);
        intent.putExtra(BroadcastItemAddActivity.ARTICLE_IDS_EXTRA, info.getArticleId());
        startActivity(intent);
    }

    private void addStore(String id) {
        AddStoreRequest request = new AddStoreRequest();
        request.setArticleId(id);
        request.doSign();
        mAddStorePresenter.addStore(request);
    }

    private void delStore(BroadcastInfo info) {
        ArticleIdsRequest request = new ArticleIdsRequest();
        request.setArticleIds(info.getArticleId());
        request.doSign();
        mDelStorePreesenter.delStore(request);
    }

    @Override
    public void onItemDel(BroadcastInfo info) {

    }

    @Override
    public void onAddStoreSuccess(BaseResponse response) {
        initList();
    }

    @Override
    public void onAddStoreError(int code, String errorMsg) {

    }

    @Override
    public void onDelStoreSuccess(BaseResponse response) {
        initList();
    }

    @Override
    public void onDelStoreError(int code, String errorMsg) {

    }

    @Override
    public void onBottomTingItemClick(BottomTingItemModle modle) {
        switch (modle.getName()) {
            case Const.BottomDialogItem.SET_TOP:
                setTop(SetTopRequest.EVENT.TOP.name().toLowerCase());
                break;
            case Const.BottomDialogItem.CANEL_TOP:
                setTop(SetTopRequest.EVENT.CANCEL.name().toLowerCase());
                break;
            case Const.BottomDialogItem.SUBSCRIBE:
                subscribe(SubscribeRequest.EVENT.SUBSCRIBE.name().toLowerCase());
                break;
            case Const.BottomDialogItem.CANCEL_SUBSCRIBE:
                subscribe(SubscribeRequest.EVENT.CANCEL.name().toLowerCase());
                break;
            case Const.BottomDialogItem.EDIT_BROADCAST:
                if (mDetailInfo != null) {
                    Intent intent = new Intent(this, BroadcastEditActivity.class);
                    intent.putExtra(BroadcastEditActivity.BROADCAST_ID, mDetailInfo.getBroadcastId());
                    intent.putExtra(BroadcastEditActivity.BROADCAST_NAME, mDetailInfo.getName());
                    intent.putExtra(BroadcastEditActivity.BROADCAST_INTRO, mDetailInfo.getInfo());
                    startActivity(intent);
                }
                break;
            case Const.BottomDialogItem.BATCH:
                break;
            case Const.BottomDialogItem.REPORT:
                break;
            case Const.BottomDialogItem.DELETE:
                delBroadcast();
                break;
            default:
        }
    }

    private void delBroadcast() {
        BroadcastIdRequest request = new BroadcastIdRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.doSign();
        mBroadcastAllDelPresenter.delBroadcast(request);
    }

    private void setTop(String event) {
        SetTopRequest request = new SetTopRequest();
        request.setBroadcastId(getIntent().getStringExtra(EXTRA_BROADCASTID));
        request.setEvent(event);
        request.doSign();
        mSetTopPresenter.setTop(request);
    }

    private void subscribe(String event) {
        SubscribeRequest request = new SubscribeRequest();
        request.setBroadcastId(mDetailInfo.getBroadcastId());
        request.setEvent(event);
        request.doSign();
        mSubscribePresenter.subscribe(request);
    }


    @Override
    public void onSetTopSuccess(BaseResponse response) {
        if (mDetailInfo != null) {
            mDetailInfo.setTop(mDetailInfo.getTop() ^ 1);
        } else if ("-1".equals(getIntent().getStringExtra(EXTRA_BROADCASTID))) {
            isTopIntent ^= 1;
        }
        EventBus.getDefault().post(new TingRefreshEvent());
    }

    @Override
    public void onSetTopError(int code, String errorMsg) {

    }

    @Override
    public void onSubscribeSuccess(BaseResponse response, String event) {
        mDetailInfo.setSubscribe(mDetailInfo.getSubscribe() ^ 1);
        EventBus.getDefault().post(new TingRefreshEvent());
    }

    @Override
    public void onSubscribeError(int code, String errorMsg, String event) {

    }

    @Override
    public void onBroadcastAllDelSuccess(BaseResponse response, BroadcastInfo info) {
        if (info != null) {
            mAdapter.notifyItemRemoved(info.getPositin());
        } else {
            EventBus.getDefault().post(new TingRefreshEvent());
            this.finish();
        }
    }

    @Override
    public void onBroadcastAllDelError(int code, String errorMsg) {

    }
}
