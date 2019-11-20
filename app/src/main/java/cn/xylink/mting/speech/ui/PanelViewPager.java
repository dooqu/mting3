package cn.xylink.mting.speech.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class PanelViewPager extends ViewPager {
    public PanelViewPager(Context context) {
        super(context);
    }

    public PanelViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int height = 0;
        for(int i = 0; i < childCount; i++) {
            getChildAt(i).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int childHeight = getChildAt(i).getMeasuredHeight();
            if(childHeight > height) {
                height = childHeight;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
