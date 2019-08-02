package me.codeminions.zhizhi.tools;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import me.codeminions.common.view.app.Fragment;

public class MHandler extends Handler {
    private WeakReference<Activity> referenceAct;
    private WeakReference<Fragment> referenceFrag;

    private HandleCallBack callBack;

    public MHandler(Activity activity, HandleCallBack callBack) {
        referenceAct = new WeakReference<>(activity);
        this.callBack = callBack;
    }

    public MHandler(Fragment frag, HandleCallBack callBack) {
        referenceFrag = new WeakReference<>(frag);
        this.callBack = callBack;
    }

    @Override
    public void handleMessage(Message msg) {
        // 这里需要先检查reference是否为空，若为空则不进行下一判断（否则直接get()会空指针）
        if(referenceAct != null && referenceAct.get() != null)
            callBack.handleMessage(msg, referenceAct);
        else
            callBack.handleMessage(msg, referenceFrag);
    }

    public interface HandleCallBack{
        void handleMessage(Message msg, WeakReference reference);
    }
}
