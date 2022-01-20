package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.weather.api.OpenWeatherMapService
import com.example.weather.model.OpenWeatherMapResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val titleView: TextView by lazy{ findViewById(R.id.main_title) }
    private val statusView: TextView by lazy{ findViewById(R.id.main_status) }
    private val descriptionView: TextView by lazy{ findViewById(R.id.main_description) }
    private val imageView: ImageView by lazy { findViewById(R.id.image_prediction) }

    private val retrofit by lazy{ Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    }

    private val weatherApiService by lazy { retrofit.create(OpenWeatherMapService::class.java) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherApiService.getWeather("Santander","35975275dd38dae3e12c3f6d79ad21ce").enqueue(object: Callback<OpenWeatherMapResponseData>{
            override fun onResponse(
                call: Call<OpenWeatherMapResponseData>,
                response: Response<OpenWeatherMapResponseData>
            ){handleResponse(response)}

            override fun onFailure(call: Call<OpenWeatherMapResponseData>, t: Throwable) {
               showError("Ha habido problema con la peticion ${t.message}")
            }

        })
    }

    private fun showError(message:String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    private fun handleResponse(response:Response<OpenWeatherMapResponseData>) =
        if (response.isSuccessful){
            response.body()?.let{
                handleValidResponse(it)
            }
        } else{
            showError("Response no exitosa")
        }
    private fun handleValidResponse(response:OpenWeatherMapResponseData){
        titleView.text = response.locationName
        response.weather.firstOrNull()?.let{
            statusView.text = it.status
            descriptionView.text= it.description
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.icon}@2x.png")
                .centerInside()
                .into(imageView)

        }
    }
}