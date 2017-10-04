package com.goloveschenko.gifsearcher.data.api;

import com.goloveschenko.gifsearcher.data.model.Data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyApiService {
    @GET("gifs/trending")
    Observable<Data> getTrending();
    @GET("gifs/search")
    Observable<Data> getSearcher(@Query("q") String query);
}
