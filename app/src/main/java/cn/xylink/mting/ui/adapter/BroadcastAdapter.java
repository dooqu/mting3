package cn.xylink.mting.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastDetailInfo;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.common.Const;
import cn.xylink.mting.utils.ContentManager;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.utils.ImageUtils;
import cn.xylink.mting.utils.L;

/**
 * -----------------------------------------------------------------
 * 2019/11/11 16:14 : Create BroadcastAdapter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastAdapter extends RecyclerView.Adapter<BroadcastAdapter.ViewHolder> {
    private Context mContext;
    private List<BroadcastInfo> mData = new ArrayList<>();
    private BroadcastAdapter.OnItemClickListener mOnItemClickListener;
    private String mBroadcastid = "-1";
    private BroadcastDetailInfo mDetailInfo;

    public BroadcastAdapter(Context context) {
        this.mContext = context;
        mData.add(new BroadcastInfo());
    }

    public BroadcastAdapter(Context context, String id) {
        this.mContext = context;
        mData.add(new BroadcastInfo());
        this.mBroadcastid = id;
    }

    public void setDetailInfo(BroadcastDetailInfo detailInfo) {
        mDetailInfo = detailInfo;
        notifyItemChanged(0);
    }

    public void setData(List<BroadcastInfo> list) {
        if (mData == null) {
            mData = list;
        } else {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mData != null) {
            mData.clear();
        }
        mData.add(new BroadcastInfo());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BroadcastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view;
        if (position == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_broadcast_header, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_broadcast, viewGroup, false);
        }
        return new BroadcastAdapter.ViewHolder(view, position);
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            if (mBroadcastid.startsWith("-")) {
                initSysBroadcast(holder);
            } else if (mDetailInfo != null) {
                holder.mShadowView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams lp = holder.mTopLayout.getLayoutParams();
                lp.height = DensityUtil.dip2pxComm(mContext, 253f);
                holder.mTopLayout.setLayoutParams(lp);
                holder.mTitleTextView.setText(mDetailInfo.getCreateName());
                holder.mDesTextView.setText(mDetailInfo.getInfo());
                ImageUtils.get().load(holder.mImageView, 0, 0, 8, mDetailInfo.getPicture());
                ImageUtils.get().load(holder.mTopLayout, mDetailInfo.getPicture());
                if (mDetailInfo.getCreateName().equals(ContentManager.getInstance().getUserInfo().getUserId())) {
                    if (mDetailInfo.getShare() == 0) {
                        holder.mShare2worldTextView.setVisibility(View.VISIBLE);
                    } else {
                        holder.mSubscribedTextView.setVisibility(View.VISIBLE);
                        holder.mSubscribedTextView.setText("已定阅：" + getSubscribedNum(mDetailInfo.getSubscribeTotal()));
                    }
                } else {
                    holder.mSubscribedTextView.setVisibility(View.VISIBLE);
                    holder.mSubscribedTextView.setText("已定阅：" + getSubscribedNum(mDetailInfo.getSubscribeTotal()));
                }
            }
        } else {
            BroadcastInfo data = mData.get(position);
            holder.tvTitle.setText(data.getTitle());
            holder.tvSource.setText(data.getSourceName());
            if (data.getProgress() > 0) {
                holder.tvProgress.setText("已读"+getPercentFormat(data.getProgress()));
            }
            if (!TextUtils.isEmpty(data.getPicture())) {
                holder.ivImg.setVisibility(View.VISIBLE);
                ImageUtils.get().load(holder.ivImg, data.getPicture());
            } else {
                holder.ivImg.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(data);
                }
            });
            holder.itemView.setOnLongClickListener(v -> {
                if (mOnItemClickListener != null) {
                    data.setPositin(holder.getAdapterPosition());
                    mOnItemClickListener.onItemLongClick(data);
                }
                return true;
            });
        }
    }

    /**
     * 待读传-1，已读历史传-2，收藏传-3，我创建的传-4。
     */
    private void initSysBroadcast(BroadcastAdapter.ViewHolder holder) {
        holder.mTitleTextView.setText("简介");
        if (Const.SystemBroadcast.SYSTEMBROADCAST_UNREAD.equals(mBroadcastid)) {
            holder.mImageView.setImageResource(R.mipmap.icon_head_unread);
            holder.mDesTextView.setText("待读会自动存放您添加到轩辕 听内的文章");
        } else if (Const.SystemBroadcast.SYSTEMBROADCAST_READED.equals(mBroadcastid)) {
            holder.mImageView.setImageResource(R.mipmap.icon_head_readed);
            holder.mDesTextView.setText("这里显示您读过的所有文章");
        } else if (Const.SystemBroadcast.SYSTEMBROADCAST_STORE.equals(mBroadcastid)) {
            holder.mImageView.setImageResource(R.mipmap.icon_head_love);
            holder.mDesTextView.setText("这里显示您收藏的所有文章");
        } else if (Const.SystemBroadcast.SYSTEMBROADCAST_MY_CREATE_ARTICLE.equals(mBroadcastid)) {
            holder.mImageView.setImageResource(R.mipmap.icon_head_mycreate);
            holder.mDesTextView.setText("这里显示您创建的所有文章");
        }
    }

    /**
     * 客户端显示的订阅数规则：
     * 小于1000，<1千
     * 1000-10000，显示具体数字，如：6725
     * 10000以上，1万+
     */
    private String getSubscribedNum(int total) {
        if (total < 1000) {
            return "<1千";
        } else if (total > 10000) {
            return "1万+";
        }
        return total + "";
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    public static String getPercentFormat(float d) {
        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        nf.setMaximumIntegerDigits(3);//小数点前保留几位
        nf.setMinimumFractionDigits(0);// 小数点后保留几位
        String str = nf.format(d);
        return str;
    }

    public void notifyItemRemoe(int position) {
        if (mData != null & mData.size() > position) {
            ListIterator<BroadcastInfo> ite = mData.listIterator(position);
            ite.next();
            ite.remove();
        }
        notifyItemRemoved(position);
    }

    public List<BroadcastInfo> getArticleList() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvSource;
        TextView tvProgress;
        ImageView ivImg;
        /**
         * top
         */
        RelativeLayout mTopLayout;
        ImageView mImageView;
        TextView mTitleTextView;
        TextView mDesTextView;
        TextView mSubscribedTextView;
        TextView mShare2worldTextView;
        View mShadowView;

        public ViewHolder(@NonNull View itemView, int position) {
            super(itemView);
            if (position == 0) {
                mTopLayout = itemView.findViewById(R.id.rl_broadcast_top_layout);
                mImageView = itemView.findViewById(R.id.iv_broadcast_img);
                mTitleTextView = itemView.findViewById(R.id.tv_broadcast_title);
                mDesTextView = itemView.findViewById(R.id.tv_broadcast_description);
                mSubscribedTextView = itemView.findViewById(R.id.tv_broadcast_subscribed);
                mShare2worldTextView = itemView.findViewById(R.id.tv_broadcast_share2world);
                mShadowView = itemView.findViewById(R.id.iv_broadcast_shadow);
            } else {
                tvTitle = itemView.findViewById(R.id.tv_broadcast_title);
                tvSource = itemView.findViewById(R.id.tv_broadcast_source);
                tvProgress = itemView.findViewById(R.id.tv_broadcast_progress);
                ivImg = itemView.findViewById(R.id.iv_broadcast_img);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BroadcastInfo article);

        void onItemLongClick(BroadcastInfo article);
    }
}