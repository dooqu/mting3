package cn.xylink.mting.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.base.BaseResponseArray;
import cn.xylink.mting.bean.BroadcastIdRequest;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.SetTopRequest;
import cn.xylink.mting.bean.SubscribeRequest;
import cn.xylink.mting.bean.TingInfo;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.contract.BroadcastAllDelContact;
import cn.xylink.mting.contract.SetTopContact;
import cn.xylink.mting.contract.SubscribeContact;
import cn.xylink.mting.contract.TingListContact;
import cn.xylink.mting.event.ArticleDetailScrollEvent;
import cn.xylink.mting.event.TingChangeMessageEvent;
import cn.xylink.mting.event.TingRefreshEvent;
import cn.xylink.mting.presenter.BroadcastAllDelPresenter;
import cn.xylink.mting.presenter.SetTopPresenter;
import cn.xylink.mting.presenter.SubscribePresenter;
import cn.xylink.mting.presenter.TingListPresenter;
import cn.xylink.mting.ui.activity.ArticleCreateActivity;
import cn.xylink.mting.ui.activity.BroadcastActivity;
import cn.xylink.mting.ui.activity.BroadcastCreateActivity;
import cn.xylink.mting.ui.activity.SearchActivity;
import cn.xylink.mting.ui.adapter.TingAdapter;
import cn.xylink.mting.ui.dialog.BottomTingDialog;
import cn.xylink.mting.ui.dialog.BottomTingItemModle;
import cn.xylink.mting.ui.dialog.InputDialog;
import cn.xylink.mting.ui.dialog.MainAddMenuPop;
import cn.xylink.mting.ui.dialog.SubscribeTipDialog;
import cn.xylink.mting.ui.dialog.TipDialog;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.utils.T;
import cn.xylink.mting.widget.TingHeaderView;

/**
 * @author JoDragon
 */
