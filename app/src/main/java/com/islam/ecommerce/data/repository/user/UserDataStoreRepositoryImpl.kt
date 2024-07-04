package com.islam.ecommerce.data.repository.user

import androidx.datastore.preferences.core.edit
import com.islam.ecommerce.data.dataSource.dataStore.DataStoreKeys.IS_USER_LOGGED_IN
import com.islam.ecommerce.data.dataSource.dataStore.UserPreferencesDataStore
import com.islam.ecommerce.data.dataSource.dataStore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStoreRepositoryImpl(val userPrefDataStore: UserPreferencesDataStore) :
    UserPreferenceRepository {
    override suspend fun isUserLoggedIn(): Flow<Boolean> {
       return isUserLoggedIn()
    }

    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        saveLoginState(isLoggedIn)
    }


}