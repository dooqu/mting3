package cn.xylink.mting.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import cn.xylink.mting.R;

/**
 * -----------------------------------------------------------------
 * 2019/12/6 11:16 : Create WarningTipDialog.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class WarningTipDialog implements View.OnClickListener, DialogInterface.OnDismissListener {
    private AlertDialog alertDialog;
    AlertDialog.Builder builder;
    private TextView msgTextView;
    private Context mContext;

    public WarningTipDialog(@NonNull Context context) {
        mContext = context;
        builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_warning_tip, null);
        msgTextView = view.findViewById(R.id.tv_warning);
        builder.setView(view);
        builder.setOnDismissListener(this::onDismiss);
        alertDialog = builder.create();
        view.findViewById(R.id.tv_warning_sure).setOnClickListener(this);
    }

    public void setMsg(String msg) {
        msgTextView.setText(msg);
    }

    public void show() {
        alertDialog.show();
    }

    public void dissmiss() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    public boolean isShowing() {
        return alertDialog != null ? alertDialog.isShowing() : false;
    }

    @Override
    public void onClick(View v) {
        dissmiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ((Activity) mContext).finish();
    }
}