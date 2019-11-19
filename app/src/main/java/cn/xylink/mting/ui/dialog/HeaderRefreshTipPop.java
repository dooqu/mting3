package cn.xylink.mting.ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.xylink.mting.R;
import cn.xylink.mting.utils.DensityUtil;

/**
 * -----------------------------------------------------------------
 * 2019/11/19 11:55 : Create HeaderRefreshTipPop.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class HeaderRefreshTipPop  extends PopupWindow {
    private TextView mView;
    public HeaderRefreshTipPop(Context context) {
        super(context);
        mView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mView.setLayoutParams(layoutParams);
        mView.setGravity(Gravity.CENTER);
        mView.setText("已更新到最新");
        mView.setTextColor(context.getResources().getColor(R.color.white));
        setContentView(mView);
        this.setFocusable(false);
        this.setTouchable(false);
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(context.getDrawable(R.color.c488def));
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(DensityUtil.dip2pxComm(context, 25f));
//        this.setAnimationStyle(R.style.anim_pop);
    }

    public HeaderRefreshTipPop setText(String str){
        return this;
    }
    public HeaderRefreshTipPop setBackground(Drawable drawable){
        return this;
    }
   public HeaderRefreshTipPop setTextColor(int color){
        return this;
    }


}
