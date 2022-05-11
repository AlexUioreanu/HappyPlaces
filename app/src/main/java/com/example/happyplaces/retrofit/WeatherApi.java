package com.example.happyplaces.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("/data/2.5/weather")
    Call<Weather> getWeather(@Query("q") String city, @Query("appid") String token, @Query("units") String units);
}
