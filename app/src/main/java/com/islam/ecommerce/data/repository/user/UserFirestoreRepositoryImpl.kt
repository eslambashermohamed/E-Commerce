package com.islam.ecommerce.data.repository.user

import com.google.firebase.firestore.FirebaseFirestore
import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.data.models.user.UserDetailsModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

class UserFirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserFirestoreRepository {

    override suspend fun getUserDetails(userId: String):Flow<Resource<UserDetailsModel>> =
        callbackFlow {
            send(Resource.Loading())
            val documentPath = "users/$userId"
            val document = firestore.document(documentPath)
            val listener = document.addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                value?.toObject(UserDetailsModel::class.java)?.let {
                    trySend(Resource.Success(it))
                } ?: run {
                    close(UserNotFoundException("User not found"))
                }
            }
            awaitClose {
                listener.remove()
            }
        }
}

class UserNotFoundException(message: String) : Exception(message)