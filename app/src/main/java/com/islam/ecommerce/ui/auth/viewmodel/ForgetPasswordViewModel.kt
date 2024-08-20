package com.islam.ecommerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.islam.ecommerce.ui.common.views.ProgressDialog
import com.islam.ecommerce.utils.isValidEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(val authRepository: FirebaseAuthRepository) : ViewModel() {
    val email = MutableStateFlow("")
    private val _forgetPasswordState = MutableSharedFlow<Resource<String>>()
    val forgetPasswordState: SharedFlow<Resource<String>> = _forgetPasswordState

    fun sendUpdatePassword() = viewModelScope.launch(Dispatchers.IO) {

        if (email.value.isValidEmail()) {
            authRepository.sendEmailToUpdatePassword(email.value).collect{
                _forgetPasswordState.emit(it)
            }
        }

}
}

class ForgetPasswordViewModelFactory(val authRepository: FirebaseAuthRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) {
            return ForgetPasswordViewModel(authRepository) as T
        }
        throw Throwable("unknown viewModel")
    }
}