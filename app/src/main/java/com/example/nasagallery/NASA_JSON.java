package com.example.nasagallery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NASA_JSON {
    @GET("apod?api_key=kuZRpbmxczf8poKOJk7HrBAgmCDAqZvgrDVxjGo5&date={date}")
    Call<NASAGallery> getObject(@Query("date") String date);
}
