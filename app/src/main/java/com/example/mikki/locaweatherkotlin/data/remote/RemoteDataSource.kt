package com.example.mikki.locaweatherkotlin.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.mikki.locaweatherkotlin.data.IRepository
import com.example.mikki.locaweatherkotlin.data.model.ListItem
import com.example.mikki.locaweatherkotlin.data.model.Location
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class RemoteDataSource : IRepository{


    private val tag = AnkoLogger("myTag")

    var disposable: Disposable? = null
    val apiService by lazy {
        APIService.create()
    }

    override fun loadWeather(location: Location): LiveData<List<ListItem>>? {
        //lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22
        var data = MutableLiveData<List<ListItem?>>()

        disposable = apiService.loadWeather(location.lat,
            location.lon, "b6907d289e10d714a6e88b30761fae22")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        result -> tag.warn {
                    result.list!!.get(0)!!.main.toString()}
                    tag.warn { result.list!!.get(0)!!.weather.toString() }

                    data.value = result.list
                    tag.warn { "data === " + data.value}

                }, {
                        error -> tag.warn { error.message.toString() }

                }
            )
        tag.warn { "outside apiCall: " + data.value }
        return data as LiveData<List<ListItem>>?
    }

    /*override fun loadWeather(location: Location): LiveData<Weather>? {
        var data = LiveData<Weather>()
        disposable = apiService.loadWeather(location.lat,
            location.lon, "b6907d289e10d714a6e88b30761fae22")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        result -> tag.warn { result.list!!.get(0)!!.main.toString()}
                    tag.warn { result.list!!.get(0)!!.weather.toString() }

                    data = result
                    tag.warn { "data === " + data.city}

                }, {
                        error -> tag.warn { error.message.toString() }

                }
            )
        return data

    }*/


}