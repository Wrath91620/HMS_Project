package com.first.a1806957;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OMI {
    @GET("/poi")
    Call<List<POIListItem>> FetchPOILists(@Query("key") String key);
}
