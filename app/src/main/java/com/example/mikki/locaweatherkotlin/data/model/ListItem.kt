package com.example.mikki.locaweatherkotlin.data.model

import com.google.gson.annotations.SerializedName

data class ListItem(

	@field:SerializedName("dt_txt")
	val dtTxt: String? = null,

	@field:SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

	@field:SerializedName("main")
	val main: Main? = null

)