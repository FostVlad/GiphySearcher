package com.goloveschenko.gifsearcher.data.model;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("count")
    private int count;

    public int getCount() {
        return count;
    }
}
