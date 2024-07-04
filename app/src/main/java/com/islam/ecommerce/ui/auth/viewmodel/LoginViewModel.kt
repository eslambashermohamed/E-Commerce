package com.islam.ecommerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.islam.ecommerce.data.repository.user.UserPreferenceRepository
import com.islam.ecommerce.utils.isValidEmail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _loginState = MutableSharedFlow<Resource<String>>()

    val loginState: SharedFlow<Resource<String>> = _loginState.asSharedFlow()
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    val isLoginValid = combine(email, password) { email, password ->
        email.isValidEmail() && password.length >= 6
    }

    fun loginWithFacebook(token: String) = viewModelScope.launch {
        firebaseAuthRepository.loginWithFacebook(token).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
                }
                else -> _loginState.emit(resource)
            }
        }.launchIn(viewModelScope)
    }
    fun loginWithEmailAndPassword() {
        GlobalScope.launch {
            val email = email.value
            val password = password.value
            if (isLoginValid.first()) {
                firebaseAuthRepository.loginWithEmailAndPassword(email, password)
                    .onEach { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                _loginState.emit(Resource.Loading())
                            }
                            else -> _loginState.emit(resource)
                        }
                    }.launchIn(viewModelScope)
            } else {
                _loginState.emit(Resource.Error(Exception("Invalid email or password")))
            }
        }


    }

    fun loginWithGoogle(idToken: String) = viewModelScope.launch {
        firebaseAuthRepository.loginWithGoogle(idToken).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    //TODO get user details from the user id
                    _loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
                }

                else -> _loginState.emit(resource)
            }
        }.launchIn(viewModelScope)
    }

}

class LoginViewModelFactory(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(
                userPreferenceRepository, firebaseAuthRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}