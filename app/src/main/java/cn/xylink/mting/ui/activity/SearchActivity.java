package cn.xylink.mting.ui.activity;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.SearchInfo;
import cn.xylink.mting.bean.SearchRequest;
import cn.xylink.mting.contract.SearchContact;
import cn.xylink.mting.presenter.SearchPresenter;
import cn.xylink.mting.ui.adapter.SearchAdapter;
import cn.xylink.mting.widget.EndlessRecyclerOnScrollListener;

public class SearchActivity extends BasePresenterActivity implements SearchContact.ISearchView {

    private SearchPresenter mSearchPresenter;
    @BindView(R.id.rv_search)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private boolean isSearchArticle = true;
    private SearchAdapter mAdapter;
    @BindView(R.id.et_search)
    EditText mEditView;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        mSearchPresenter = (SearchPresenter) createPresenter(SearchPresenter.class);
        mSearchPresenter.attachView(this);
        mRecyclerView.setItemAnimator(null);
        mAdapter = new SearchAdapter(this);
//        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(endlessScrollListener);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
//            loadMoreData();
        });
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mEditView.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEditView, 0);
        }, 300);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {

    }

    private void loadData(int page) {
        if (!TextUtils.isEmpty(mEditView.getText().toString().trim())) {
            showLoading();
            SearchRequest request = new SearchRequest();
            request.setQuery(mEditView.getText().toString().trim());
            request.setPage(page);
            request.doSign();
            if (isSearchArticle) {
                mSearchPresenter.searchArticle(request);
            } else {
                mSearchPresenter.searchBroadcast(request);
            }
        }
    }


    @OnCheckedChanged({R.id.rb_search_article, R.id.rb_search_broadcast})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_search_article:
                    if (!isSearchArticle) {
                        mAdapter.clearData();
                        isSearchArticle = true;
                        loadData(1);
                    }
                    isSearchArticle = true;

                    break;
                case R.id.rb_search_broadcast:
                    if (isSearchArticle) {
                        mAdapter.clearData();
                        isSearchArticle = false;
                        loadData(1);
                    }
                    isSearchArticle = false;
                    break;
                default:
                    break;
            }
            mAdapter.setSearchArticle(isSearchArticle);
        }
    }


    @OnEditorAction(R.id.et_search)
    boolean onEditorAction(KeyEvent key) {
        if (!TextUtils.isEmpty(mEditView.getText().toString())) {
            mAdapter.clearData();
            loadData(1);
        }
        return true;
    }

    @OnClick(R.id.tv_search_cancel)
    void onClick(View v){
        this.finish();
    }

    @Override
    public void onSearchArticleSuccess(List<SearchInfo> response) {
        hideLoading();
        mAdapter.setData(response);
    }

    @Override
    public void onSearchArticleError(int code, String errorMsg) {
        hideLoading();
    }

    @Override
    public void onSearchBroadcastSuccess(List<SearchInfo> response) {
        hideLoading();
        mAdapter.setData(response);
    }

    @Override
    public void onSearchBroadcastError(int code, String errorMsg) {
        hideLoading();
    }

    private EndlessRecyclerOnScrollListener endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int current_page) {
        }
    };
}
