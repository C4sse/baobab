package com.example.caloriesapp.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriesapp.data.model.Food
import com.example.caloriesapp.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(private val repository: FoodRepository) : ViewModel() {

    private val _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> get() = _foods

    fun searchFoods(query: String) {
        println(query)
        println("starting flex")
        viewModelScope.launch {
            val response = repository.getFoods(query)
            println("*******************")
            println(response.isSuccessful)
            println(response.body()?.items)
            println("*******************")
            if (response.isSuccessful) {
                response.body()?.items?.let { newFoods ->
                    val currentFoods = _foods.value ?: emptyList()
                    _foods.value = currentFoods + newFoods
                }
                println("flex22")
                println("flex22")
            }
        }
    }
}
