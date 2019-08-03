package me.codeminions.common.net;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofitService {

    private final static String URL_BASE = "http://192.168.1.107:8080/api/";

    private final static long DEFAULT_TIME_OUT = 30000L;
    private final static long DEFAULT_READ_TIME_OUT = 30000L;

    private Retrofit retrofit;

    private MyRetrofitService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i("RetrofitLog","retrofitBack = " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(URL_BASE)
                .build();
    }

    public static MyRetrofitService getInstance(){
        return SimpleHolder.instance;
    }

    public static class SimpleHolder{
        public final static MyRetrofitService instance = new MyRetrofitService();
    }

    public <T> T create(Class<T> service){
        return retrofit.create(service);
    }

}
