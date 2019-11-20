package cn.xylink.mting.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


public class ControlPanelAdapter extends PagerAdapter {

    List<View> pageViewList = new ArrayList<>();

    public ControlPanelAdapter(List<View> list) {
        pageViewList.addAll(list);
    }

    @Override
    public int getCount() {
        return pageViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(pageViewList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view =  pageViewList.get(position);
        int height = pageViewList.get(position).getMeasuredHeight();
        container.addView(pageViewList.get(position));

        return pageViewList.get(position);
    }
}
