package com.islam.ecommerce.data.repository.common

import com.islam.ecommerce.data.datasource.datastore.AppPreferencesDataSource
import kotlinx.coroutines.flow.Flow

class AppDataStoreRepositoryImpl(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : AppPreferenceRepository {

    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        appPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun isLoggedIn(): Flow<Boolean> {
        return appPreferencesDataSource.isUserLoggedIn
    }
}
