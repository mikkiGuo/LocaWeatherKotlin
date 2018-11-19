package com.example.mikki.locaweatherkotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mikki.locaweatherkotlin.R
import com.example.mikki.locaweatherkotlin.data.model.ListItem
import kotlinx.android.synthetic.main.item_weather.view.*

class WeatherAdapter:RecyclerView.Adapter<WeatherAdapter.ViewHolder>(){

    var weatherList = mutableListOf<ListItem>()
    //var weatherInfo = Weather()

    fun setList(list: List<ListItem>){
        weatherList = list as MutableList<ListItem>
    }
    /*fun setWeather(item:Weather){
        weatherInfo = item
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_weather, parent, false))
    }

    override fun getItemCount(): Int {
        //return weatherInfo.list!!.size
        return weatherList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        holder.city.text = weatherInfo.city!!.name

        val weatherItem = weatherList.get(position)

        holder.main.text = weatherItem.weather!!.get(0)!!.main

        val symbol = 0x00B0.toChar()

        val tempF = ((9/5) * (weatherItem.main!!.temp!! - 273.15) + 32).toInt().toString()
        val temperature = tempF + symbol
        holder.temp.text = temperature

        holder.dateTime.text = weatherItem.dtTxt

        val tempMinF = ((9/5) * (weatherItem.main!!.tempMin!! - 273.15) + 32).toInt().toString()
        val tempMinimum = "Lowest Temperature: " + tempMinF + symbol
        holder.tempMin.text = tempMinimum

        val tempMaxF:Int = ((9/5) * (weatherItem.main.tempMax!! - 273.15) + 32).toInt()
        holder.tempMax.text = "Highest Temperature: " + tempMaxF.toString() + symbol

        val temp1 = "Humidity: " + weatherItem.main.humidity.toString()
        holder.humidity.text = temp1

        val temp2 = "Pressure: " + weatherItem.main.pressure.toString()
        holder.pressure.text = temp2

        val temp3 = "Sea Level: " + weatherItem.main.seaLevel.toString()
        holder.seaLevel.text = temp3
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //var city = itemView.tv_city_name
        var main = itemView.tv_category
        var temp = itemView.tv_temp
        var dateTime = itemView.tv_datetime
        var tempMin = itemView.tv_temp_min
        var tempMax = itemView.tv_temp_max
        var humidity = itemView.tv_humidity
        var pressure = itemView.tv_pressure
        var seaLevel = itemView.tv_sealevel
    }
}