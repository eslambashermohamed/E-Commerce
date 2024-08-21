package com.islam.ecommerce.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.islam.ecommerce.BuildConfig
import com.islam.ecommerce.R
import com.islam.ecommerce.data.models.Resource
import com.islam.ecommerce.databinding.FragmentRegisterBinding
import com.islam.ecommerce.ui.auth.viewmodel.RegisterViewModel
import com.islam.ecommerce.ui.common.BaseFragment
import com.islam.ecommerce.ui.common.views.AlertDialog
import com.islam.ecommerce.ui.showSnakeBarError
import com.islam.ecommerce.utils.CrashlyticsUils
import com.islam.ecommerce.utils.RegisterException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {
    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManager: LoginManager by lazy { LoginManager.getInstance() }


    override val viewModel: RegisterViewModel by viewModels ()

    override fun getResId() = R.layout.fragment_register

    override fun init() {
        iniListeners()
        initViewModel()
    }


    private fun iniListeners() {
        binding.signUpTx.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.registerWithGoogle.setOnClickListener {
            registerWithGoogleRequest()
        }
        binding.registerWithFacebook.setOnClickListener {
            registerWithFacebook()
        }
    }

    fun initViewModel() {
        lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                state?.let { resources ->
                    when (resources) {
                        is Resource.Loading -> {
                            progressDialog.show()
                        }

                        is Resource.Success -> {
                            progressDialog.dismiss()
                            AlertDialog.showMaterialAlertDialog(requireActivity(), requireView())
                        }

                        is Resource.Error -> {
                            progressDialog.dismiss()
                            val msg =
                                resources.exception?.message ?: getString(R.string.generic_err_msg)
                            view?.showSnakeBarError(
                                resources.exception?.message ?: getString(R.string.generic_err_msg)
                            )
                        }
                    }
                }
            }
        }
    }

    fun registerWithGoogleRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.clientServerId)
            .requestEmail()
            .requestProfile()
            .requestServerAuthCode(BuildConfig.clientServerId)
            .build()
        val gsc = GoogleSignIn.getClient(requireActivity(), gso)
        gsc.signOut()
        val signInIntent = gsc.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignUpResult(task)
            } else {
                view?.showSnakeBarError(getString(R.string.google_sign_in_field_msg))
            }
        }

    private fun handleSignUpResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            viewModel.registerWithGoogle(account.idToken!!)

        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
            val msg = e.message ?: getString(R.string.generic_err_msg)
            logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUils.sendCustomLogToCrashlytics<RegisterException>(
            msg,
            CrashlyticsUils.REGISTER_KEY to msg,
            CrashlyticsUils.LOGIN_PROVIDER to provider,
        )
    }

    private fun registerWithFacebook() {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken.token
                viewModel.registerWithFacebook(token)
            }

            override fun onCancel() {}

            override fun onError(error: FacebookException) {
                // val msg = error.message ?: getString(R.string.generic_err_msg)
                //view?.showSnakeBarError(msg)
                //logAuthIssueToCrashlytics(msg, "Facebook")
            }
        })
        loginManager.logInWithReadPermissions(
            this,
            callbackManager,
            listOf("email", "public_profile")
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}