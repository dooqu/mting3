package cn.xylink.mting.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;

/**
 * @author wjn
 * @date 2019/12/18
 */
public class BottomAccountLogoutDialog extends BaseDimDialog {
    @BindView(R.id.btn_out_app)
    Button btnOutApp;
    @BindView(R.id.btn_out_account)
    Button btnOutAccount;


    private OnBottomSelectDialogListener mListener;

    public BottomAccountLogoutDialog(Context context) {
        super(context);
    }

    public void onClickListener(OnBottomSelectDialogListener listener) {
        this.mListener = listener;
    }

    @Override
    protected View getLayout() {
        return View.inflate(mContext, R.layout.dialog_bottom_account_logout, null);
    }

    @OnClick({R.id.btn_out_account, R.id.btn_out_app, R.id.v_top})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_out_app:
                mListener.onFirstClick();
                BottomAccountLogoutDialog.this.dismiss();
                break;
            case R.id.btn_out_account:
                mListener.onSecondClick();
                BottomAccountLogoutDialog.this.dismiss();
                break;
            case R.id.v_top:
                BottomAccountLogoutDialog.this.dismiss();
                break;
        }

    }

    public interface OnBottomSelectDialogListener {
        void onFirstClick();

        void onSecondClick();
    }

}
