package cn.xylink.mting.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.scwang.smartrefresh.layout.util.DesignUtil;
import com.tendcloud.tenddata.TCAgent;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.utils.L;

/**
 *首页右上角菜单
 *
 * -----------------------------------------------------------------
 * 2019/3/14 11:55 : Create GroupMemberSelectPop.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class MainAddMenuPop extends PopupWindow {

    private Context mContext;
    private Window mWindow;
    private OnMainAddMenuListener mListener;

    public MainAddMenuPop(Context context, OnMainAddMenuListener listener) {
        super(context);
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.pop_amin_add_menu, null);
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        this.mContext = context;
        this.mListener = listener;
        this.setFocusable(true);
        this.setTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setOutsideTouchable(true);
        this.setWidth(DensityUtil.dip2pxComm(mContext, 140f));
        this.setHeight(DensityUtil.dip2pxComm(mContext, 150f));
//        this.setAnimationStyle(R.style.anim_pop);
        mWindow = ((Activity) context).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = 0.6f;
        mWindow.setAttributes(params);
    }

    public void showAsRight(View v) {

        this.showAsDropDown(v, -DensityUtil.dip2pxComm(mContext,95), 0);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = 1.0f;
        mWindow.setAttributes(params);
    }

    @OnClick({R.id.btn_group_member_pop_create, R.id.btn_group_member_pop_put, R.id.btn_group_member_pop_create_broadcast})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_group_member_pop_create:
                mListener.onCreateArticle();
                break;
            case R.id.btn_group_member_pop_put:
                mListener.onPut();
                break;
            case R.id.btn_group_member_pop_create_broadcast:
                mListener.onCreateBroadcast();
                break;
        }
        this.dismiss();
    }

    public interface OnMainAddMenuListener {
        void onCreateArticle();
        void onPut();
        void onCreateBroadcast();
    }
}
