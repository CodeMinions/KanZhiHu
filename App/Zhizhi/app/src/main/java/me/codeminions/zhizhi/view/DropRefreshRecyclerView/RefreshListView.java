package me.codeminions.zhizhi.view.DropRefreshRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import me.codeminions.zhizhi.helper.RefreshAdapter;

public class RefreshListView extends RecyclerView {

    private IRefreshHeader header;
    private Context mContext;
    private boolean isRefreshing = false;
    private OnRefreshListener listener;

    public RefreshListView(Context context) {
        this(context,null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public void setAdapter(RefreshAdapter adapter) {
        if(header == null){
            header = new RefreshHeader(mContext);
            adapter.setHeader(header);
        }
        super.setAdapter(adapter);
    }

    private float sumY = 0; // 记录滑动距离
    private float lastY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (lastY == -1) {
            lastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = e.getRawY();
                sumY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = (e.getRawY() - lastY) / 2;//为了防止滑动幅度过大，将实际手指滑动的距离除以2
                lastY = e.getRawY();
                sumY += deltaY;//计算总的滑动的距离
                if (isTop() && !isRefreshing) {
                    header.onMove(deltaY, sumY);//移动刷新的头部View
                    if (header.getVisibleHeight() > 0) {
                        return false;
                    }
                }
                break;
            default:
                lastY = -1; // reset
                if(!isTop())
                    Log.i("isTop()", "这个时候isTop() == false");
                if (isTop()&& !isRefreshing) {
                    if (header.onRelease()) {
                        //手指松开
                        if (listener != null) {
                            isRefreshing = true;
                            listener.onReFresh();//回调刷新完成的监听
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    boolean isTop(){
        return header.getHeader().getParent() != null;  // header的Parent即是列表的首行，及判断首行是否加载
    }

    public void setListener(OnRefreshListener listener){
        this.listener = listener;
    }

    public void refreshCompete(){
        if(isRefreshing){
            isRefreshing = false;
            header.refreshComlete();
        }
    }
}
