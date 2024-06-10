package com.example.caloriesapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.caloriesapp.domain.viewmodel.AutoCompleteTextField
import com.example.caloriesapp.domain.viewmodel.FoodViewModel

@Composable
fun FoodSearchScreen(viewModel: FoodViewModel = hiltViewModel()) {

    val foods by viewModel.foods.observeAsState(emptyList())
    val focusManager = LocalFocusManager.current
    var totalCalories by remember { mutableStateOf(0) }
    val calorieBudget = 1750
    val query by viewModel.query.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text(
            text = "Calorie Tracker",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))
        AutoCompleteTextField(
            suggestions = suggestions,
            query = query,
        onQueryChanged = { viewModel.onQueryChanged(it) },
        onSuggestionSelected = { selectedFood ->
            viewModel.searchFoods(selectedFood)
            viewModel.onQueryChanged("")
            // clear focus if needed
            focusManager.clearFocus(true)
        }
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Calculate total calories whenever foods change
        totalCalories = foods.sumOf { it.calories.toInt() }

        // Pass data to pie chart
        CaloriePieChart(calories = totalCalories, totalCalories = calorieBudget)
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn {
            items(foods) { food ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White),
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = food.name.capitalize(),
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface
                        )
                        Text(
                            text = "${food.calories} cal",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
