package com.example.caloriesapp.domain.viewmodel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.unit.dp

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

    Box(modifier = Modifier.fillMaxWidth()) {
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
                label = { Text("Enter food") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(0.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
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
        Button(
            onClick = {
                onSuggestionSelected(query)
                onQueryChanged("")
                expanded = false
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) {
            Text("Search", color = Color.White)
        }
    }
}
