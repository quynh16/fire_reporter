package com.example.fire_reporter2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetroFitAPI {
    @GET
    Call<NewsModal> getAllNews(@Url String url);

    @GET
    Call<NewsModal>getNewsByCategory(@Url String url);
}
