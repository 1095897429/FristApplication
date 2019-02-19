package com.ngbj.browse.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2018/8/20
 * author:zl
 * 备注：viewPager的适配器
 */
public class HomeFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private List<String> list_Title;

    public HomeFragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public HomeFragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragments, List<String> list_Titl) {
        super(fm);
        this.fragments = fragments;
        this.list_Title = list_Titl;
    }

    public void setList_Title(List<String> list_Title) {
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }

}
