package com.islam.ecommerce.ui.common.repository

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.islam.ecommerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.islam.ecommerce.data.repository.user.UserPreferenceRepository

class UserViewModel(private val userPreferenceRepository: UserPreferenceRepository) : ViewModel() {

}