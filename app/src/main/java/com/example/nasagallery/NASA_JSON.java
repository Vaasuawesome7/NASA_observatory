package com.example.nasagallery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NASA_JSON {
    @GET("apod?api_key=kuZRpbmxczf8poKOJk7HrBAgmCDAqZvgrDVxjGo5")
    Call<NASAGallery> getObject(@Query("date") String date);

    @GET("/search")
    Call<NASAImageAndVideo> getSearchResult(@Query("q") String q);

    @GET("/asset/{nasa_id}")
    Call<RecievedItems> getItemFromSearch(@Path("nasa_id") String nasaId);
}
