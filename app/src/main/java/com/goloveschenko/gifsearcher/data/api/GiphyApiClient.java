package com.goloveschenko.gifsearcher.data.api;

import com.goloveschenko.gifsearcher.BuildConfig;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GiphyApiClient {
    private static final String QUERY_NAME_APPID = "key";
    private static final String QUERY_VALUE_APPID = "792a9563d39242d5a1acbb60e59548d8";

    private static GiphyApiService service = null;

    public static GiphyApiService getClient() {
        if (service == null) {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter(QUERY_NAME_APPID, QUERY_VALUE_APPID).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            service = retrofit.create(GiphyApiService.class);
            return service;
        }
        return service;
    }
}
