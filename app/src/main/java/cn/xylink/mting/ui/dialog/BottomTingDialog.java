package cn.xylink.mting.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import cn.xylink.mting.R;

/**
 * -----------------------------------------------------------------
 * 2019/11/15 11:35 : Create BottomTingDialog.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BottomTingDialog extends BaseDimDialog {

    private OnBottomTingListener mListener;
    private List<TextView> mViewList;

    public BottomTingDialog(Context context) {
        super(context);
    }

    public BottomTingDialog(Context context, OnBottomTingListener l) {
        super(context);
        this.mListener = l;
    }

    @Override
    protected View getLayout() {
        return View.inflate(mContext, R.layout.dialog_bottom_ting, null);
    }

    @Override
    public void initView() {
        super.initView();
        this.getWindow().setWindowAnimations(R.style.share_animation);
        mViewList = new ArrayList<>();
        mViewList.add(findViewById(R.id.tv_dialog_ting_1));
        mViewList.add(findViewById(R.id.tv_dialog_ting_2));
        mViewList.add(findViewById(R.id.tv_dialog_ting_3));
        mViewList.add(findViewById(R.id.tv_dialog_ting_4));
    }

    /**
     * 设置item ，按顺序显示
     */
    public void setItemModle(BottomTingItemModle... modle) {
        ArrayList<View> list = new ArrayList<View>();
        for (int i = 0; i < (modle.length > 4 ? 4 : modle.length); i++) {
            setItemConent(mViewList.get(i), modle[i]);
        }
    }

    private void setItemConent(TextView view, BottomTingItemModle modle) {
        view.setTag(modle);
        view.setVisibility(View.VISIBLE);
        view.setText(modle.isTwo() ? modle.getNameTwo() : modle.getName());
        view.setCompoundDrawablesWithIntrinsicBounds(null, modle.isTwo() ? modle.getDrawableTwo() : modle.getDrawable(), null, null);
    }

    @OnClick({R.id.root,R.id.ll_layout, R.id.tv_dialog_ting_cancel, R.id.tv_dialog_ting_1, R.id.tv_dialog_ting_2, R.id.tv_dialog_ting_3, R.id.tv_dialog_ting_4})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_layout:
                break;
            case R.id.root:
            case R.id.tv_dialog_ting_cancel:
                this.dismiss();
                break;
            case R.id.tv_dialog_ting_1:
            case R.id.tv_dialog_ting_2:
            case R.id.tv_dialog_ting_3:
            case R.id.tv_dialog_ting_4:
                if (mListener != null) {
                    mListener.onBottomTingItemClick((BottomTingItemModle) view.getTag());
                }
                this.dismiss();
                break;
            default:
        }
    }

    public interface OnBottomTingListener {
        void onBottomTingItemClick(BottomTingItemModle modle);
    }
}
