package cn.xylink.mting.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.utils.ImageUtils;

/**
 * -----------------------------------------------------------------
 * 2019/12/20 10:27 : Create BroadcastDetailActivity.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastDetailActivity extends BasePresenterActivity{
    public static final String EXTARA_DETAIL_INFO = "extara_detail_info";
    @BindView(R.id.iv_broadcast_detail_img)
    ImageView mImageView;
    @BindView(R.id.iv_broadcast_detail_create_img)
    ImageView mCreateImageView;
    @BindView(R.id.iv_broadcast_detail_bg)
    ImageView mBGImageView;
    @BindView(R.id.tv_broadcast_detail_describe)
    TextView mDesTextView;
    @BindView(R.id.tv_broadcat_detail_name)
    TextView mNameTextView;
    @BindView(R.id.tv_broadcast_detail_create_name)
    TextView mCreateNameTextView;

    @Override
    protected void preView() {
        setContentView(R.layout.activity_broadcast_detail);
    }

    @Override
    protected void initView() {
        BroadcastDetailInfo broadcastDetailInfo = (BroadcastDetailInfo) getIntent().getSerializableExtra(EXTARA_DETAIL_INFO);
        ImageUtils.get().load(mImageView,R.mipmap.cjbd_img_fm_default,R.mipmap.cjbd_img_fm_default,10,broadcastDetailInfo.getPicture());
        ImageUtils.get().load(mBGImageView,R.mipmap.cjbd_img_fm_default,R.mipmap.cjbd_img_fm_default,10,broadcastDetailInfo.getPicture());
//        ImageUtils.get().load(mCreateImageView,R.mipmap.cjbd_img_fm_default,R.mipmap.cjbd_img_fm_default,10,broadcastDetailInfo.getPicture());
        mDesTextView.setText(broadcastDetailInfo.getInfo());
        mNameTextView.setText(broadcastDetailInfo.getName());
        mCreateNameTextView.setText(broadcastDetailInfo.getCreateName());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleBar() {

    }

    @OnClick(R.id.iv_broadcast_detail_back)
    void onClick(View view){
        this.finish();
    }
}
