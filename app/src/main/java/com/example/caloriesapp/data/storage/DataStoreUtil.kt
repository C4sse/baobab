package com.example.caloriesapp.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "suggestions")

object PreferencesKeys {
    val SUGGESTIONS = stringPreferencesKey("suggestions")
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
