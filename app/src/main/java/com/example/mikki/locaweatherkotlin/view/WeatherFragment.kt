package com.example.mikki.locaweatherkotlin.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mikki.locaweatherkotlin.R

import com.example.mikki.locaweatherkotlin.adapter.WeatherAdapter
import com.example.mikki.locaweatherkotlin.data.model.ListItem
import com.example.mikki.locaweatherkotlin.data.model.Location
import com.example.mikki.locaweatherkotlin.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.frag_weather.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class WeatherFragment: Fragment() {

    private val tag = AnkoLogger("myTag")
    private val viewModel = WeatherViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.frag_weather, container, false)
        /*val binding:FragWeatherBinding
                = DataBindingUtil.inflate(inflater,
            R.layout.frag_weather,container,false)

        val view2:View = binding.root

        binding.viewModel = viewModel*/

        val adapter = WeatherAdapter()

        var location = Location(
            lat = "35",
            lon = "139"
        )

        viewModel.loadWeather(location)?.observe(this,
            object :Observer<List<ListItem>>{
                override fun onChanged(t: List<ListItem>?) {
                    adapter.setList(t!!)
                    adapter.notifyDataSetChanged()
                    tag.warn { t.get(0).toString() }
                }
            });


        val mLayoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        view.rv_weather_list.layoutManager = mLayoutManager
        view.rv_weather_list.adapter = adapter

        return view

    }



}