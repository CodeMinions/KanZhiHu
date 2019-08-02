package me.codeminions.zhizhi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import me.codeminions.zhizhi.frags.main.CollegeFragment;
import me.codeminions.zhizhi.frags.main.FirstPageFragment;
import me.codeminions.zhizhi.frags.main.IdeaFragment;
import me.codeminions.zhizhi.frags.main.MeFragment;
import me.codeminions.zhizhi.frags.main.MsgFragment;
import me.codeminions.zhizhi.helper.NavHelper;

/**
 * 主页面
 * 底部是5个Activity的入口
 * 默认为FirstPageActivity
 */
public class MainActivity extends AppCompatActivity implements NavHelper.OnTabChangeListen, BottomNavigationView.OnNavigationItemSelectedListener {

    NavHelper navHelper;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);

        navHelper = new NavHelper(this, R.id.lay_container, getSupportFragmentManager(), this);
        navHelper.add(R.id.action_firstPage, new NavHelper.Tab(FirstPageFragment.class))
                .add(R.id.action_idea, new NavHelper.Tab(IdeaFragment.class))
                .add(R.id.action_college, new NavHelper.Tab(CollegeFragment.class))
                .add(R.id.action_msg, new NavHelper.Tab(MsgFragment.class))
                .add(R.id.action_me, new NavHelper.Tab(MeFragment.class));

        navigation.setOnNavigationItemSelectedListener(this);

        initData();
    }

    void initData(){
        Menu m = navigation.getMenu();
        m.performIdentifierAction(R.id.action_firstPage, 1);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return navHelper.performClickMenu(menuItem.getItemId());
    }

    @Override
    public void onTabChange(NavHelper.Tab newF, NavHelper.Tab oldF) {

    }
}
