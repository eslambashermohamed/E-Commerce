package com.islam.ecommerce.data.datasource.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesDataSource  @Inject constructor(private val context: Context) {

    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[DataStoreKeys.IS_USER_LOGGED_IN] = isLoggedIn
        }
    }
    val isUserLoggedIn: Flow<Boolean> = context.appDataStore.data.map { preferences ->
            preferences[DataStoreKeys.IS_USER_LOGGED_IN] ?: false
        }

}
