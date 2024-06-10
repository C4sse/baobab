package com.example.caloriesapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val name: String,
    val calories: Float,
    val serving_size_g: Double = 0.0,
    val fat_total_g: Double = 0.0,
    val protein_g: Double = 0.0,
    val carbohydrates_total_g: Double = 0.0,
)
