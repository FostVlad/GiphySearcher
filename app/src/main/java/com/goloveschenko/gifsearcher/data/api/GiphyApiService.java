package com.goloveschenko.gifsearcher.data.api;

import com.goloveschenko.gifsearcher.data.model.Data;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface GiphyApiService {
    @GET("trending")
    Observable<Data> getTrending();
}