public class TingFragment extends BasePresenterFragment implements TingListContact.ITingListView, TingAdapter.OnItemClickListener,
        MainAddMenuPop.OnMainAddMenuListener, BottomTingDialog.OnBottomTingListener, SetTopContact.ISetTopView, SubscribeContact.ISubscribeView
        , SubscribeTipDialog.OnTipListener , BroadcastAllDelContact.IBroadcastAllDelView{

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
    private BroadcastAllDelPresenter mBroadcastAllDelPresenter;

    public static TingFragment newInstance() {
        return new TingFragment();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.ting_fragment;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTitleLayout.getLayoutParams();
        lp.topMargin = DensityUtil.getStatusBarHeight(getActivity());
        mTitleLayout.setLayoutParams(lp);
        mTingListPresenter = (TingListPresenter) createPresenter(TingListPresenter.class);
        mTingListPresenter.attachView(this);
        mSetTopPresenter = (SetTopPresenter) createPresenter(SetTopPresenter.class);
        mSetTopPresenter.attachView(this);
        mSubscribePresenter = (SubscribePresenter) createPresenter(SubscribePresenter.class);
        mSubscribePresenter.attachView(this);
        mBroadcastAllDelPresenter = (BroadcastAllDelPresenter) createPresenter(BroadcastAllDelPresenter.class);
        mBroadcastAllDelPresenter.attachView(this);
        mAdapter = new TingAdapter(getActivity(), this);
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            initData();
        });
        mRefreshLayout.setRefreshHeader(new TingHeaderView(getActivity()).setIsWrite(true));
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
        mRefreshLayout.finishRefresh(true);
    }

    @Override
    public void onItemClick(TingInfo article, int position) {
        Intent intent = new Intent(getActivity(), BroadcastActivity.class);
        intent.putExtra(BroadcastActivity.EXTRA_BROADCASTID, article.getBroadcastId());
        intent.putExtra(BroadcastActivity.EXTRA_TITLE, article.getName());
        intent.putExtra(BroadcastActivity.EXTRA_ISTOP, article.getTop());
        getActivity().startActivity(intent);
        if (article.getNewMsg() > 0) {
            article.setNewMsg(0);
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onItemLongClick(TingInfo article) {

        if ("-1".equals(article.getBroadcastId())) {
            mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.SET_TOP, Const.BottomDialogItem.CANEL_TOP,
                    getActivity().getResources().getDrawable(R.mipmap.icon_set_top),
                    getActivity().getResources().getDrawable(R.mipmap.icon_cancel_top), article.getTop() == 1, article.getBroadcastId()));
        } else if(ContentManager.getInstance().getUserInfo().getUserId().equals(article.getCreateUserId())){
            mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.SET_TOP, Const.BottomDialogItem.CANEL_TOP,
                    getActivity().getResources().getDrawable(R.mipmap.icon_set_top),
                    getActivity().getResources().getDrawable(R.mipmap.icon_cancel_top), article.getTop() == 1, article.getBroadcastId())
            ,new BottomTingItemModle(Const.BottomDialogItem.DELETE, getResources().getDrawable(R.mipmap.icon_del), article.getBroadcastId()));
        }else {
            mBottomTingDialog.setItemModle(new BottomTingItemModle(Const.BottomDialogItem.SET_TOP, Const.BottomDialogItem.CANEL_TOP,
                            getActivity().getResources().getDrawable(R.mipmap.icon_set_top),
                            getActivity().getResources().getDrawable(R.mipmap.icon_cancel_top), article.getTop() == 1, article.getBroadcastId())
                    , new BottomTingItemModle(Const.BottomDialogItem.CANCEL_SUBSCRIBE,
                            getActivity().getResources().getDrawable(R.mipmap.icon_cancel_subscibe), article.getBroadcastId()));
        }
        mBottomTingDialog.show();
    }

    @OnClick({R.id.iv_ting_menu, R.id.tv_ting_search})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_ting_menu:
                MainAddMenuPop pop = new MainAddMenuPop(getActivity(), this);
                pop.showAsRight(mMenuImageView);
                break;
            case R.id.tv_ting_search:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    /**
    * 置顶
    * */
    private void setTop(String bid, String event) {
        SetTopRequest request = new SetTopRequest();
        request.setBroadcastId(bid);
        request.setEvent(event);
        request.doSign();
        mSetTopPresenter.setTop(request);
    }

    /***
    * 订阅
    * */
    private void subscribe(String bid, String event) {
        SubscribeRequest request = new SubscribeRequest();
        request.setBroadcastId(bid);
        request.setEvent(event);
        request.doSign();
        mSubscribePresenter.subscribe(request);
    }

    @Override
    public void onCreateArticle() {
        startActivity(new Intent(getActivity(), ArticleCreateActivity.class));
    }

    @Override
    public void onPut() {
        InputDialog dialog = new InputDialog(getActivity());
        dialog.show();
    }

    @Override
    public void onCreateBroadcast() {
        startActivity(new Intent(getActivity(), BroadcastCreateActivity.class));
    }

    @Override
    public void onBottomTingItemClick(BottomTingItemModle modle) {
        if (Const.BottomDialogItem.SET_TOP.equals(modle.getName())) {
            setTop(modle.getId(), modle.isTwo() ? SetTopRequest.EVENT.CANCEL.name().toLowerCase() : SetTopRequest.EVENT.TOP.name().toLowerCase());
        } else if (Const.BottomDialogItem.CANCEL_SUBSCRIBE.equals(modle.getName())) {
            TipDialog dialog = new TipDialog(getActivity());
            dialog.setMsg("不再订阅此播单？", "取消", "确定", new TipDialog.OnTipListener() {
                @Override
                public void onLeftClick() {

                }

                @Override
                public void onRightClick() {
                    subscribe(modle.getId(), SubscribeRequest.EVENT.CANCEL.name().toLowerCase());
                }
            });
            dialog.show();
        } else if (Const.BottomDialogItem.DELETE.equals(modle.getName())){
            SubscribeTipDialog dialog1 = new SubscribeTipDialog(getActivity());
            dialog1.setMsg("播单删除确认", "播单删除后，播单内的文章也会被删除。", new SubscribeTipDialog.OnTipListener() {
                @Override
                public void onLeftClick(Object tag) {

                }

                @Override
                public void onRightClick(Object tag) {
                    delBroadcast(modle.getId());
                }
            });
            dialog1.show();
        }
    }

    /**
     * 删除播单
     */
    private void delBroadcast(String id) {
        BroadcastIdRequest request = new BroadcastIdRequest();
        request.setBroadcastId(id);
        request.doSign();
        mBroadcastAllDelPresenter.delBroadcast(request);
    }

    @Override
    public void onSetTopSuccess(BaseResponse response, String event) {
        if (event.equals(SetTopRequest.EVENT.TOP.name().toLowerCase())) {
            T.showCustomCenterToast("置顶成功");
        } else {
            T.showCustomCenterToast("取消置顶成功");
        }
        initData();
    }

    @Override
    public void onSetTopError(int code, String errorMsg, String event) {
        if (event.equals(SetTopRequest.EVENT.TOP.name().toLowerCase())) {
            T.showCustomCenterToast("置顶失败");
        } else {
            T.showCustomCenterToast("取消置顶失败");
        }
    }

    @Override
    public void onSubscribeSuccess(BaseResponse response, String event) {
        initData();
        if (event.equals(SubscribeRequest.EVENT.SUBSCRIBE.name().toLowerCase())) {
            T.showCustomCenterToast("订阅成功");
        } else if (event.equals(SubscribeRequest.EVENT.SUBSCRIBE.name().toLowerCase())) {
            T.showCustomCenterToast("取消订阅成功");
        }
    }

    @Override
    public void onSubscribeError(int code, String errorMsg, String event) {
        if (event.equals(SubscribeRequest.EVENT.SUBSCRIBE.name().toLowerCase())) {
            T.showCustomCenterToast("订阅失败");
        } else if (event.equals(SubscribeRequest.EVENT.SUBSCRIBE.name().toLowerCase())) {
            T.showCustomCenterToast("取消订阅失败");
        }
    }

    @Override
    public void onLeftClick(Object tag) {
    }

    @Override
    public void onRightClick(Object tag) {
        BottomTingItemModle modle = (BottomTingItemModle) tag;
        subscribe(modle.getId(), SubscribeRequest.EVENT.CANCEL.name().toLowerCase());

    }

    @Subscribe
    public void eventRefresh(TingRefreshEvent event) {
        initData();
    }

    @Subscribe
    public void eventChangeMessage(TingChangeMessageEvent event) {
        mAdapter.changeMessage(event.getBroadcastId(), event.getMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mRecyclerView.clearOnScrollListeners();
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 2) {
                ArticleDetailScrollEvent event = new ArticleDetailScrollEvent("upGlide");
                event.setActivity(getActivity());
                EventBus.getDefault().post(event);
            } else if (dy < -2) {
                ArticleDetailScrollEvent event = new ArticleDetailScrollEvent("glide");
                event.setActivity(getActivity());
                EventBus.getDefault().post(event);
            }
        }
    };

    @Override
    public void onBroadcastAllDelSuccess(BaseResponse response, BroadcastInfo info) {
        T.showCustomCenterToast("删除成功");
        initData();
    }

    @Override
    public void onBroadcastAllDelError(int code, String errorMsg) {
        T.showCustomCenterToast("删除失败");
    }
}
