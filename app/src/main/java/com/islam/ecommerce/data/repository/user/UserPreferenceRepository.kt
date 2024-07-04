package com.islam.ecommerce.data.repository.user

import com.islam.ecommerce.data.models.Resource
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    suspend fun isUserLoggedIn(): Flow<Boolean>
    suspend fun saveLoginState(isLoggedIn:Boolean)

}