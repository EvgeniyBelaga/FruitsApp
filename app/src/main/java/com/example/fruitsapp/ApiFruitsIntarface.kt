package com.example.fruitsapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiFruitsIntarface {

    @GET("fruitsBT/getFruits")
    fun getFruits(): Call<String?>?
}