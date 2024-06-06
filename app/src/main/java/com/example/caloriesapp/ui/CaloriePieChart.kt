package com.example.caloriesapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CaloriePieChart(calories: Int, totalCalories: Int) {
    val color = if (calories <= totalCalories) Color.Green else Color.Red
    val remainingCalories = totalCalories - calories

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Calorie Budget", color = Color.Gray)
        Text(text = totalCalories.toString(), color = Color.Blue, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.LightGray,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 20f)
                )
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = (calories / totalCalories.toFloat()) * 360f,
                    useCenter = false,
                    style = Stroke(width = 20f)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = calories.toString(), color = Color.Green, fontSize = 24.sp)
                Text(text = "Left", color = Color.Gray)
                Text(text = remainingCalories.toString(), color = Color.Gray, fontSize = 18.sp)
            }
        }
    }
}
