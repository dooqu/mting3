package cn.xylink.mting.widget;

import android.content.Context;
import android.view.View;

import com.zinc.jrecycleview.loadview.base.IBaseRefreshLoadView;
import com.zinc.jrecycleview.loadview.bean.MoveInfo;

/**
 * 刷新view
 * -----------------------------------------------------------------
 * 2019/11/6 14:34 : Create TingRefreshView.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class TingRefreshView extends IBaseRefreshLoadView {

    public TingRefreshView(Context context) {
        super(context);
    }

    @Override
    protected void onMoving(MoveInfo moveInfo) {

    }

    @Override
    protected View getLoadView() {
        return null;
    }

    @Override
    protected void onPullToAction() {

    }

    @Override
    protected void onReleaseToAction() {

    }

    @Override
    protected void onExecuting() {

    }

    @Override
    protected void onDone() {

    }

    @Override
    protected View initView(Context context) {
        return null;
    }
}
