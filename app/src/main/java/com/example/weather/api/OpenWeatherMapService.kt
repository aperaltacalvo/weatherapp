package com.example.weather.api

import com.example.weather.model.OpenWeatherMapResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {
    @GET ("weather")
    fun getWeather(@Query("q")location:String, @Query("appid") token:String): Call<OpenWeatherMapResponseData>
}