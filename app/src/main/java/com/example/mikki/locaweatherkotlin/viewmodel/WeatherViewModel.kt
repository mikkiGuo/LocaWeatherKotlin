package com.example.mikki.locaweatherkotlin.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.mikki.locaweatherkotlin.data.IRepository
import com.example.mikki.locaweatherkotlin.data.Repository
import com.example.mikki.locaweatherkotlin.data.model.ListItem
import com.example.mikki.locaweatherkotlin.data.model.Location
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


class WeatherViewModel: ViewModel() {

    val repository:IRepository = Repository()
    private val tag = AnkoLogger("myTag")

    fun loadWeather(location: Location)
            :LiveData<List<ListItem>>?{
        val data = repository.loadWeather(location)
        tag.warn {"view model : " + data.toString()  }
        return data
    }
}