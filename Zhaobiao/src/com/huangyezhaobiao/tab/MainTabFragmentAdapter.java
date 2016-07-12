package com.huangyezhaobiao.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by 58 on 2016/6/16.
 */
public class MainTabFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mArrayList;

    public MainTabFragmentAdapter(FragmentManager fm,ArrayList<Fragment> mArrayList) {
        super(fm);
        this.mArrayList = mArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        if((mArrayList != null) && (mArrayList.size() > position))
            return mArrayList.get(position);
        return null;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        return super.instantiateItem(container, position);
    }
}
