package cn.xylink.mting.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DesignUtil;

import java.util.ArrayList;
import java.util.List;

import cn.xylink.mting.R;
import cn.xylink.mting.bean.SearchInfo;
import cn.xylink.mting.utils.DensityUtil;
import cn.xylink.mting.utils.ImageUtils;

/**
 * -----------------------------------------------------------------
 * 2019/12/2 15:59 : Create SearchAdapter.java (JoDragon);
 * -----------------------------------------------------------------
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchInfo> mData = new ArrayList<>();
    private boolean isSearchArticle = true;
    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_BROADCAST = 1;
    private Context mContext;
    private OnItemClickListener mListener;

    public SearchAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<SearchInfo> list) {
        if (mData == null) {
            mData = list;
        } else {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mData != null && mData.size() > 0) {
            mData.clear();
            this.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (getItemViewType(i) == TYPE_ARTICLE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_article, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_broadcast, viewGroup, false);
        }
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SearchInfo info = mData.get(i);
        if (isSearchArticle) {
            viewHolder.tvTitle.setText(TextUtils.isEmpty(info.getTitle()) ? "" : Html.fromHtml(info.getTitle()));
            viewHolder.tvSource.setText(info.getSourceName());
        } else {
            viewHolder.tvTitle.setText(TextUtils.isEmpty(info.getName()) ? "" : Html.fromHtml(info.getName()));
        }
        if (!TextUtils.isEmpty(info.getPicture())) {
            viewHolder.ivImg.setVisibility(View.VISIBLE);
            ImageUtils.get().load(viewHolder.ivImg, DensityUtil.dip2pxComm(mContext, 8), info.getPicture());
        } else {
            viewHolder.ivImg.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(info);
                }
            }
        });

    }

    public boolean isSearchArticle() {
        return isSearchArticle;
    }

    public void setSearchArticle(boolean searchArticle) {
        isSearchArticle = searchArticle;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isSearchArticle ? TYPE_ARTICLE : TYPE_BROADCAST;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvSource;
        ImageView ivImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_search_title);
            tvSource = itemView.findViewById(R.id.tv_search_source);
            ivImg = itemView.findViewById(R.id.iv_search_img);
        }
    }

    public OnItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(SearchInfo article);
    }

}
