package me.codeminions.zhizhi.net;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static OkHttpClient client;
    public static void sendHttpRequest(String address, MyCallback callback){

        client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();

        client.newCall(request).enqueue(callback);

    }

    public static void sendHttpRequest(String address, RequestBody requestBody, MyCallback callback){
        client = new OkHttpClient.Builder()
                .readTimeout(20,TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(20,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(40,TimeUnit.SECONDS)//设置连接超时时间
                .build();

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public abstract static class MyCallback implements okhttp3.Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("", e.getMessage());
        }
    }
}
