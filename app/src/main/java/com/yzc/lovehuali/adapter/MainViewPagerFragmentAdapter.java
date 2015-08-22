package com.yzc.lovehuali.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yzc.lovehuali.fragment.CollegeActivityFragment;
import com.yzc.lovehuali.fragment.AssociationNewsFragment;
import com.yzc.lovehuali.fragment.InformationFragment;
import com.yzc.lovehuali.fragment.NewsListFragment;
import com.yzc.lovehuali.fragment.ScheduleFragment;
import com.yzc.lovehuali.fragment.ToolKitFragment;

/**
 * Created by Administrator on 2015/1/26 0026.
 */
public class MainViewPagerFragmentAdapter extends FragmentPagerAdapter {


    @Override
    public int getCount() {
        return 4;
    }

    public MainViewPagerFragmentAdapter(FragmentManager fm) {super(fm);}

    @Override
    public Fragment getItem(int position) {


        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new InformationFragment();
                break;
            case 1:
                fragment = new NewsListFragment();
                break;
            case 2:
                fragment = new CollegeActivityFragment();
                break;
            case 3:
                fragment = new ToolKitFragment();
                break;
            default:
                return null;
        }
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
