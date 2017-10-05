package com.goloveschenko.gifsearcher.data.model;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("count")
    private int count;
    @SerializedName("offset")
    private int offset;

    public int getCount() {
        return count;
    }

    public int getOffset() {
        return offset;
    }
}
