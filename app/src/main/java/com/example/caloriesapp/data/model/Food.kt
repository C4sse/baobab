package com.example.caloriesapp.data.model

import kotlinx.serialization.Serializable


@Serializable
data class Food(val name: String, val calories: Float)
