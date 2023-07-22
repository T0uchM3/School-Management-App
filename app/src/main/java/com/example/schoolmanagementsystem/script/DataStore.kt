package com.example.schoolmanagementsystem.script

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Arrays

class StoreData(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val login = stringPreferencesKey("login")
        val lang = stringPreferencesKey("lang")
        val mail = stringPreferencesKey("mail")
        val mdp = stringPreferencesKey("mdp")

    }


    suspend fun saveToDataStore(newLang: String) {
        context.dataStore.edit {
            it[lang] = newLang
        }
    }

    //    suspend fun saveNewLoginState(newState: String) {
//        val byteArray = newState.toByteArray()
//        println("******************************* "+ byteArray.contentToString())
//        context.dataStore.edit {
//            it[login] = newState
//        }
//    }
    suspend fun saveNewLoginState(newState: String) {
        val byteArray = newState.toByteArray()
        context.dataStore.edit {
            it[login] = byteArray.contentToString()
        }
    }

    suspend fun saveMail(newVal: String) {
        context.dataStore.edit {
            it[mail] = newVal
        }
    }

    //    suspend fun saveMdp(newVal: String) {
//        val byteArray = newVal.toByteArray()
//        context.dataStore.edit {
//            it[mdp] = byteArray
//        }
//    }
    suspend fun saveMdp(newVal: String) {
        val byteArray = newVal.toByteArray()
        context.dataStore.edit {
            it[mdp] =  byteArray.contentToString()
        }
    }

    val getFromDataStore: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[lang] ?: ""
    }
    val getLoginState: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[login] ?: ""
    }
    val getMail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[mail] ?: ""
    }

    //    val getMdp: Flow<String> = context.dataStore.data.map { preferences ->
//        preferences[mdp] ?: ""
//    }
    val getMdp: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[mdp] ?: ""
    }

    suspend fun clearDataStore() = context.dataStore.edit {
        it.clear()
    }
}