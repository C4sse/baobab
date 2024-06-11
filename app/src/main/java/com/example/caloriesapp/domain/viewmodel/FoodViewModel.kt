package com.example.caloriesapp.domain.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriesapp.data.model.Food
import com.example.caloriesapp.data.repository.FoodRepository
import com.example.caloriesapp.data.storage.getFoods
import com.example.caloriesapp.data.storage.getSuggestions
import com.example.caloriesapp.data.storage.saveFoods
import com.example.caloriesapp.data.storage.saveSuggestions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class responsible for managing UI-related data in a lifecycle-conscious way.
 * This class works with the FoodRepository to fetch and manage food data.
 *
 * @property repository An instance of FoodRepository for data operations.
 * @property context The application context, injected using Hilt.
 */
@HiltViewModel
class FoodViewModel @Inject constructor(
    private val repository: FoodRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // LiveData to hold the list of Food items.
    private val _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> get() = _foods

    // StateFlow to hold the current query string.
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query

    // StateFlow to hold the list of suggestion strings.
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> get() = _suggestions

    init {
        // Fetch saved suggestions from the data store and update the StateFlow.
        viewModelScope.launch {
            getSuggestions(context).collect { savedSuggestions ->
                _suggestions.value = savedSuggestions
            }
        }
        // Fetch saved foods from the data store and update the LiveData.
        viewModelScope.launch {
            getFoods(context).collect { savedFoods ->
                _foods.value = savedFoods
            }
        }
    }

    /**
     * Updates the query string.
     *
     * @param newQuery The new query string.
     */
    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    /**
     * Searches for foods based on the query string and updates the foods and suggestions.
     *
     * @param query The query string to search for foods.
     */
    fun searchFoods(query: String) {
        viewModelScope.launch {
            val response = repository.getFoods(query)
            println(response.isSuccessful)
            println(response.body()?.items)
            if (response.isSuccessful) {
                response.body()?.items?.let { newFoods ->
                    val currentFoods = _foods.value ?: emptyList()
                    val updatedFoods = currentFoods + newFoods
                    _foods.value = updatedFoods

                    // Save foods to DataStore.
                    saveFoods(context, updatedFoods)
                }

                val updatedSuggestions = _suggestions.value.toMutableList().apply {
                    if (!contains(query)) add(query)
                }

                viewModelScope.launch {
                    saveSuggestions(context, updatedSuggestions)
                    _suggestions.value = updatedSuggestions
                }
            }
        }
    }

    /**
     * Clears all foods from the list and updates the DataStore.
     */
    fun clearFoods() {
        viewModelScope.launch {
            _foods.value = emptyList()
            saveFoods(context, emptyList())
        }
    }

    /**
     * Deletes a specific food item from the list and updates the DataStore.
     *
     * @param food The food item to delete.
     */
    fun deleteFood(food: Food) {
        viewModelScope.launch {
            val currentFoods = _foods.value ?: emptyList()
            val updatedFoods = currentFoods.filter { it.id != food.id }
            _foods.value = updatedFoods
            saveFoods(context, updatedFoods)
        }
    }
}
