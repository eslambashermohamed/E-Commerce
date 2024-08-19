package com.islam.ecommerce.ui.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.islam.ecommerce.data.datasource.datastore.AppPreferencesDataSource
import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.data.models.user.UserDetailsModel
import com.islam.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.islam.ecommerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.islam.ecommerce.data.repository.common.AppDataStoreRepositoryImpl
import com.islam.ecommerce.data.repository.common.AppPreferenceRepository
import com.islam.ecommerce.data.repository.user.UserPreferenceRepository
import com.islam.ecommerce.data.repository.user.UserPreferenceRepositoryImpl
import com.islam.ecommerce.utils.isValidEmail
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val appPreferenceRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    val email = MutableStateFlow("")
    val name = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
    val _registerState= MutableSharedFlow<Resource<UserDetailsModel>>()
    val registerState:SharedFlow<Resource<UserDetailsModel>> = _registerState
    fun registerWithEmailAndPassword() = viewModelScope.launch(IO) {
        val name = name.value
        val email = email.value
        val password = password.value
        val confirmPassword = confirmPassword.value
        if (isRegisterValidate().first()) {
              firebaseAuthRepository.registerWithEmailAndPassword(email,password,name).collect{
                  _registerState.emit(it)
              }
        } else {

        }
    }

    fun registerWithGoogle(idToken: String) {

    }

    fun registerWithFacebook(token: String) {
    }

    fun isRegisterValidate() =
        combine(name, email, password, confirmPassword) { name, email, password, confirmPassword ->
            email.isValidEmail() && name.isNotEmpty() && password.length >= 6 && confirmPassword.isNotEmpty() && password == confirmPassword
        }

}

class RegisterviewModelFactory(
    private val contextValue: Context
) :
    ViewModelProvider.Factory {
    private val appPreferenceRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(contextValue))
    private val userPreferenceRepository = UserPreferenceRepositoryImpl(contextValue)
    private val authRepository = FirebaseAuthRepositoryImpl()
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                appPreferenceRepository,
                userPreferenceRepository,
                authRepository
            ) as T
        }
        throw IllegalArgumentException("un nowen viewModel class")
    }
}