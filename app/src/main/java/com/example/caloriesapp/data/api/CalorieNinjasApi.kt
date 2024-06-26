package com.example.caloriesapp.data.api

import com.example.caloriesapp.data.model.NutritionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CalorieNinjasApi {
    //TODO: put key as ENV
    @GET("nutrition")
    @Headers("X-Api-Key: 8/MqBej61B6ALLuEf7cIWg==tJbmaSTQHGZd6wLJ")
    suspend fun getNutrition(@Query("query") query: String): Response<NutritionResponse>
}
