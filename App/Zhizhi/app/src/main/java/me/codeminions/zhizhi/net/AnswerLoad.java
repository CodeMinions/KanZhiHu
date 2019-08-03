package me.codeminions.zhizhi.net;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.codeminions.common.bean.Answer;
import me.codeminions.common.net.ApiService;
import me.codeminions.common.net.MyRetrofitService;

public class AnswerLoad {

    private ApiService apiService;

    public AnswerLoad() {
        apiService = MyRetrofitService.getInstance().create(ApiService.class);
    }

    public Observable<Answer> getAnswerDe(String q, String a) {
        return observer(apiService.getAnswer(q, a));
    }


    public Observable<List<Answer>> getAnswerList() {
        return observer(apiService.getExplore());
    }

    public Observable<List<Answer>> getSearchAnswer(String q) {
        return observer(apiService.getSearch(q));
    }

    private <T> Observable<T> observer(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
