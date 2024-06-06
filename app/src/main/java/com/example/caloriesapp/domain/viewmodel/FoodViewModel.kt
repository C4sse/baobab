package com.example.caloriesapp.domain.viewmodel

import androidx.lifecycle.*
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
        viewModelScope.launch {
            val response = repository.getFoods(query)
            if (response.isSuccessful) {
                _foods.value = response.body()?.items
            }
        }
    }
}
