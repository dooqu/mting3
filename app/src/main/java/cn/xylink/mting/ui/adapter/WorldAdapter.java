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
import cn.xylink.mting.bean.WorldInfo;
import cn.xylink.mting.utils.ImageUtils;

/**
 * 世界适配器
 * -----------------------------------------------------------------
 * 2019/11/6 14:56 : Create WorldAdapter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class WorldAdapter extends RecyclerView.Adapter<WorldAdapter.ViewHolder> {
    private Context mContext;
    private List<WorldInfo> mData = new ArrayList<>();
    private WorldAdapter.OnItemClickListener mOnItemClickListener;

    public WorldAdapter(Context context) {
        this.mContext = context;
    }

    public WorldAdapter(Context context, WorldAdapter.OnItemClickListener listener) {
        this.mContext = context;
        this.mOnItemClickListener = listener;
    }

    public WorldAdapter(Context context, List<WorldInfo> list, WorldAdapter.OnItemClickListener listener) {
        this.mContext = context;
        this.mData = list;
        this.mOnItemClickListener = listener;
    }

    public void setData(List<WorldInfo> list) {
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
    public WorldAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_world, viewGroup, false);
        return new WorldAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorldAdapter.ViewHolder holder, int position) {
        WorldInfo data = mData.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvContact.setText(data.getDescribe());
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

    public List<WorldInfo> getArticleList() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvContact;
        ImageView ivImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_world_title);
            tvContact = itemView.findViewById(R.id.tv_world_msg);
            ivImg = itemView.findViewById(R.id.iv_world_img);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(WorldInfo article);
    }
}