package com.example.caloriesapp.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Food(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val calories: Double = 0.0,
    val serving_size_g: Double = 0.0,
    val fat_total_g: Double = 0.0,
    val protein_g: Double = 0.0,
    val carbohydrates_total_g: Double = 0.0,
)
