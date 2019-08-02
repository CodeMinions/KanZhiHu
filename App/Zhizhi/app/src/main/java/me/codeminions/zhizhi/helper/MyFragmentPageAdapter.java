package me.codeminions.zhizhi.helper;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.codeminions.zhizhi.frags.firstPage.AttentFragment;
import me.codeminions.zhizhi.frags.firstPage.HostListFragment;
import me.codeminions.zhizhi.frags.firstPage.RecommendFragment;
import me.codeminions.zhizhi.frags.firstPage.TestFragment;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    private String[] titles = {"关注", "推荐", "热榜"};

    public MyFragmentPageAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new AttentFragment();
            case 1:
                return new RecommendFragment();
            case 2:
                return new HostListFragment();
            default:
                return new AttentFragment();
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    /**
     *  ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
