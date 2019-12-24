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
 * 2019/12/24 11:06 : Create SelectArticleAddAdapter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SelectArticleAddAdapter extends RecyclerView.Adapter<SelectArticleAddAdapter.ViewHolder> {
    private Context mContext;
    private List<BroadcastInfo> mData = new ArrayList<>();
    private SelectArticleAddAdapter.OnItemClickListener mOnItemClickListener;

    public SelectArticleAddAdapter(Context context) {
        this.mContext = context;
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

    public void setOnItemClickListener(SelectArticleAddAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SelectArticleAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select_article_add, viewGroup, false);
        return new SelectArticleAddAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectArticleAddAdapter.ViewHolder holder, int position) {
        BroadcastInfo data = mData.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.tvSource.setText(data.getSourceName());
        if (!TextUtils.isEmpty(data.getPicture())) {
            holder.ivImg.setVisibility(View.VISIBLE);
            ImageUtils.get().load(holder.ivImg, data.getPicture());
        } else {
            holder.ivImg.setVisibility(View.GONE);
        }

        if (data.isChecked()){
            holder.ivAdd.setVisibility(View.GONE);
            holder.tvAdded.setVisibility(View.VISIBLE);
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
        ImageView ivImg;
        TextView tvAdded;
        ImageView ivAdd;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_select_article_title);
            tvSource = itemView.findViewById(R.id.tv_select_article_source);
            ivImg = itemView.findViewById(R.id.iv_select_article_img);
            tvAdded = itemView.findViewById(R.id.tv_select_article_added);
            ivAdd = itemView.findViewById(R.id.iv_select_article_add);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BroadcastInfo article);
    }

    public void changeItemChecked(String id){
        for (int i = 0;i<mData.size();i++){
            if (id.equals(mData.get(i).getArticleId())){
                mData.get(i).setChecked(true);
                notifyItemChanged(i);
            }
        }
    }
}
