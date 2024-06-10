package com.example.caloriesapp.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.caloriesapp.CaloriesAppTheme
import com.example.caloriesapp.data.model.Food
import com.example.caloriesapp.domain.viewmodel.AutoCompleteTextField
import com.example.caloriesapp.domain.viewmodel.FoodViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun LandingScreen()  {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    )
    {
        Column {
            TopAppBar(
                title = {
                    Text(text = "Calories Budget", color = Color.White)
                },
                backgroundColor = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TodaysBudgetSection()

            Spacer(modifier = Modifier.height(16.dp))

            FoodTrackingSection()
        }
    }
}

@Composable
fun TodaysBudgetSection(viewModel: FoodViewModel = hiltViewModel()) {
    val foods by viewModel.foods.observeAsState(emptyList())
    val focusManager = LocalFocusManager.current
    var totalCalories by remember { mutableStateOf(0) }
    var totalProteins by remember { mutableStateOf(0) }
    var totalCabs by remember { mutableStateOf(0) }
    var totalFat by remember { mutableStateOf(0) }
    val calorieBudget = 1750
    val query by viewModel.query.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {

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
    }
    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "Today's budget", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "Budget", color = Color.Gray, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.height(35.dp))
                    Text(text = "$calorieBudget",fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(text = " kcal", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
            Column(
            ) {
                // Calculate total calories whenever foods change
                totalCalories = foods.sumOf { it.calories.toInt() }
                totalCabs = foods.sumOf { it.carbohydrates_total_g.toInt() }
                totalFat = foods.sumOf { it.fat_total_g.toInt() }
                totalProteins = foods.sumOf { it.protein_g.toInt() }
                CaloriePieChart(calories = totalCalories, totalCalories = calorieBudget)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NutrientProgress("Protein", totalProteins, 170, Color(0xFF4CAF50))
            NutrientProgress("Carbs", totalCabs, 170, Color(0xFFFFC107))
            NutrientProgress("Fat", totalFat, 65, Color(0xFFFF5722))
        }
    }
}

@Composable
fun NutrientProgress(name: String, value: Int, maxValue: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = name, color = color, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(5.dp))
        LinearProgressIndicator(
            progress = value / maxValue.toFloat(),
            color = color,
            backgroundColor = Color.LightGray,
            modifier = Modifier.width(60.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "$value/$maxValue g", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun FoodTrackingSection(viewModel: FoodViewModel = hiltViewModel()) {
    val foods by viewModel.foods.observeAsState(emptyList())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Food List",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)  // This will push the Icon to the far right
                )
                IconButton(onClick = { viewModel.clearFoods() }) {
                    Icon(
                        imageVector = Icons.Default.ClearAll,
                        contentDescription = "Add",
                        tint = Color.Red
                    )
                }
            }

            LazyColumn {
                items(foods) { food ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(2.dp, 2.dp)
                    ) {
                        FoodTrackingItem(
                            food.name,
                            food.calories,
                            500,
                            Icons.Outlined.Fastfood,
                            foodInfo = food,
                            onDelete = {
                                viewModel.deleteFood(food)
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun NutritionalInfo(protein: Double, fats: Double, carbs: Double) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(text = "Nutritional information", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = "for 100 g:", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Proteins")
            Text(text = "${protein} g", color = Color(0xFF4CAF50))
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Fats")
            Text(text = "${fats} g", color = Color(0xFFFFA000)) // Amber color
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Carbs")
            Text(text = "${carbs} g", color = Color.Red)
        }
    }
}

//swipe to delete
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodTrackingItem(
    name: String,
    calories: Double,
    maxCalories: Int,
    icon: ImageVector,
    foodInfo: Food,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val swipeableState = rememberSwipeableState(0)
    val coroutineScope = rememberCoroutineScope()

    val sizePx = with(LocalDensity.current) { 300.dp.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .background(Color.White)
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        coroutineScope.launch {
                            swipeableState.performDrag(dragAmount)
                            if (swipeableState.offset.value <= -sizePx) {
                                onDelete()
                            }
                        }
                        change.consume()
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = name)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "$calories kcal", color = Color.Gray)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Add $name",
                        tint = Color(0xFF4CAF50)
                    )
                }
            }

            if (isExpanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    NutritionalInfo(protein = foodInfo.protein_g, fats = foodInfo.fat_total_g, carbs = foodInfo.carbohydrates_total_g)
                }
            }
        }

        println(swipeableState.currentValue)
        if (swipeableState.currentValue > 0.7) {
            LaunchedEffect(Unit) {
                onDelete()
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewLandingScreen() {
    CaloriesAppTheme {
        LandingScreen()
    }
}
