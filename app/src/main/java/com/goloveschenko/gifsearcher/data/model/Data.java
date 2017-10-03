package com.goloveschenko.gifsearcher.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("data")
    private List<Datum> data = null;
    @SerializedName("pagination")
    private Pagination pagination;

    public List<Datum> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
