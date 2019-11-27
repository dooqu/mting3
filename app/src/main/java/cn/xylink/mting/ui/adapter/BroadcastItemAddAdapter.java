package cn.xylink.mting.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastInfo;
import cn.xylink.mting.bean.BroadcastItemAddInfo;

/**
 * @author wjn
 * @date 2019/11/26
 */
public class BroadcastItemAddAdapter extends RecyclerView.Adapter<BroadcastItemAddAdapter.ViewHolder> {
    private Context context;

    private List<BroadcastItemAddInfo> mData = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public BroadcastItemAddAdapter(Context context) {
        this.context = context;
    }

    public BroadcastItemAddAdapter(Context context, OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public BroadcastItemAddAdapter(Context context, List<BroadcastItemAddInfo> mData, OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.mData = mData;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public List<BroadcastItemAddInfo> getBroadcastItemList() {
        return mData;
    }

    public void setData(List<BroadcastItemAddInfo> mData) {
        if (null == mData) {
            this.mData = mData;
        } else {
            this.mData.addAll(mData);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (null != mData) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public BroadcastItemAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_broadcast_item_add, viewGroup, false);
        return new BroadcastItemAddAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastItemAddAdapter.ViewHolder viewHolder, int position) {
        BroadcastItemAddInfo data = mData.get(position);
        if (null != data) {
            viewHolder.broadcastName.setText(data.getName());
        }
//        viewHolder.itemView.setOnClickListener(view -> {
//            if (null != mOnItemClickListener) {
//                mOnItemClickListener.onItemClick(data);
//            }
//        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView broadcastName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            broadcastName = itemView.findViewById(R.id.tv_name_broadcast);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
