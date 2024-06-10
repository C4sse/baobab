package com.example.caloriesapp.domain.viewmodel

//import androidx.compose.ui.window.application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriesapp.data.model.Food
import com.example.caloriesapp.data.repository.FoodRepository
import com.example.caloriesapp.data.storage.getSuggestions
import com.example.caloriesapp.data.storage.saveSuggestions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val repository: FoodRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> get() = _foods
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> get() = _suggestions

    init {
        viewModelScope.launch {
            getSuggestions(context).collect { savedSuggestions ->
                _suggestions.value = savedSuggestions
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

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

                val updatedSuggestions = _suggestions.value.toMutableList().apply {
                    if (!contains(query)) add(query)
                }

                viewModelScope.launch {
                    saveSuggestions(context, updatedSuggestions)
                    _suggestions.value = updatedSuggestions
                }
                println("flex22")
                println("flex22")
            }
        }
    }
}
