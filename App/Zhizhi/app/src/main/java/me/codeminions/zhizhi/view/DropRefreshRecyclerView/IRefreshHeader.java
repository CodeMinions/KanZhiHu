package me.codeminions.zhizhi.view.DropRefreshRecyclerView;

import android.view.View;

public interface IRefreshHeader {

    int STATE_NOMAL = 1;    // 正常状态
    int STATE_SCROLL = 2;   // 可触发刷新状态
    int STATE_REFRESH = 3;  // 刷新状态
    int STATE_DONE = 4;     // 刷新完成状态

    /**
     * 移动时回调
     */
    void onMove(float deltaY, float sumY);

    /**
     * 超过指定高度
     */
    void onPrepare();

    /**
     * 松开时回调
     * @return 返回是否触发刷新
     */
    boolean onRelease();

    /**
     * 正在刷新
     */
    void onRefreshing();

    /**
     * 刷新完成调用
     */
    void refreshComlete();

    View getHeader();

    /**
     * 获取Header的显示高度
     */
    int getVisibleHeight();
}
