package cn.xylink.mting.ui.activity;

import android.content.Intent;
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
import cn.xylink.mting.bean.ArticleEditRequest;
import cn.xylink.mting.contract.ArticleEditContact;
import cn.xylink.mting.presenter.ArticleEditPresenter;
import cn.xylink.mting.widget.EditTextWidthClear;

/**
 * @author wjn
 * @date 2019/11/29
 */
public class ArticleEditActivity extends BasePresenterActivity implements TextWatcher, ArticleEditContact.IArticleEditView {
    @BindView(R.id.tv_include_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_title_article_edit)
    EditTextWidthClear etTitle;
    @BindView(R.id.et_content_article_edit)
    EditText etContent;
    private ArticleEditPresenter mArticleEditPresenter;

    public static String ARTICLE_ID_EDIT = "ARTICLE_ID_EDIT";
    public static String ARTICLE_TITLE_EDIT = "ARTICLE_TITLE_EDIT";
    public static String ARTICLE_CONTENT_EDIT = "ARTICLE_CONTENT_EDIT";
    private String articleId;
    private String title;
    private String content;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_article_edit);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        articleId = intent.getStringExtra(ARTICLE_ID_EDIT);
        title = intent.getStringExtra(ARTICLE_TITLE_EDIT);
        content = intent.getStringExtra(ARTICLE_CONTENT_EDIT);
        etContent.setText(content);
        etTitle.setText(title);
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            etTitle.requestFocus();
            etTitle.setSelection(etTitle.getText().toString().length());
        } else {
            etContent.requestFocus();
            etContent.setSelection(etContent.getText().toString().length());
        }
        mArticleEditPresenter = (ArticleEditPresenter) createPresenter(ArticleEditPresenter.class);
        mArticleEditPresenter.attachView(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {
        tvTitle.setText("编辑文章");
        tvRight.setText("完成");
        tvRight.setTextColor(getResources().getColor(R.color.c488def));
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
                    doEditArticle();
                }
                break;
        }
    }

    private void doEditArticle() {
        ArticleEditRequest request = new ArticleEditRequest();
        request.setArticleId(articleId);
        request.setTitle(etTitle.getText().toString());
        request.setContent(etContent.getText().toString());
        request.doSign();
        mArticleEditPresenter.onArticleEdit(request);
        showLoading();
    }

    @Override
    public void onArticleEditSuccess(BaseResponse<String> baseResponse) {
        hideLoading();
        ArticleEditActivity.this.finish();
    }

    @Override
    public void onArticleEditError(int code, String errorMsg) {

    }
}
