package com.goloveschenko.gifsearcher.data.entity;

public class Gif {
    private String smallSizeUrl;
    private String normalSizeUrl;
    private String rating;

    public Gif(String smallSizeUrl, String normalSizeUrl, String rating) {
        this.smallSizeUrl = smallSizeUrl;
        this.normalSizeUrl = normalSizeUrl;
        this.rating = rating;
    }

    public String getSmallSizeUrl() {
        return smallSizeUrl;
    }

    public void setSmallSizeUrl(String smallSizeUrl) {
        this.smallSizeUrl = smallSizeUrl;
    }

    public String getNormalSizeUrl() {
        return normalSizeUrl;
    }

    public void setNormalSizeUrl(String normalSizeUrl) {
        this.normalSizeUrl = normalSizeUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
