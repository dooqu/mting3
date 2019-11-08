package cn.xylink.mting.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zinc.jrecycleview.loadview.base.IBaseRefreshLoadView;
import com.zinc.jrecycleview.loadview.bean.MoveInfo;

import cn.xylink.mting.R;
import cn.xylink.mting.utils.L;

/**
 * 刷新view
 * -----------------------------------------------------------------
 * 2019/11/6 14:34 : Create TingRefreshView.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class TingRefreshView extends IBaseRefreshLoadView {

    private View mLoadView;
    private ImageView imageView;
    private TextView textView;
    private AnimationDrawable loadingAnim;

    public TingRefreshView(Context context) {
        super(context);
    }

    @Override
    protected void onMoving(MoveInfo moveInfo) {
        L.v();
    }

    @Override
    protected View initView(Context context) {
        L.v();
//        imageView = new ImageView(context);
//        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity= Gravity.CENTER;
//        imageView.setLayoutParams(layoutParams);
//        loadingAnim = (AnimationDrawable) context.getResources().getDrawable(R.drawable.anim_ting_refresh,null);
//        imageView.setImageDrawable(loadingAnim);
        mLoadView = LayoutInflater
                .from(context)
                .inflate(R.layout.layout_list_refresh, this, false);
        imageView = mLoadView.findViewById(R.id.iv_refresh_view);
        textView = mLoadView.findViewById(R.id.tv_refresh_view);
        loadingAnim = (AnimationDrawable) context.getResources().getDrawable(R.drawable.anim_ting_refresh,null);
        imageView.setImageDrawable(loadingAnim);
        return mLoadView;
    }

    @Override
    protected View getLoadView() {
        L.v();
        return mLoadView;
    }

    @Override
    protected void onPullToAction() {
        L.v();
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onReleaseToAction() {
//        textView.setVisibility(View.VISIBLE);
        imageView.post(() -> {
            loadingAnim.stop();
            loadingAnim.start();
        });

        L.v();
    }

    @Override
    protected void onExecuting() {
        L.v();
    }

    @Override
    protected void onDone() {
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        L.v();
    }
}
