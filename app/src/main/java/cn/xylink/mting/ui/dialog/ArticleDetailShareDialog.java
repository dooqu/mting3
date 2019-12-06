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
import cn.xylink.mting.bean.ArticleDetail2Info;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.openapi.QQApi;
import cn.xylink.mting.openapi.WXapi;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.T;

/**
 * @author wjn
 * @date 2019/12/06
 */
public class ArticleDetailShareDialog extends BaseDimDialog {
    @BindView(R.id.tv_broadcast_item_menu_title)
    TextView mTitleTextView;
    private String mShareUrl;
    private String mShareTitle;
    private String mShareDes;

    public ArticleDetailShareDialog(Context context) {
        super(context);
    }

    @Override
    protected View getLayout() {
        return View.inflate(mContext, R.layout.dialog_article_detail_share, null);
    }

    @Override
    public void initView() {
        super.initView();
        this.getWindow().setWindowAnimations(R.style.share_animation);
    }

    public void setDetailInfo(ArticleDetail2Info info) {
        mTitleTextView.setText("分享文章");
        mShareUrl = info.getShareUrl();
        mShareTitle = info.getTitle() + "——" + info.getNickName();
        mShareDes = info.getContent();
    }


    @OnClick({R.id.tv_broadcast_item_menu_wx, R.id.tv_broadcast_item_menu_pyq, R.id.tv_broadcast_item_menu_qq, R.id.tv_broadcast_item_menu_kj,
            R.id.tv_broadcast_item_menu_close, R.id.rl_broadcast_item_menu_root, R.id.ll_broadcast_item_menu_bg})
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
            case R.id.tv_broadcast_item_menu_close:
            case R.id.rl_broadcast_item_menu_root:
                this.dismiss();
                break;
            case R.id.ll_broadcast_item_menu_bg:
                break;
            default:
        }
    }

}
