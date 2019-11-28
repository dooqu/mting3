package cn.xylink.mting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import cn.xylink.mting.utils.DensityUtil;

/**
 * -----------------------------------------------------------------
 * 2019/11/28 17:42 : Create HDividerItemDecoration.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class HDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mLine;
    private Context mContext;

    public HDividerItemDecoration(Context context) {
        mContext = context;
        int[] attrs = new int[]{android.R.attr.listDivider};
        TypedArray a = context.obtainStyledAttributes(attrs);
        mLine = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount-1; i++) {
            View child = parent.getChildAt(i);
            int left = child.getLeft();
            int top = child.getBottom();
            int right = child.getRight();
            int bottom = top + mLine.getIntrinsicHeight();
            mLine.setBounds(left+DensityUtil.dip2pxComm(mContext,16), top, right-DensityUtil.dip2pxComm(mContext,16), bottom);
            mLine.draw(c);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, DensityUtil.dip2pxComm(mContext,1));
    }
}
