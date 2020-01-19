package cn.xylink.mting.ui.dialog;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.model.data.RemoteUrl;
import cn.xylink.mting.openapi.QQApi;
import cn.xylink.mting.openapi.WXapi;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.T;

/**
 * -----------------------------------------------------------------
 * 2019/11/26 10:24 : Create BroadcastItemMenuDialog.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastItemMenuDialog extends BaseDimDialog {
    @BindView(R.id.tv_broadcast_item_menu_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_broadcast_item_menu_collect)
    TextView mCollectTextView;
    @BindView(R.id.tv_broadcast_item_menu_del)
    TextView mDelTextView;
    @BindView(R.id.ll_broadcast_item_menu_br)
    LinearLayout mBrLayout;
    private BroadcastInfo mBroadcastInfo;
    private String mShareUrl;
    private String mShareTitle;
    private String mShareDes;

    public BroadcastItemMenuDialog(Context context) {
        super(context);
    }

    @Override
    protected View getLayout() {
        return View.inflate(mContext, R.layout.dialog_broadcast_item, null);
    }

    @Override
    public void initView() {
        super.initView();
        this.getWindow().setWindowAnimations(R.style.share_animation);
    }

    public void setBroadcastInfo(BroadcastInfo broadcastInfo) {
        mBroadcastInfo = broadcastInfo;
        mTitleTextView.setText(mBroadcastInfo.getTitle());
        if (mBroadcastInfo.getStore() == 1) {
            mCollectTextView.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.mipmap.icon_uncollect), null, null);

        }
        mShareUrl = broadcastInfo.getShareUrl();
        mShareTitle = broadcastInfo.getTitle();
        mShareDes = broadcastInfo.getDescribe();

    }

    public void setDetailInfo(BroadcastDetailInfo info) {
        mTitleTextView.setText(info.isSimulateArticle()? info.getName() : "分享");
        mShareUrl = info.getShareUrl();
        mShareTitle = info.getName() + "——" + info.getCreateName();
        mShareDes = info.getInfo();
        mBrLayout.setVisibility(View.GONE);
    }

    public void setAppinfo(){
        mTitleTextView.setText("分享「轩辕听」");
        mShareUrl = RemoteUrl.getShareUrl();
        mShareTitle = "轩辕听";
        mShareDes = "帮你读文章的软件，读朋友圈、读新闻的APP";
        mBrLayout.setVisibility(View.GONE);
    }

    public void isShowDel(boolean b) {
        if (!b) {
            mDelTextView.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.tv_broadcast_item_menu_wx, R.id.tv_broadcast_item_menu_pyq, R.id.tv_broadcast_item_menu_qq, R.id.tv_broadcast_item_menu_kj,
            R.id.tv_broadcast_item_menu_link
            , R.id.tv_broadcast_item_menu_collect, R.id.tv_broadcast_item_menu_addto, R.id.tv_broadcast_item_menu_del,
            R.id.tv_broadcast_item_menu_close
            , R.id.rl_broadcast_item_menu_root, R.id.ll_broadcast_item_menu_bg})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_broadcast_item_menu_wx:
                WXapi.shareWx((Activity) mContext, mShareUrl, null, mShareTitle, mShareDes);
                this.dismiss();
                break;
            case R.id.tv_broadcast_item_menu_pyq:
                WXapi.sharePyq((Activity) mContext, mShareUrl, null, mShareTitle, mShareDes);
                this.dismiss();
                break;
            case R.id.tv_broadcast_item_menu_qq:
                QQApi.shareQQ((Activity) mContext, mShareUrl, null, mShareTitle, mShareDes);
                this.dismiss();
                break;
            case R.id.tv_broadcast_item_menu_kj:
                QQApi.shareSpace((Activity) mContext, mShareUrl, null, mShareTitle, mShareDes);
                this.dismiss();
                break;
            case R.id.tv_broadcast_item_menu_link:
                ContentManager.getInstance().addCopyItem(mShareUrl);
                String url = "我正在使用【轩辕听】：一款帮你读文章的软件，读朋友圈、读新闻的APP    " + mShareUrl;
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", url);
                cm.setPrimaryClip(mClipData);
                T.showCustomToast("分享链接复制成功");
                this.dismiss();
                break;
            case R.id.tv_broadcast_item_menu_collect:
                if (mListener != null) {
                    mListener.onItemCollect(mBroadcastInfo);
                }
                this.dismiss();
                break;
            case R.id.tv_broadcast_item_menu_addto:
                if (mListener != null) {
                    mListener.onItemAddTO(mBroadcastInfo);
                }
                this.dismiss();
                break;
            case R.id.tv_broadcast_item_menu_del:
                if (mListener != null) {
                    mListener.onItemDel(mBroadcastInfo);
                }
                this.dismiss();
                break;
            case R.id.tv_broadcast_item_menu_close:
            case R.id.rl_broadcast_item_menu_root:
                this.dismiss();
                break;
            case R.id.ll_broadcast_item_menu_bg:
                break;
            default:
        }
    }

    private OnBroadcastItemMenuListener mListener;

    public void setListener(OnBroadcastItemMenuListener listener) {
        mListener = listener;
    }

    public interface OnBroadcastItemMenuListener {
        void onItemCollect(BroadcastInfo info);

        void onItemAddTO(BroadcastInfo info);

        void onItemDel(BroadcastInfo info);
    }
}
