package com.example.caloriesapp.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.caloriesapp.data.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "suggestions")

object PreferencesKeys {
    val SUGGESTIONS = stringPreferencesKey("suggestions")
    val FOODS = stringPreferencesKey("foods")
}

suspend fun saveSuggestions(context: Context, suggestions: List<String>) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.SUGGESTIONS] = suggestions.joinToString(",")
    }
}

fun getSuggestions(context: Context): Flow<List<String>> {
    return context.dataStore.data.map { preferences ->
        val suggestionsString = preferences[PreferencesKeys.SUGGESTIONS] ?: ""
        if (suggestionsString.isEmpty()) emptyList() else suggestionsString.split(",")
    }
}

suspend fun saveFoods(context: Context, foods: List<Food>) {
    context.dataStore.edit { preferences ->
        val foodsString = Json.encodeToString(foods)
        preferences[PreferencesKeys.FOODS] = foodsString
    }
}

fun getFoods(context: Context): Flow<List<Food>> {
    return context.dataStore.data.map { preferences ->
        val foodsString = preferences[PreferencesKeys.FOODS] ?: ""
        if (foodsString.isEmpty()) emptyList() else Json.decodeFromString(foodsString)
    }
}
