package cn.xylink.mting.ui.activity;

import android.content.Intent;
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
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.BroadcastItemAddInfo;
import cn.xylink.mting.bean.BroadcastItemAddRequest;
import cn.xylink.mting.contract.BroadcastItemAddContact;
import cn.xylink.mting.presenter.BroadcastItemAddPresenter;
import cn.xylink.mting.ui.adapter.BroadcastItemAddAdapter;
import cn.xylink.mting.utils.L;

/**
 * @author wjn
 * @date 2019/11/25
 */
public class BroadcastItemAddActivity extends BasePresenterActivity implements BroadcastItemAddContact.IBroadcastItemAddView, BroadcastItemAddAdapter.OnItemClickListener {
    public static String ARTICLE_IDS_EXTRA = "articleIds";

    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.btn_left)
    ImageButton btnLeft;
    @BindView(R.id.rv_broadcast_list)
    RecyclerView mRecyclerView;
    private BroadcastItemAddAdapter mAdapter;
    private BroadcastItemAddPresenter mBroadcastItemAddPresenter;
    private List<BroadcastItemAddInfo> mDataBean;
    private String broadcastId;
    private String articleIds;

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
        initList();
        mAdapter.setmOnItemClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        articleIds = intent.getStringExtra(ARTICLE_IDS_EXTRA);
    }

    private void initList() {
        BaseRequest request = new BaseRequest();
        request.doSign();
        mBroadcastItemAddPresenter.getBroadcastItemAddList(request);
    }

    @Override
    protected void initTitleBar() {
        btnLeft.setVisibility(View.INVISIBLE);
        tvRight.setText("关闭");
        tvRight.setTextColor(getResources().getColor(R.color.c333333));
        tvTitle.setText("添加到播单");
    }

    @Override
    public void onBroadcastItemAddListSuccess(List<BroadcastItemAddInfo> data) {
        this.mDataBean = data;
        if (null != data && data.size() > 0) {
            mAdapter.clearData();
            mAdapter.setData(data);
        }
    }

    @Override
    public void onBroadcastItemAddListError(int code, String errorMsg) {

    }

    @Override
    public void onBroadcastItemAddSuccess(BaseResponse baseResponse) {

    }

    @Override
    public void onBroadcastItemAddError(int code, String errorMsg) {

    }

    @Override
    public void onItemClick(int position) {
        if (null != mDataBean && mDataBean.size() > 0) {
            broadcastId = mDataBean.get(position).getBroadcastId();
            L.v(broadcastId);
            doAddBroadcastItem();
        }

    }

    private void doAddBroadcastItem() {
        BroadcastItemAddRequest request = new BroadcastItemAddRequest();
        request.setBroadcastId(broadcastId);
        request.setArticleIds(articleIds);
        request.doSign();
        mBroadcastItemAddPresenter.getBroadcastItemAdd(request);
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
