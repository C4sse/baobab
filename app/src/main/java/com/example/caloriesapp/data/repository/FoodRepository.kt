package com.example.caloriesapp.data.repository

import com.example.caloriesapp.data.api.CalorieNinjasApi
import com.example.caloriesapp.data.model.NutritionResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(private val api: CalorieNinjasApi) {
    suspend fun getFoods(query: String): Response<NutritionResponse> {
        return api.getNutrition(query)
    }
}
