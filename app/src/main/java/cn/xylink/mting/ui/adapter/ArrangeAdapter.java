package cn.xylink.mting.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.BroadcastInfo;

/**
 * -----------------------------------------------------------------
 * 2019/11/29 14:59 : Create ArrangeAdapter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class ArrangeAdapter extends RecyclerView.Adapter<ArrangeAdapter.ViewHolder> {
    private Context mContext;
    private List<BroadcastInfo> mData = new ArrayList<>();
    private ArrangeAdapter.OnItemClickListener mOnItemClickListener;
    private int mSelectCount = 0;

    public ArrangeAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<BroadcastInfo> list) {
        if (mData == null) {
            mData = list;
        } else {
            mData.addAll(list);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.itemCheckChanged(mSelectCount);
            }
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mData != null) {
            mData.clear();
        }
    }

    public void selectAll() {
        if (mData != null && mData.size() > 0) {
            boolean isCheck = true;
            if (mSelectCount==getItemCount()){
                isCheck = false;
            }
            if (isCheck) {
                mSelectCount = getItemCount();
            } else {
                mSelectCount = 0;
            }
            for (BroadcastInfo info : mData) {
                info.setChecked(isCheck);
            }
            notifyDataSetChanged();
            if (mOnItemClickListener != null) {
                mOnItemClickListener.itemCheckChanged(mSelectCount);
            }
        }
    }

    public String getSelectArticleIDs() {
        if (mData != null && mData.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (BroadcastInfo article : mData) {
                if (article.isChecked()) {
                    buffer.append(article.getArticleId() + ",");
                }
            }
            return buffer.toString();
        }
        return null;
    }

    public void setOnItemClickListener(ArrangeAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ArrangeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_arrange, viewGroup, false);
        return new ArrangeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArrangeAdapter.ViewHolder holder, int position) {
        BroadcastInfo data = mData.get(position);
        holder.tvTitle.setText(data.getTitle());
        holder.checkBox.setChecked(data.isChecked());
        holder.itemView.setOnClickListener(v -> {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                notifCheckChange(holder, data);
        });
        holder.checkBox.setOnClickListener(v -> {
            notifCheckChange(holder, data);
        });
    }


    private void notifCheckChange(@NonNull ViewHolder holder, BroadcastInfo data) {
        data.setChecked(holder.checkBox.isChecked());
        if (!holder.checkBox.isChecked()) {
            if (mSelectCount > 0)
                mSelectCount--;

        } else {
            mSelectCount++;
            if (mSelectCount > getItemCount())
                mSelectCount = getItemCount();
        }
        if (mOnItemClickListener != null) {
            mOnItemClickListener.itemCheckChanged(mSelectCount);
        }
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
        AppCompatCheckBox checkBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_arrange_item);
            checkBox = itemView.findViewById(R.id.cb_arrange_item);
        }
    }

    public interface OnItemClickListener {
        void itemCheckChanged(int selectCount);
    }
}