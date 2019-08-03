package me.codeminions.common.net;

import io.reactivex.observers.DisposableObserver;

public class RequestResult<T> extends DisposableObserver<T> {

    private OnRequestResult<T> onRequestResult;

    public RequestResult(OnRequestResult<T> result){
        this.onRequestResult = result;
    }

    @Override
    public void onNext(T t) {
        onRequestResult.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onRequestResult.onFail(e);
    }

    @Override
    public void onComplete() {

    }

    public interface OnRequestResult<T>{
        void onSuccess(T t);
        void onFail(Throwable e);
    }
}
