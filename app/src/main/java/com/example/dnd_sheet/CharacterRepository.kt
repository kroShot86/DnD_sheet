package com.example.dnd_sheet

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.dnd_sheet.Models.CharacterAttributes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "characters_data")

class CharacterRepository(private val context: Context) {
    private val CHARACTERS_KEY = stringPreferencesKey("characters_list")
    private val json = Json { ignoreUnknownKeys = true }


    suspend fun saveCharacters(characters: List<CharacterAttributes>) {
        val jsonString = json.encodeToString(characters)
        context.dataStore.edit { prefs ->
            prefs[CHARACTERS_KEY] = jsonString
        }
    }


    suspend fun loadCharacters(): List<CharacterAttributes> {
        val jsonString = context.dataStore.data.map { it[CHARACTERS_KEY] ?: "[]" }.first()
        return try {
            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}