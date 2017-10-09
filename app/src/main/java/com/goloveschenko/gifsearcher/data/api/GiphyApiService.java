package com.goloveschenko.gifsearcher.data.api;

import com.goloveschenko.gifsearcher.data.model.Data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyApiService {
    @GET("gifs/trending")
    Observable<Data> getTrending(@Query("rating") String rating, @Query("offset") int offset);
    @GET("gifs/search")
    Observable<Data> getSearching(@Query("q") String query, @Query("rating") String rating, @Query("offset") int offset);
}
