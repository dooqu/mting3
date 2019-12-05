package cn.xylink.mting.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class SearchActivity extends BasePresenterActivity implements SearchContact.ISearchView, SearchAdapter.OnItemClickListener {

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
    private int mCurrentPage = 1;
    @BindView(R.id.ll_empty)
    LinearLayout mEmptylayout;
    @BindView(R.id.iv_empty)
    ImageView mEmptyImageView;
    @BindView(R.id.tv_empty)
    TextView mEmptyTextView;

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
        mAdapter.setListener(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(endlessScrollListener);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadMoreListener(refreshlayout -> {
            if (!isLoading)
                loadData(mCurrentPage);
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

    private boolean isLoading = false;

    private void loadData(int page) {
        if (!TextUtils.isEmpty(mEditView.getText().toString().trim())) {
            if (page == 1) {
                mCurrentPage = 1;
                showLoading();
            }
            isLoading = true;
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
                        if (mArticleData == null) {
                            loadData(1);
                        } else {
                            mAdapter.setData(mArticleData);
                        }
                    }

                    break;
                case R.id.rb_search_broadcast:
                    if (isSearchArticle) {
                        mAdapter.clearData();
                        isSearchArticle = false;
                        if (mBroadcastData == null) {
                            loadData(1);
                        } else {
                            mAdapter.setData(mBroadcastData);
                        }
                    }
                    break;
                default:
                    break;
            }
            mAdapter.setSearchArticle(isSearchArticle);
        }
    }


    @OnEditorAction(R.id.et_search)
    boolean onEditorAction(KeyEvent key) {
        if (!TextUtils.isEmpty(mEditView.getText().toString()) && key.getAction() == KeyEvent.ACTION_DOWN) {
            mArticleData = null;
            mBroadcastData = null;
            mAdapter.clearData();
            loadData(1);
        }
        return true;
    }

    @OnClick({R.id.tv_search_cancel, R.id.ll_empty})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_cancel:
                this.finish();
                break;
            case R.id.ll_empty:
                mArticleData = null;
                mBroadcastData = null;
                mAdapter.clearData();
                loadData(1);
                mEmptylayout.setEnabled(false);
                break;
            default:
        }
    }

    private List<SearchInfo> mArticleData;
    private List<SearchInfo> mBroadcastData;

    @Override
    public void onSearchArticleSuccess(List<SearchInfo> response) {
        hideLoading();
        if (mCurrentPage == 1) {
            if (response != null && response.size() == 0) {
                showSearchEmpty();
            } else {
                mEmptylayout.setVisibility(View.GONE);
            }
        }
        if (mCurrentPage > 1) {
            mRefreshLayout.finishLoadMore(true);
            if (response.size() < 20) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
        mCurrentPage++;
        if (mAdapter.getItemCount() == 0) {
            mArticleData = response;
        }
        mAdapter.setData(response);
        isLoading = false;
    }

    @Override
    public void onSearchArticleError(int code, String errorMsg) {
        hideLoading();
        isLoading = false;
        mEmptylayout.setVisibility(View.VISIBLE);
        mEmptylayout.setEnabled(true);
        mAdapter.clearData();
        mAdapter.notifyDataSetChanged();
        if (code == 9999) {
            showNetworlError();
        } else {
            showLoadFail();
        }
    }

    @Override
    public void onSearchBroadcastSuccess(List<SearchInfo> response) {
        hideLoading();
        mCurrentPage++;
        if (mAdapter.getItemCount() == 0) {
            mBroadcastData = response;
        }
        mAdapter.setData(response);
        isLoading = false;
    }

    @Override
    public void onSearchBroadcastError(int code, String errorMsg) {
        hideLoading();
        isLoading = false;
    }

    private EndlessRecyclerOnScrollListener endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int current_page) {
            if (!isLoading)
                loadData(mCurrentPage);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(endlessScrollListener);
        }
    }

    @Override
    public void onItemClick(SearchInfo article) {
        if (isSearchArticle) {
            Intent intent = new Intent(this, ArticleDetailActivity.class);
//            intent.putExtra(ArticleDetailActivity.BROADCAST_ID_DETAIL,article.getBroadcastId());
            intent.putExtra(ArticleDetailActivity.ARTICLE_ID_DETAIL, article.getArticleId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, BroadcastActivity.class);
            intent.putExtra(BroadcastActivity.EXTRA_BROADCASTID, article.getBroadcastId());
//            intent.putExtra(BroadcastActivity.EXTRA_TITLE, article.getName());
//            intent.putExtra(BroadcastActivity.EXTRA_ISTOP, article.getTop());
            startActivity(intent);
        }
    }

    private void showLoadFail() {
        mEmptylayout.setVisibility(View.VISIBLE);
        mEmptyImageView.setImageResource(R.mipmap.bg_load_fail);
        mEmptyTextView.setText("加载失败");
    }

    private void showNetworlError() {
        mEmptylayout.setVisibility(View.VISIBLE);
        mEmptyImageView.setImageResource(R.mipmap.bg_network_error);
        mEmptyTextView.setText("网络开小差了，等会试试吧");
    }

    private void showSearchEmpty() {
        mEmptylayout.setVisibility(View.VISIBLE);
        mEmptyImageView.setImageResource(R.mipmap.bg_empty_search);
        mEmptyTextView.setText("没有找到内容，换个关键词试试？");
    }

}
