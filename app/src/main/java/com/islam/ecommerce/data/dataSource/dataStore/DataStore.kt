package com.islam.ecommerce.data.dataSource.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.islam.ecommerce.data.dataSource.dataStore.DataStoreKeys.E_COMERCE_PREFRENCE


object DataStoreKeys {
    const val E_COMERCE_PREFRENCE = "e_comerce prefrence"
    val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
    val USER_ID= stringPreferencesKey("user_id")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = E_COMERCE_PREFRENCE)
