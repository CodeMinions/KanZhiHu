package me.codeminions.zhizhi.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;

public class NavHelper {

    private FragmentManager manager;
    private Context context;
    private int containerId;
    private OnTabChangeListen listen;
    private SparseArray<Tab> tabs = new SparseArray<>();

//    private Fragment currentFragment;
    private Tab currentTab;

    public NavHelper(Context context, int containerId, FragmentManager manager, OnTabChangeListen listen){
        this.context = context;
        this.containerId = containerId;
        this.manager = manager;
        this.listen = listen;
    }

    public NavHelper add(int menuId, Tab tab){
        tabs.put(menuId, tab);
        return this;
    }

    public Tab getCurrentTab() {
        return currentTab;
    }

    public boolean performClickMenu(int menuId){
        Tab t = tabs.get(menuId);
        if(t != null){
            Log.i("808080", t.getClass().getName());
            doSelect(t);
            return true;
        }
        return false;
    }

    private void doSelect(Tab tab){
        Tab oldT = null;
        if(currentTab != null){
            oldT = currentTab;
            if (currentTab == tab) {
                // TODO: 19-2-14 刷新页面
                return;
            }
        }
        currentTab = tab;
        doChangeTab(tab, oldT);

    }

    private void doChangeTab(Tab newF, Tab oldF) {
        FragmentTransaction ft = manager.beginTransaction();
        if (oldF != null) {
            ft.detach(oldF.fragment);
        }

        if (newF != null){
            if(newF.fragment == null){
                Fragment fragment = Fragment.instantiate(context, newF.clx.getName(), null);
                ft.add(containerId, fragment, fragment.getClass().getName());
                newF.fragment = fragment;
            }else
                ft.attach(newF.fragment);
        }

        ft.commit();
        notifyTabSelect(newF, oldF);
    }

    private void notifyTabSelect(Tab newF, Tab oldF){
        if(listen != null){
            listen.onTabChange(newF, oldF);
        }
    }

    public static class Tab{
        Class<?> clx;
        Fragment fragment;
        public Tab(Class<?> clx){
            this.clx = clx;
        }
    }

    public interface OnTabChangeListen{
        void onTabChange(Tab newF, Tab oldF);
    }
}
