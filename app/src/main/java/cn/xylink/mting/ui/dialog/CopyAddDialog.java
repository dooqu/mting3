package cn.xylink.mting.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.LinkArticle;
import cn.xylink.mting.bean.LinkCreateRequest;
import cn.xylink.mting.contract.LinkCreateContact;
import cn.xylink.mting.presenter.LinkCreatePresenter;

/**
 *检测黏贴板
 *
 * -----------------------------------------------------------------
 * 2019/7/22 18:09 : Create CopyAddDialog.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class CopyAddDialog extends BaseDimDialog implements LinkCreateContact.IPushView {

    @BindView(R.id.ll_copy_add_content)
    LinearLayout mContentLayout;
    @BindView(R.id.ll_copy_add_loading)
    LinearLayout mLoadingLayout;
    @BindView(R.id.tv_copy_add_title)
    TextView mTitleView;
    @BindView(R.id.tv_copy_add_contact)
    TextView mContactView;
    @BindView(R.id.tv_copy_add_add_unread)
    TextView mAddUnreadView;
    @BindView(R.id.tv_copy_add_play)
    TextView mPlayView;
    @BindView(R.id.iv_copy_add_close)
    ImageView mCloseView;
    private LinkCreatePresenter mLinkCreatePresenter;
    private String mUrl;

    public CopyAddDialog(Context context, String url) {
        super(context);
        mLinkCreatePresenter = new LinkCreatePresenter();
        mLinkCreatePresenter.attachView(this);
        mUrl = url;
        mContactView.setText(url);
    }

    @Override
    protected View getLayout() {
        return View.inflate(mContext, R.layout.dialog_copy_add, null);
    }

    @OnClick({R.id.ll_copy_add_write, R.id.ll_copy_add_layout, R.id.iv_copy_add_close, R.id.tv_copy_add_play, R.id.tv_copy_add_add_unread})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_copy_add_add_unread:
                addUnread();
                break;
            case R.id.tv_copy_add_play:
                isPlay = true;
                addUnread();
                break;
            case R.id.iv_copy_add_close:
            case R.id.ll_copy_add_layout:
                this.dismiss();
                break;
        }
    }

    private boolean isPlay;

    private void addUnread() {
        mContentLayout.setVisibility(View.INVISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);

        LinkCreateRequest request = new LinkCreateRequest();
        request.setUrl(mUrl);
        request.setInType(1);
        request.doSign();
        mLinkCreatePresenter.onPush(request);
    }

    private LinkArticle mLinkArticle;

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void dismiss() {
        mLinkCreatePresenter.deatchView();
        super.dismiss();
    }

    @Override
    public void onPushSuccess(BaseResponse<LinkArticle> loginInfoBaseResponse) {
        mLinkArticle = loginInfoBaseResponse.data;
        if (isPlay) {
            Toast.makeText(mContext, "开始朗读文章", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "已添加到待读", Toast.LENGTH_SHORT).show();
        }
        this.dismiss();
    }

    @Override
    public void onPushError(int code, String errorMsg) {
        mLoadingLayout.setVisibility(View.GONE);
        Toast.makeText(mContext, "文章加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
//        if (!TextUtils.isEmpty(errorMsg))
//            mTitleView.setText(errorMsg);
        mPlayView.setEnabled(true);
        mAddUnreadView.setEnabled(true);
        this.dismiss();
    }
}
