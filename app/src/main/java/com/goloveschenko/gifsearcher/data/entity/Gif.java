package com.goloveschenko.gifsearcher.data.entity;

public class Gif {
    private String smallSizeUrl;
    private String normalSizeUrl;

    public Gif(String smallSizeUrl, String normalSizeUrl) {
        this.smallSizeUrl = smallSizeUrl;
        this.normalSizeUrl = normalSizeUrl;
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
}
