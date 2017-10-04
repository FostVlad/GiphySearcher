package com.goloveschenko.gifsearcher.domain;

import com.goloveschenko.gifsearcher.data.api.GiphyApiClient;
import com.goloveschenko.gifsearcher.data.entity.Gif;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

public class GifListUseCase extends UseCase<String, List<Gif>> {
    @Override
    protected Observable<List<Gif>> build(String s) {
        //search trending gifs
        if (s == null) {
            return GiphyApiClient.getClient().getTrending().map(data -> {
                List<Gif> gifList = new LinkedList<>();
                int count = data.getPagination().getCount();
                for (int i = 0; i < count; i++) {
                    String smallSizeUrl = data.getData().get(i).getImages().getFixedHeightSmall().getUrl();
                    String normalSizeUrl = data.getData().get(i).getImages().getFixedHeight().getUrl();
                    gifList.add(new Gif(smallSizeUrl, normalSizeUrl));
                }
                return gifList;
            });
        } else {
            return GiphyApiClient.getClient().getSearcher(s).map(data -> {
                List<Gif> gifList = new LinkedList<>();
                int count = data.getPagination().getCount();
                for (int i = 0; i < count; i++) {
                    String smallSizeUrl = data.getData().get(i).getImages().getFixedHeightSmall().getUrl();
                    String normalSizeUrl = data.getData().get(i).getImages().getFixedHeight().getUrl();
                    gifList.add(new Gif(smallSizeUrl, normalSizeUrl));
                }
                return gifList;
            });
        }
    }
}
