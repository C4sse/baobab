package com.example.caloriesapp.data.api

import com.example.caloriesapp.data.model.NutritionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CalorieNinjasApi {
    @GET("nutrition")
    suspend fun getNutrition(@Query("query") query: String): Response<NutritionResponse>
}
