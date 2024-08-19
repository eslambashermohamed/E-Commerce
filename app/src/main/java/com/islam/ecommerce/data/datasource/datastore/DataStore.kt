package com.islam.ecommerce.data.datasource.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import com.google.protobuf.InvalidProtocolBufferException
import com.islam.ecommerce.data.datasource.datastore.DataStoreKeys.E_COMMERCE_PREFERENCES
import com.islam.ecommerce.data.datasource.datastore.DataStoreKeys.USER_DETAILS_PREFERENCES_PB
import com.islam.ecommerce.data.models.user.UserDetailsPreferences
import java.io.InputStream
import java.io.OutputStream

object DataStoreKeys {
    const val E_COMMERCE_PREFERENCES = "e_commerce_preferences"
    const val USER_DETAILS_PREFERENCES_PB = "user_details.pb"
    val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
}

val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = E_COMMERCE_PREFERENCES)

val Context.userDetailsDataStore by dataStore(
    fileName = USER_DETAILS_PREFERENCES_PB, serializer = UserDetailsPreferencesSerializer
)

object UserDetailsPreferencesSerializer : Serializer<UserDetailsPreferences> {

    override val defaultValue: UserDetailsPreferences = UserDetailsPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserDetailsPreferences = try {
        UserDetailsPreferences.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: UserDetailsPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}