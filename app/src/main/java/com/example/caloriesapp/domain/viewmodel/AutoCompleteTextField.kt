package com.example.caloriesapp.domain.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AutoCompleteTextField(
    suggestions: List<String>,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSuggestionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val filteredSuggestions = suggestions.filter { it.contains(query, ignoreCase = true) }

    if (filteredSuggestions.isEmpty()) {
        expanded = false
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(0.dp,0.dp)
        .background(Color(0xFFF5F5F5), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center, // Center vertically
            horizontalAlignment = Alignment.Start // Center horizontally if needed
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    if (filteredSuggestions.isNotEmpty()) {
                        expanded = !expanded
                    }
                }
            ) {
                TextField(
                    value = query,
                    onValueChange = {
                        onQueryChanged(it)
                        expanded = true
                    },
                    placeholder = { Text("Search") },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent)
                        .padding(10.dp, 0.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filteredSuggestions.forEach { suggestion ->
                        DropdownMenuItem(
                            onClick = {
                                onSuggestionSelected(suggestion)
                                onQueryChanged(suggestion)
                                expanded = false
                            }
                        ) {
                            Text(text = suggestion)
                        }
                    }
                }
            }
        }
        IconButton(
            onClick = {
                onSuggestionSelected(query)
                onQueryChanged("")
                expanded = false
            },
            modifier = Modifier.align(Alignment.CenterEnd) // Align the IconButton to the end
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_search_category_default),
                contentDescription = "Search Icon",
                tint = Color.Black
            )
        }
    }
}
