package cn.xylink.mting.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.TingInfo;
import cn.xylink.mting.common.Const;

/**
 * 享听适配器
 * <p>
 * -----------------------------------------------------------------
 * 2019/11/5 16:28 : Create tingAdapter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class TingAdapter extends RecyclerView.Adapter<TingAdapter.ViewHolder> {
    private Context mContext;
    private List<TingInfo> mData = new ArrayList<>();
    private TingAdapter.OnItemClickListener mOnItemClickListener;


    public TingAdapter(Context context, TingAdapter.OnItemClickListener listener) {
        this.mContext = context;
        this.mOnItemClickListener = listener;
        TingInfo info = new TingInfo();
        info.setBroadcastId(Const.SystemBroadcast.SYSTEMBROADCAST_UNREAD);
        info.setName("待读");
        mData.add(info);
    }

    public void setData(List<TingInfo> list) {
        if (mData == null) {
            mData = list;
        } else {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mData != null)
            mData.clear();
    }

    @NonNull
    @Override
    public TingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ting, viewGroup, false);
        return new TingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TingAdapter.ViewHolder holder, int position) {
        TingInfo data = mData.get(position);
        holder.tvTitle.setText(data.getName());
        holder.tvContact.setText(data.getMessage());
        if (data.getTop() == 1) {
            holder.ivTop.setVisibility(View.VISIBLE);
        } else {
            holder.ivTop.setVisibility(View.GONE);
        }
        if (data.getNewMsg() > 0) {
            holder.tvDot.setVisibility(View.VISIBLE);
        } else {
            holder.tvDot.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(data,position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemLongClick(data);
            }
            return false;
        });
    }

    public List<TingInfo> getArticleList() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvContact;
        TextView tvDot;
        ImageView ivTop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_ting_item_title);
            tvContact = itemView.findViewById(R.id.tv_ting_item_content);
            tvDot = itemView.findViewById(R.id.iv_ting_item_dot);
            ivTop = itemView.findViewById(R.id.iv_ting_item_top);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TingInfo article,int position);

        void onItemLongClick(TingInfo article);
    }
}