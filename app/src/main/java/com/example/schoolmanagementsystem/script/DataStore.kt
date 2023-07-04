package com.example.schoolmanagementsystem.script

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val loggeIn = stringPreferencesKey("store_Data")
        val lang = stringPreferencesKey("lang")

    }


    suspend fun saveToDataStore(newLang: String) {
        context.dataStore.edit {
            it[lang] = newLang
        }
    }

    val getFromDataStore: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[lang] ?: ""

    }

    suspend fun clearDataStore() = context.dataStore.edit {
        it.clear()
    }
}