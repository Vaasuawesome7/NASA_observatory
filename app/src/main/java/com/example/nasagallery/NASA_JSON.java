package com.example.nasagallery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NASA_JSON {
    @GET("apod?api_key=kuZRpbmxczf8poKOJk7HrBAgmCDAqZvgrDVxjGo5&date={date}")
    Call<NASAGallery> getObject(@Path("date") String date);
}
