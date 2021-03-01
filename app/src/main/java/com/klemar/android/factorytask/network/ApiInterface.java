package com.klemar.android.factorytask.network;

import com.klemar.android.factorytask.model.NewsListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("everything")
    Call<NewsListResponse> getNews(@Query("sources") String source, @Query("apiKey") String apiKey);

    @GET("everything")
    Call<NewsListResponse> getOlderNews(@Query("sources") String source, @Query("to") String toDate, @Query("apiKey") String apiKey);

}
