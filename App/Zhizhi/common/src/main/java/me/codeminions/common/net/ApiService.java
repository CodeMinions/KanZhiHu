package me.codeminions.common.net;

import java.util.List;

import io.reactivex.Observable;
import me.codeminions.common.bean.Answer;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @Headers({
            "User-Agent: android",
            "apiKey: 2828"
    })

    @GET("/api/explore/titles")
    Observable<List<Answer>> getExplore();

    @GET("/api/search/login")
    Observable<List<Answer>> getSearch(@Query("q") String q);

    @GET("/api/answer/login/")
    Observable<Answer> getAnswer(@Query("q") String q, @Query("a") String a);

    @POST("/api/answer/login/")
    Observable<Answer> getAnswer(@Body String con);
}
