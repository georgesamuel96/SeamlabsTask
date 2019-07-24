package com.example.georgesamuel.seamlabstask.remote;

import com.example.georgesamuel.seamlabstask.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClientAPI {

    @GET("/v2/everything")
    Call<NewsResponse> getArticles(
            @Query("q") String q,
            @Query("from") String from,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );
}
