package com.islam.ecommerce.di
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.islam.ecommerce.data.datasource.datastore.AppPreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  AppModule {

    @Provides
    @Singleton
    fun getFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun getFireStoreAuth():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun getAppPreferenceDataStore(@ApplicationContext context: Context):AppPreferencesDataSource{
        return AppPreferencesDataSource(context)
    }

    @Provides
    @Singleton
    fun getContext(@ApplicationContext context:Context):Context{
        return context
    }

}