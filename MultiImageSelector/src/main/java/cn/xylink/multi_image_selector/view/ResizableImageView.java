package cn.xylink.multi_image_selector.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class ResizableImageView extends android.support.v7.widget.AppCompatImageView {
    public ResizableImageView(Context context) {
        super(context);
    }

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Drawable d = getDrawable();
        if(d != null)
        {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() /(float) d.getIntrinsicWidth());
            setMeasuredDimension(width,height);
        }else{
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }
}
