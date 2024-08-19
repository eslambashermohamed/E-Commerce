package com.islam.ecommerce.data.repository.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.data.models.user.AuthProvider
import com.islam.ecommerce.data.models.user.UserDetailsModel
import com.islam.ecommerce.utils.CrashlyticsUils
import com.islam.ecommerce.utils.LoginException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) :
    FirebaseAuthRepository {

    private suspend fun login(
        provider: AuthProvider,
        signInRequest: suspend () -> AuthResult,
    ): Flow<Resource<UserDetailsModel>> = flow {
        try {
            emit(Resource.Loading())

            val authResult = signInRequest()
            val userId = authResult.user?.uid

            if (userId == null) {
                val msg = "Sign in UserID not found"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }
            if (authResult.user?.isEmailVerified() == false) {
                val mag = "Verify Your Email"
                emit(Resource.Error(Exception(mag)))
                return@flow
            }
            // get user details from firestore
            val userDoc = firebaseFirestore.collection("users").document(userId).get().await()

            if (!userDoc.exists()) {
                val msg = "user desn't have data in firebase"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }

            // map user details to UserDetailsModel
            val userDetails = userDoc.toObject(UserDetailsModel::class.java)

            userDetails?.let {
                emit(Resource.Success(userDetails))
            } ?: run {
                val msg = "Error mapping user details to UserDetailsModel, user id = $userId"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
            }
        } catch (e: Exception) {
            logAuthIssueToCrashlytics(
                e.message ?: "Unknown error from exception = ${e::class.java}", provider.name
            )
            emit(Resource.Error(e))

        }
    }

    override suspend fun loginWithFacebook(token: String) = login(AuthProvider.FACEBOOK) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).await()
    }

    override suspend fun loginWithGoogle(idToken: String) = login(AuthProvider.GOOGLE) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    override suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ) = login(AuthProvider.EMAIL) {

        auth.signInWithEmailAndPassword(email, password).await()

    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUils.LOGIN_KEY to msg,
            CrashlyticsUils.LOGIN_PROVIDER to provider,
        )
    }


    override fun logout() {
        auth.signOut()
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        name: String
    ): Flow<Resource<UserDetailsModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid
                if (userId == null) {
                    val mag = "user id not fount"
                    logAuthIssueToCrashlytics(mag, AuthProvider.EMAIL.name)
                    emit(Resource.Error(Exception(mag)))
                    return@flow
                }

                val userDetails = UserDetailsModel(id = userId, email = email, name = name)
                firebaseFirestore.collection("users").document(userId!!).set(userDetails).await()
                authResult.user?.sendEmailVerification()
                emit(Resource.Success(userDetails))
            } catch (ex: Exception) {
                emit(Resource.Error(ex))
            }
        }
    }

    override suspend fun registerWithGoogle(idToken: String): Flow<Resource<UserDetailsModel>> =
        flow {
            try {
                emit(Resource.Loading())
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()
                val userId = authResult.user?.uid
                if (userId == null) {
                    val msg = "sign upp UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.GOOGLE.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }
                val userDetails = UserDetailsModel(
                    id = userId,
                    name = authResult.user?.displayName ?: "",
                    email = authResult.user?.email ?: "",
                )
                firebaseFirestore.collection("users").document(userId).set(userDetails).await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "UnKnown error form exception = ${e::class.java}",
                    AuthProvider.GOOGLE.name
                )
                emit(Resource.Error(e))
            }
        }
}