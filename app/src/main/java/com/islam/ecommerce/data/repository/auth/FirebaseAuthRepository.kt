package com.islam.ecommerce.data.repository.auth

import com.islam.ecommerce.data.models.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun loginWithEmailAndPassword(email: String, password: String): Flow<Resource<String>>
    suspend fun loginWithGoogle(idToken: String): Flow<Resource<String>>

    suspend fun loginWithFacebook(token: String): Flow<Resource<String>>


    fun logout()
}