package cn.xylink.mting.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import cn.xylink.mting.R;
import cn.xylink.mting.ui.dialog.HeaderRefreshTipPop;
import cn.xylink.mting.ui.dialog.MainAddMenuPop;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.utils.L;


/**
 * -----------------------------------------------------------------
 * 2019/11/8 15:50 : Create TIngHeaderView.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class TingHeaderView extends LinearLayout implements RefreshHeader {
    private ImageView imageView;
    private TextView textView;
    private AnimationDrawable loadingAnim;
    private Context mContext;
    private HeaderRefreshTipPop mTipPop;

    public TingHeaderView(Context context) {
        super(context);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_refresh, this);
        imageView = view.findViewById(R.id.iv_refresh_view);
        textView = view.findViewById(R.id.tv_refresh_view);
        loadingAnim = (AnimationDrawable) context.getResources().getDrawable(R.drawable.anim_ting_refresh, null);
        imageView.setImageDrawable(loadingAnim);
        this.setMinimumHeight(1);
        mTipPop = new HeaderRefreshTipPop(mContext);
    }

    public TingHeaderView setIsWrite(boolean b) {
        if (b) {
            loadingAnim = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.anim_ting_refresh, null);
        } else {
            loadingAnim = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.anim_world_refresh, null);
        }
        imageView.setImageDrawable(loadingAnim);
        return this;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh: //下拉过程
//                textView.setVisibility(INVISIBLE);
//                imageView.setVisibility(VISIBLE);
                L.v();
                break;
            case ReleaseToRefresh: //松开刷新
                mTipPop.dismiss();
                L.v();
                break;
            case Refreshing: //loading中
                loadingAnim.start();
                L.v();
                break;
            case RefreshFinish:
                loadingAnim.stop();
//                textView.setVisibility(VISIBLE);
//                imageView.setVisibility(GONE);

                mTipPop.showAsDropDown(textView, 0, -DensityUtil.dip2pxComm(mContext, 25f)-2);
                this.postDelayed(() -> mTipPop.dismiss(),1000);
                L.v();
                break;
            case ReleaseToTwoLevel:
                L.v();
                break;
            default:
        }
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.FixedBehind;
    }

    @Override
    public void setPrimaryColors(int... colors) {
        L.v();
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        L.v();
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
//        L.v("isDragging" + isDragging + "~~~~percent" + percent + "~~~~offset" + offset + "~~~~height" + height + "~~~~maxDragHeight" +
//        maxDragHeight);
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        L.v();
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        L.v();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        L.v();
        if (success) {
        } else {
        }
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        L.v();
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}
