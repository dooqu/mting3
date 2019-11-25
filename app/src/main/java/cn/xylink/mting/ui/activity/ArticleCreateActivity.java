package cn.xylink.mting.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.base.BaseResponse;
import cn.xylink.mting.bean.ArticleCreateInputInfo;
import cn.xylink.mting.bean.ArticleCreateInputRequest;
import cn.xylink.mting.contract.ArticleCreateContact;
import cn.xylink.mting.presenter.ArticleCreatePresenter;
import cn.xylink.mting.widget.EditTextWidthClear;

/**
 * @author wjn
 * @date 2019/11/22
 */
public class ArticleCreateActivity extends BasePresenterActivity implements TextWatcher, ArticleCreateContact.IArticleCreateView {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_title_article)
    EditTextWidthClear etTitle;
    @BindView(R.id.et_content_article)
    EditText etContent;
    private ArticleCreatePresenter articleCreatePresenter;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_article_create);
    }

    @Override
    protected void initView() {
        articleCreatePresenter = (ArticleCreatePresenter) createPresenter(ArticleCreatePresenter.class);
        articleCreatePresenter.attachView(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("创建文章");
        tvRight.setText("完成");
        etTitle.addTextChangedListener(this);
        etContent.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!TextUtils.isEmpty(etContent.getText().toString()) && !TextUtils.isEmpty(etTitle.getText().toString())) {
            tvRight.setTextColor(getResources().getColor(R.color.c488def));
        } else {
            tvRight.setTextColor(getResources().getColor(R.color.cccccc));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @OnClick({R.id.btn_left, R.id.tv_right})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_right:
                if (!TextUtils.isEmpty(etTitle.getText().toString()) && !TextUtils.isEmpty(etContent.getText().toString())) {
                    doCreateArticle();
                }
                break;
        }
    }

    private void doCreateArticle() {
        ArticleCreateInputRequest request = new ArticleCreateInputRequest();
        request.setTitle(etTitle.getText().toString());
        request.setContent(etContent.getText().toString());
        request.doSign();
        articleCreatePresenter.onArticleCreate(request);
    }

    @Override
    public void onArticleCreateSuccess(BaseResponse<ArticleCreateInputInfo> baseResponse) {
        if (baseResponse.code == 200) {
            ArticleCreateActivity.this.finish();
        } else {
            toastShort(baseResponse.message);
        }


    }

    @Override
    public void onArticleCreateError(int code, String errorMsg) {

    }
}
