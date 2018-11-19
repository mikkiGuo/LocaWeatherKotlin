package com.example.mikki.locaweatherkotlin.data.model

import com.google.gson.annotations.SerializedName

data class Weather(

	@field:SerializedName("list")
	val list: List<ListItem?>? = null
)