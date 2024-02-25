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

    companion object {
        lateinit var dataStore: DataStoreModule

    }

    override fun onCreate() {
        super.onCreate()
        dataStore = DataStoreModule(applicationContext)
    }
}
class DataStoreModule(val context: Context) {
    private val dataStore = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    var dsUid: String?
        get() =dataStore.getString("uid", "")
        set(value) = dataStore.edit().putString("uid", value!!).apply()
    var dsName: String?
        get() =dataStore.getString("name", "")
        set(value) = dataStore.edit().putString("name", value!!).apply()

}