package cn.xylink.mting.speech.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

import cn.xylink.mting.utils.DensityUtil;


public class PanelViewPager extends ViewPager {

    static String TAG = PanelViewPager.class.getSimpleName();

    WeakReference<Context> contextWeakReference;
    public PanelViewPager(Context context) {
        super(context);

        contextWeakReference = new WeakReference<>(context);
    }

    public PanelViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        contextWeakReference = new WeakReference<>(context);
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


    float downPosX;
    float downPosY;
    float upPosX;
    float upPosY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "up slip start");
                downPosX = event.getX();
                downPosY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

                upPosX = event.getX();
                upPosY = event.getY();

                float lineDistance = (float)Math.sqrt(Math.pow(downPosY - upPosY, 2) + Math.pow(downPosX - upPosX, 2));
                float slipSlope = Math.abs(( downPosY - upPosY ) / (downPosX - upPosX));
                Log.d(TAG, "up slip end:line" + lineDistance + ", slipSlop:" + slipSlope);
                if(downPosY - upPosY > 0 && lineDistance > DensityUtil.dip2px(contextWeakReference.get(), 80)) {
                    if(slipSlope > 1) {
                        Log.d(TAG, "discrepency the up slip, the slope is:" + slipSlope);
                        contextWeakReference.get().sendBroadcast(new Intent("SPEECH_ACTION_NEXT"));
                        return false;
                    }
                }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
