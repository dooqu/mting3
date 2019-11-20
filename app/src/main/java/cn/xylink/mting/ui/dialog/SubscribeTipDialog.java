package cn.xylink.mting.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import cn.xylink.mting.R;

/**
 * 订阅提示
 * -----------------------------------------------------------------
 * 2019/11/18 11:20 : Create SubscribeTipDialog.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SubscribeTipDialog implements View.OnClickListener {
    private AlertDialog alertDialog;
    AlertDialog.Builder builder;
    private TextView msgTextView;
    private TextView cancelTextView;
    private TextView sureTextView;
    private TextView titleTextView;
    private OnTipListener mListener;

    public SubscribeTipDialog(@NonNull Context context) {
        builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_tip_subscribe, null);
        msgTextView = view.findViewById(R.id.tv_tip_dialog_msg);
        cancelTextView = view.findViewById(R.id.tv_tip_dialog_cancel);
        sureTextView = view.findViewById(R.id.tv_tip_dialog_sure);
        titleTextView = view.findViewById(R.id.tv_tip_dialog_title);
        msgTextView.setOnClickListener(this);
        cancelTextView.setOnClickListener(this);
        sureTextView.setOnClickListener(this);
        builder.setView(view);
        alertDialog = builder.create();

    }

    public void setMsg(String title,String msg, OnTipListener listener) {
        msgTextView.setText(msg);
        titleTextView.setText(title);
        mListener = listener;
    }
    public void setMsg(String title,String msg, String cancel, String sure, OnTipListener listener) {
        msgTextView.setText(msg);
        cancelTextView.setText(cancel);
        sureTextView.setText(sure);
        titleTextView.setText(title);
        mListener = listener;
    }

    public void show() {
        alertDialog.show();
    }

    private Object mTag;
    public void show(Object tag) {
        alertDialog.show();
        this.mTag = tag;
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
        switch (v.getId()) {
            case R.id.tv_tip_dialog_cancel:
                if (mListener != null)
                    mListener.onLeftClick(mTag);
                break;
            case R.id.tv_tip_dialog_sure:
                if (mListener != null)
                    mListener.onRightClick(mTag);
                break;
        }
        dissmiss();
    }

    public interface OnTipListener {
        void onLeftClick(Object tag);

        void onRightClick(Object tag);
    }
}
