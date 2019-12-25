package cn.xylink.mting.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseRequest;
import cn.xylink.mting.bean.BroadcastAllListInfo;
import cn.xylink.mting.contract.BroadcastAllListContact;
import cn.xylink.mting.event.BroadcastRefreshEvent;
import cn.xylink.mting.presenter.BroadcastAllListPresenter;
import cn.xylink.mting.ui.adapter.SelectBroadcastAdapter;

/**
 * @author JoDragon
 */
public class SelectBroadcastActivity extends BasePresenterActivity implements BroadcastAllListContact.IBroadcastAllListView,
        SelectBroadcastAdapter.OnItemClickListener {

    @BindView(R.id.rv_select_broadcast)
    RecyclerView mRecyclerView;
    private BroadcastAllListPresenter mPresenter;
    private SelectBroadcastAdapter mAdapter;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_select_broadcast);
    }

    @Override
    protected void initView() {
        mPresenter = (BroadcastAllListPresenter) createPresenter(BroadcastAllListPresenter.class);
        mPresenter.attachView(this);
        mAdapter = new SelectBroadcastAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        BaseRequest request = new BaseRequest();
        request.doSign();
        mPresenter.getAllList(request);
    }

    @OnClick(R.id.tv_select_broadcast)
    void onClick(View v) {
        this.finish();
    }

    @Override
    public void onBroadcastAllListSuccess(List<BroadcastAllListInfo> infos) {
        if (infos != null && infos.size() > 0) {
            mAdapter.setData(infos);
        }
    }

    @Override
    public void onBroadcastAllListError(int code, String errorMsg) {

    }

    @Override
    public void onItemClick(BroadcastAllListInfo article, int position) {
        if (article.getTotal() > 0) {
            Intent intent = new Intent(this, SelectArticleAddActivity.class);
            intent.putExtra(SelectArticleAddActivity.EXTRA_TITLE, article.getName());
            intent.putExtra(SelectArticleAddActivity.EXTRA_BROADCASTID, article.getBroadcastId());
            intent.putExtra(SelectArticleAddActivity.EXTRA_BROADCASTID_TO, getIntent().getStringExtra(SelectArticleAddActivity.EXTRA_BROADCASTID_TO));
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
