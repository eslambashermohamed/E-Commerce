package com.islam.ecommerce.di
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.islam.ecommerce.data.datasource.datastore.AppPreferencesDataSource
import com.islam.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.islam.ecommerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.islam.ecommerce.data.repository.common.AppDataStoreRepositoryImpl
import com.islam.ecommerce.data.repository.common.AppPreferenceRepository
import com.islam.ecommerce.data.repository.user.UserFirestoreRepository
import com.islam.ecommerce.data.repository.user.UserFirestoreRepositoryImpl
import com.islam.ecommerce.data.repository.user.UserPreferenceRepository
import com.islam.ecommerce.data.repository.user.UserPreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun getPreferenceRepository(
       appPreferenceRepository: AppDataStoreRepositoryImpl
    ): AppPreferenceRepository

    @Binds
    @Singleton
    abstract fun getUserPreferenceRepository(
        userPreferenceRepository: UserPreferenceRepositoryImpl
    ): UserPreferenceRepository

    @Binds
    @Singleton
    abstract fun getFirebaseAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepositoryImpl
    ): FirebaseAuthRepository

    @Binds
    @Singleton
    abstract fun getUserFireStoreRepository(
        userFireStoreRepository:UserFirestoreRepositoryImpl
    ): UserFirestoreRepository

}