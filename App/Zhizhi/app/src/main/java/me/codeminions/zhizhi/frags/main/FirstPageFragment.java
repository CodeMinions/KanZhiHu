package me.codeminions.zhizhi.frags.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import me.codeminions.common.view.app.Fragment;
import me.codeminions.zhizhi.R;
import me.codeminions.zhizhi.activity.SearchActivity;
import me.codeminions.zhizhi.helper.MyFragmentPageAdapter;

public class FirstPageFragment extends Fragment implements View.OnClickListener {

    ViewPager mViewPager;
    MyFragmentPageAdapter fragmentAdapter;
    TabLayout mTabLayout;

    TextView textView;

    public FirstPageFragment() {

    }

    @Override
    public void initWidget(View root) {
        super.initWidget(root);

        mViewPager = (ViewPager) root.findViewById(R.id.viewPager);
        fragmentAdapter = new MyFragmentPageAdapter(getChildFragmentManager());
        mTabLayout = (TabLayout) root.findViewById(R.id.tabLayout);

        mViewPager.setAdapter(fragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(0);


        textView = (TextView) root.findViewById(R.id.txt_search);
        textView.setOnClickListener(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_search:
                SearchActivity.actionStart(mRoot.getContext());
                break;
        }
    }
}
