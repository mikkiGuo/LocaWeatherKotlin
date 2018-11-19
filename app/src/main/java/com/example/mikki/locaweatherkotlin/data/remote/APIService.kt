package com.example.mikki.locaweatherkotlin.data.remote

import com.example.mikki.locaweatherkotlin.data.model.Weather
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

//https://samples.openweathermap.org/data/2.5/
//forecast?
//lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22

    @GET("forecast")
    fun loadWeather(@Query("lat") lat:String,
              @Query("lon") lon:String,
              @Query("appid") appid:String): Observable<Weather>


    companion object {

        val BASEURL:String = "http://api.openweathermap.org/data/2.5/"

        fun create(): APIService {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(APIService::class.java)
        }
    }


}