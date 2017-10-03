package com.goloveschenko.gifsearcher.data.model;

import com.google.gson.annotations.SerializedName;

public class Images {
    @SerializedName("fixed_height")
    private FixedHeight fixedHeight;
    @SerializedName("fixed_height_small")
    private FixedHeightSmall fixedHeightSmall;

    public FixedHeight getFixedHeight() {
        return fixedHeight;
    }

    public FixedHeightSmall getFixedHeightSmall() {
        return fixedHeightSmall;
    }
}
