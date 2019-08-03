package me.codeminions.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class Fragment extends android.support.v4.app.Fragment {
    //便于复用
    protected View mRoot;
    protected Unbinder mRootUnBinder;

    protected int isLoad = 0;
    protected int isVisible  = 0;

    /**
     * 当一个fragment被添加到一个acitvity中，最首先被调用的方法
     * context即为activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前根布局，但不在创建时就添加到container中去（return root后方法内部调度自动添加到container中去）
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        }else{
            // 父布局不等于空，则把当前root从其父控件中移除
            if(mRoot.getParent() != null){
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        if(isVisible == 1 && isLoad == 0) {
            isLoad = 1;
            requestData();
        }

        return mRoot;
    }

    /**
     * 判断是否获取到数据
     */
    protected void initArgs(Bundle bundle){

    }

    // 当view界面初始化创建完成以后
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    /**
     * 获取当前界面的资源文件id
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    protected void initWidget(View root){
        mRootUnBinder = ButterKnife.bind(this, root);
    }

    protected void initData(){

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(!isVisibleToUser){
            if(isVisible != 0){
                isLoad = 1;
                isVisible = 0;
            }
        }else{
            isVisible = 1;
            if(isLoad == 0){
                if(mRoot != null) {
                    requestData();
                    isLoad = 1;
                }
            }
        }
    }
    protected void requestData(){

    }

    /**
     * 返回按键触发时调用
     * @return 返回true代表我已处理返回逻辑，activity不用自己finish
     * 返回flase代表未处理逻辑(不拦截)，activity自己处理逻辑
     */
    public boolean onBackPressed(){
        return false;
    }
}
