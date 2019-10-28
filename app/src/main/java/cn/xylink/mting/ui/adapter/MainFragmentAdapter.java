package cn.xylink.mting.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xylink.mting.ui.fragment.BaseFragment;
import cn.xylink.mting.ui.fragment.MyFragment;
import cn.xylink.mting.ui.fragment.TingFragment;
import cn.xylink.mting.ui.fragment.WorldFragment;


public class MainFragmentAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragments = new ArrayList<>();

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(TingFragment.newInstance());
        fragments.add(WorldFragment.newInstance());
        fragments.add(MyFragment.newInstance());
    }

    @Override
    public BaseFragment getItem(int i) {
        return fragments != null ? fragments.get(i) : null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
