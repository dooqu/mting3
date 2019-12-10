package cn.xylink.mting.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;

/**
 * @author wjn
 * @date 2019/12/09
 */
public class BottomArticleReportDialog extends BaseDimDialog {
    @BindView(R.id.et_content)
    EditText mContent;
    @BindView(R.id.ll_report1)
    LinearLayout mLLReport1;
    @BindView(R.id.ll_report2)
    LinearLayout mLLReport2;


    private OnBottomSelectDialogListener mListener;

    public BottomArticleReportDialog(Context context) {
        super(context);
    }

    public void onClickListener(OnBottomSelectDialogListener listener) {
        this.mListener = listener;
    }

    @Override
    protected View getLayout() {
        return View.inflate(mContext, R.layout.dialog_bottom_article_report, null);
    }

    @OnClick({R.id.tv_vulgar, R.id.tv_illegal, R.id.tv_untrue, R.id.tv_other, R.id.btn_finish, R.id.btn_commit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_vulgar:
                mListener.onFirstClick();
                BottomArticleReportDialog.this.dismiss();
                break;
            case R.id.tv_illegal:
                mListener.onSecondClick();
                BottomArticleReportDialog.this.dismiss();
                break;
            case R.id.tv_untrue:
                mListener.onThirdClick();
                BottomArticleReportDialog.this.dismiss();
                break;
            case R.id.tv_other:
                mLLReport1.setVisibility(View.GONE);
                mLLReport2.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_finish:
                BottomArticleReportDialog.this.dismiss();
                break;
            case R.id.btn_commit:
//                if (TextUtils.isEmpty(mContent.getText().toString())) {
//                } else {
                mListener.onCommitClick(mContent.getText().toString());
//                }
                mLLReport1.setVisibility(View.VISIBLE);
                mLLReport2.setVisibility(View.GONE);
                BottomArticleReportDialog.this.dismiss();
                break;
        }

    }

    public interface OnBottomSelectDialogListener {
        void onFirstClick();

        void onSecondClick();

        void onThirdClick();

        void onCommitClick(String content);
    }

}
