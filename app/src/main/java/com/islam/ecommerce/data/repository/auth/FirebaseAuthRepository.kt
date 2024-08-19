package com.islam.ecommerce.data.repository.auth

import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.data.models.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {

    suspend fun loginWithEmailAndPassword(email: String, password: String): Flow<Resource<UserDetailsModel>>

    suspend fun loginWithGoogle(idToken: String): Flow<Resource<UserDetailsModel>>

    suspend fun loginWithFacebook(token: String): Flow<Resource<UserDetailsModel>>

    fun logout()
    suspend fun registerWithEmailAndPassword(email:String,password: String,name:String):Flow<Resource<UserDetailsModel>>

}