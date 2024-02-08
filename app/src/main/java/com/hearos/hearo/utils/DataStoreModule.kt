package com.hearos.hearo.utils

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import java.io.IOException

class HearoApplication : Application() {
    //private lateinit var dataStore: DataStoreModule
    companion object {
        lateinit var dataStore: DataStoreModule
//        private lateinit var hearoApp: HearoApplication
//        fun getInstance() : HearoApplication = hearoApp
    }

    override fun onCreate() {
        super.onCreate()
        dataStore = DataStoreModule(applicationContext)
//        hearoApp = this
//        dataStore = DataStoreModule(this)
    }
//    fun getDataStore() : DataStoreModule = dataStore
}
class DataStoreModule(val context: Context) {
    private val dataStore = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    var dsUid: String?
        get() =dataStore.getString("uid", "")
        set(value) = dataStore.edit().putString("uid", value!!).apply()
    var dsName: String?
        get() =dataStore.getString("name", "")
        set(value) = dataStore.edit().putString("name", value!!).apply()

//    private object PreferenceKeys {
//        val UID = stringPreferencesKey("uid")
//        val NICKNAME = stringPreferencesKey("nickName")
//    }
//    private val Context.dataStore by preferencesDataStore(name = "dataStore")
//
//    suspend fun getId() : String? {
//        val uidData : Flow<String> = context.dataStore.data
//            .catch { exception ->
//                when (exception) {
//                    is IOException -> emit(emptyPreferences())
//                    else -> throw exception
//                }
//            }
//            .map { preferences ->
//                preferences[UID]!!
//            }
//        return uidData.firstOrNull().orEmpty()
//    }
//
//
//    suspend fun getName() : String? {
//        val flow : Flow<String> = context.dataStore.data
//            .catch { exception ->
//                when (exception) {
//                    is IOException -> emit(emptyPreferences())
//                    else -> throw exception
//                }
//            }
//            .map { preferences ->
//                preferences[NICKNAME]!!
//            }
//        return flow.firstOrNull().orEmpty()
//    }
//
//    suspend fun setUserInfo(uid: String, nickName: String) {
//        context.dataStore.edit { preferences ->
//            preferences[UID] = uid
//            preferences[NICKNAME] =nickName
//        }
//    }
}