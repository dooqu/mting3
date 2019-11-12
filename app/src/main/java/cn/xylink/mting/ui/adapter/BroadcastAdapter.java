package cn.xylink.mting.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.utils.ImageUtils;

/**
 * -----------------------------------------------------------------
 * 2019/11/11 16:14 : Create BroadcastAdapter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class BroadcastAdapter extends RecyclerView.Adapter<BroadcastAdapter.ViewHolder> {
    private Context mContext;
    private List<BroadcastInfo> mData = new ArrayList<>();
    private BroadcastAdapter.OnItemClickListener mOnItemClickListener;

    public BroadcastAdapter(Context context) {
        this.mContext = context;
    }

    public BroadcastAdapter(Context context, BroadcastAdapter.OnItemClickListener listener) {
        this.mContext = context;
        this.mOnItemClickListener = listener;
    }

    public BroadcastAdapter(Context context, List<BroadcastInfo> list, BroadcastAdapter.OnItemClickListener listener) {
        this.mContext = context;
        this.mData = list;
        this.mOnItemClickListener = listener;
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
    }

    @NonNull
    @Override
    public BroadcastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_broadcast, viewGroup, false);
        return new BroadcastAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastAdapter.ViewHolder holder, int position) {
        BroadcastInfo data = mData.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvSource.setText(data.getSourceName());
        if (!TextUtils.isEmpty(data.getPicture())) {
            holder.ivImg.setVisibility(View.VISIBLE);
            ImageUtils.get().load(holder.ivImg, data.getPicture());
        }else {
            holder.ivImg.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(data);
            }
        });
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_broadcast_title);
            tvSource = itemView.findViewById(R.id.tv_broadcast_source);
            tvProgress = itemView.findViewById(R.id.tv_broadcast_progress);
            ivImg = itemView.findViewById(R.id.iv_broadcast_img);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BroadcastInfo article);
    }
}