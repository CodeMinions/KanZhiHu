package me.codeminions.zhizhi.view.DropRefreshRecyclerView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.codeminions.zhizhi.R;

public class RefreshHeader extends LinearLayout implements IRefreshHeader {

    private LinearLayout contentLayout;
    private TextView textView;
    private float measureHeight;
    private int lastState = 0;

    public RefreshHeader(Context context){
        this(context, null);
    }

    public RefreshHeader(Context context, AttributeSet attr){
        this(context, attr, 0);
    }

    public RefreshHeader(Context context, AttributeSet attr, int defStyleAttr){
        super(context, attr, defStyleAttr);
        init();
    }

    public void init(){
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(lp);

        contentLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_refreshheader, null);
        addView(contentLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));

        textView = contentLayout.findViewById(R.id.shuaxin);

        measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        measureHeight = getMeasuredHeight();
    }

    @Override
    public void onMove(float offSet, float sumOffSet) {
        if (getVisibleHeight() > 0 || offSet > 0) {
            setHeight((int) offSet + getVisibleHeight());
            if (lastState <= STATE_SCROLL) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > measureHeight) {
                    onPrepare();
                } else {
                    reSet();
                }
            }
        }
    }

    @Override
    public boolean onRelease() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) {// not visible.
            isOnRefresh = false;
        }

        if (getVisibleHeight() > measureHeight && lastState < STATE_REFRESH) {
            setState(STATE_REFRESH);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (lastState == STATE_REFRESH && height > measureHeight) {
            animationScroll((int) measureHeight);
        }
        if (lastState != STATE_REFRESH) {
            animationScroll(0);
        }

        if (lastState == STATE_REFRESH) {
            animationScroll((int) measureHeight);
        }

        return isOnRefresh;
    }

    @Override
    public void onPrepare() {
        setState(STATE_SCROLL);
    }

    @Override
    public void onRefreshing() {
        setState(STATE_REFRESH);
    }

    @Override
    public void refreshComlete() {
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reSet();
                animationScroll(0);
            }
        }, 200);
    }

    void setState(int state){
        if(lastState == state)
            return;
        switch (state){
            case STATE_NOMAL:
                textView.setText("下拉刷新");
                break;
            case STATE_SCROLL:
                textView.setText("释放刷新");
                break;
            case STATE_REFRESH:
                animationScroll((int) measureHeight);
                textView.setText("正在刷新");
                break;
            case STATE_DONE:
                textView.setText("刷新成功");
                break;
        }
        lastState = state;
    }

    void reSet(){
        setState(STATE_NOMAL);
    }

    /**
     * @param desHeight 指定高度
     */
    void animationScroll(int desHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), desHeight);
        animator.setDuration(300).start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public int getVisibleHeight(){
        return contentLayout.getLayoutParams().height;
    }

    /**
     * 设置heade高度
     */
    void setHeight(int height){
        if(height < 0) height = 0;
        LayoutParams lp = (LayoutParams) contentLayout.getLayoutParams();
        lp.height = height;
        contentLayout.setLayoutParams(lp);
    }

    @Override
    public View getHeader() {
        return this;
    }
}
