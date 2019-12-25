package cn.xylink.mting.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastAllListInfo;

/**
 * -----------------------------------------------------------------
 * 2019/12/25 11:20 : Create SelectBroadcastAdapter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SelectBroadcastAdapter extends RecyclerView.Adapter<SelectBroadcastAdapter.ViewHolder> {
    private Context mContext;
    private List<BroadcastAllListInfo> mData = new ArrayList<>();
    private SelectBroadcastAdapter.OnItemClickListener mOnItemClickListener;

    public SelectBroadcastAdapter(Context context) {
        mContext = context;
    }

    public SelectBroadcastAdapter(Context context, SelectBroadcastAdapter.OnItemClickListener listener) {
        this.mContext = context;
        this.mOnItemClickListener = listener;
    }

    public void setData(List<BroadcastAllListInfo> list) {
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
    public SelectBroadcastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select_broadcast, viewGroup, false);
        return new SelectBroadcastAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectBroadcastAdapter.ViewHolder holder, int position) {
        BroadcastAllListInfo data = mData.get(position);
        holder.tvTitle.setText(data.getName());
        holder.tvContact.setText("（" + data.getTotal() + "篇）");
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(data, position);
            }
        });
    }

    public List<BroadcastAllListInfo> getArticleList() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_select_broadcast_item_title);
            tvContact = itemView.findViewById(R.id.tv_select_broadcast_item_num);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BroadcastAllListInfo article, int position);
    }

}