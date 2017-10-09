package com.goloveschenko.gifsearcher.domain;

import com.goloveschenko.gifsearcher.data.api.GiphyApiClient;
import com.goloveschenko.gifsearcher.data.entity.Gif;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

public class TrendingUseCase extends UseCase<TrendingUseCase.Params, List<Gif>> {
    @Override
    protected Observable<List<Gif>> build(Params params) {
        return GiphyApiClient.getClient().getTrending(params.rating, params.offset).map(data -> {
            List<Gif> gifList = new LinkedList<>();
            int count = data.getPagination().getCount();
            for (int i = 0; i < count; i++) {
                String smallSizeUrl = data.getData().get(i).getImages().getFixedHeightSmall().getUrl();
                String normalSizeUrl = data.getData().get(i).getImages().getFixedHeight().getUrl();
                String rating = data.getData().get(i).getRating();
                gifList.add(new Gif(smallSizeUrl, normalSizeUrl, rating));
            }
            return gifList;
        });
    }

    public static final class Params {
        private final String rating;
        private final int offset;

        private Params(String rating, int offset) {
            this.rating = rating;
            this.offset = offset;
        }

        public static Params getParams(String rating, int offset) {
            return new Params(rating, offset);
        }
    }
}