package me.codeminions.common.net;

import android.content.Context;

import io.reactivex.observers.DisposableObserver;
import me.codeminions.common.widget.loaddialog.LatteLoader;

public class RequestResult<T> extends DisposableObserver<T> {

    private OnRequestResult<T> onRequestResult;
    private Context context;

    // 默认开启
    private boolean showDialog = false;

    public RequestResult(OnRequestResult<T> result) {
        this.onRequestResult = result;
    }

    public RequestResult(OnRequestResult<T> result, Context context) {
        this(result);
        this.context = context;
        showDialog = true;
    }

    public RequestResult(OnRequestResult<T> result, Context context, boolean showDialog) {
        this(result, context);
        this.showDialog = showDialog;
    }

    @Override
    protected void onStart() {
        showDialog();
    }

    @Override
    public void onNext(T t) {
        onRequestResult.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onRequestResult.onFail(e);
        dismissDialog();
    }

    @Override
    public void onComplete() {
        dismissDialog();
    }

    private void showDialog() {
        if(showDialog)
            LatteLoader.showLoading(context);
    }

    private void dismissDialog(){
        if(showDialog)
            LatteLoader.stopLoader();
    }

    public interface OnRequestResult<T> {
        void onSuccess(T t);

        void onFail(Throwable e);
    }
}
