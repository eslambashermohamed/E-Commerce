package com.islam.ecommerce.data.repository.user

import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.data.models.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface UserFirestoreRepository {
      suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>>

}