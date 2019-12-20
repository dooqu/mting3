package cn.xylink.mting.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleDetail2Info;
import cn.xylink.mting.bean.ArticleDetailRequest;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastIdRequest;
import cn.xylink.mting.bean.LinkArticle;
import cn.xylink.mting.bean.LinkCreateRequest;
import cn.xylink.mting.contract.ArticleDetailContract;
import cn.xylink.mting.contract.BroadcastDetailContact;
import cn.xylink.mting.contract.LinkCreateContact;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.presenter.ArticleDetailPresenter;
import cn.xylink.mting.presenter.BroadcastDetailPresenter;
import cn.xylink.mting.presenter.LinkCreatePresenter;
import cn.xylink.mting.ui.activity.ArticleDetailActivity;
import cn.xylink.mting.ui.activity.BasePresenterActivity;
import cn.xylink.mting.ui.activity.BroadcastActivity;
import cn.xylink.mting.utils.StringUtil;
import cn.xylink.mting.utils.T;
import cn.xylink.mting.widget.EditTextWidthClear;

/**
 * -----------------------------------------------------------------
 * 2019/12/19 10:51 : Create InputDialog.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class InputDialog extends BaseDimDialog implements BroadcastDetailContact.IBroadcastDetailView, ArticleDetailContract.IArticleDetailView
, LinkCreateContact.IPushView {
    @BindView(R.id.etwc_input)
    EditTextWidthClear mEditText;
    @BindView(R.id.tv_input_url)
    TextView mUrlTextView;
    @BindView(R.id.ll_input_copy)
    LinearLayout mCopyLayout;
    private BroadcastDetailPresenter mBroadcastDetailPresenter;
    private ArticleDetailPresenter mArticleDetailPresenter;
    private LinkCreatePresenter mLinkCreatePresenter;

    public InputDialog(Context context) {
        super(context);
    }

    @Override
    protected View getLayout() {
        return View.inflate(mContext, R.layout.dialog_input, null);
    }

    @Override
    public void initView() {
        mBroadcastDetailPresenter = new BroadcastDetailPresenter();
        mBroadcastDetailPresenter.attachView(this);
        mArticleDetailPresenter = new ArticleDetailPresenter();
        mArticleDetailPresenter.attachView(this);
        mLinkCreatePresenter = new LinkCreatePresenter();
        mLinkCreatePresenter.attachView(this);
        String copy = BasePresenterActivity.getCopy(mContext).toString();
//        String copy = "http://test.xylink.cn/broadcast/201911181650419156111592/sss";
        if (!TextUtils.isEmpty(copy)) {
            mUrlTextView.setText(copy);
        } else {
            mUrlTextView.setVisibility(View.INVISIBLE);
            mCopyLayout.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.ll_input_write, R.id.ll_input_layout, R.id.iv_input_close, R.id.ll_input_copy, R.id.tv_input_url, R.id.btn_input})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_input_close:
            case R.id.ll_input_layout:
                this.dismiss();
                break;
            case R.id.ll_input_copy:
            case R.id.tv_input_url:
                mUrlTextView.setVisibility(View.INVISIBLE);
                mCopyLayout.setVisibility(View.INVISIBLE);
                mEditText.setText(mUrlTextView.getText());
                break;
            case R.id.btn_input:
                //http://service.xylink.net/broadcast/001
                String url = StringUtil.matcherUrl(mEditText.getText().toString());
                if (!TextUtils.isEmpty(url)) {
                    if (url.startsWith(RemoteUrl.URL_BASE + "/broadcast/")) {
                        BroadcastIdRequest request = new BroadcastIdRequest();
                        request.setBroadcastId(matcherID(RemoteUrl.URL_BASE + "/broadcast/",url));
                        request.doSign();
                        mBroadcastDetailPresenter.getBroadcastDetail(request);
                    } else if (url.startsWith(RemoteUrl.URL_BASE + "/article/")) {
                        ArticleDetailRequest request = new ArticleDetailRequest();
                        request.setArticleId(matcherID(RemoteUrl.URL_BASE + "/article/",url));
                        request.doSign();
                        mArticleDetailPresenter.createArticleDetail(request);
                    } else {
                        LinkCreateRequest request =new LinkCreateRequest();
                        request.setUrl(url);
                        request.setInType(2);
                        request.doSign();
                        mLinkCreatePresenter.onPush(request);
                    }
                }
                break;
        }
    }

    private String matcherID(String head, String url) {
        Pattern p = Pattern.compile(head + "[a-zA-Z-0-9]+");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            String str = matcher.group();
            return str.replace(head,"");
        }
        return "";
    }

    @Override
    public void dismiss() {
        mBroadcastDetailPresenter.deatchView();
        mArticleDetailPresenter.deatchView();
        mLinkCreatePresenter.deatchView();
        super.dismiss();
    }

    @Override
    public void onBroadcastDetailSuccess(BroadcastDetailInfo data) {
        Intent intent = new Intent(mContext, BroadcastActivity.class);
        intent.putExtra(BroadcastActivity.EXTRA_BROADCASTID, data.getBroadcastId());
        intent.putExtra(BroadcastActivity.EXTRA_TITLE, data.getName());
        mContext.startActivity(intent);
        this.dismiss();
    }

    @Override
    public void onBroadcastDetailError(int code, String errorMsg) {
        this.dismiss();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onSuccessArticleDetail(ArticleDetail2Info info) {
        Intent intent = new Intent(mContext, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.ARTICLE_ID_DETAIL, info.getArticleId());
        mContext.startActivity(intent);
        this.dismiss();
    }

    @Override
    public void onErrorArticleDetail(int code, String errorMsg) {
        this.dismiss();
    }

    @Override
    public void onPushSuccess(BaseResponse<LinkArticle> loginInfoBaseResponse) {
        Intent intent = new Intent(mContext, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.ARTICLE_ID_DETAIL, loginInfoBaseResponse.data.getArticleId());
        mContext.startActivity(intent);
        this.dismiss();
    }

    @Override
    public void onPushError(int code, String errorMsg) {
        this.dismiss();

        switch (code){
            case -2:
                T.showCustomCenterToast("url链接无效");
                break;
            case -3:
                T.showCustomCenterToast("解析正文失败");
                break;
            case -4:
                T.showCustomCenterToast("文章正文超限,max limit 20w character");
                break;
        }
    }
}
