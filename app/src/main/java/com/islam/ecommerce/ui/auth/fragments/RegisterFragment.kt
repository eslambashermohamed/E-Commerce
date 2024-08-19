package com.islam.ecommerce.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.islam.ecommerce.ui.auth.viewmodel.RegisterviewModelFactory
import com.islam.ecommerce.ui.common.views.AlertDialog
import com.islam.ecommerce.ui.common.views.ProgressDialog
import com.islam.ecommerce.ui.showSnakeBarError
import com.islam.ecommerce.utils.CrashlyticsUils
import com.islam.ecommerce.utils.RegisterException
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding;
    val registerViewModel: RegisterViewModel by viewModels {
        RegisterviewModelFactory(requireContext())
    }

    val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.registerViewModel = registerViewModel
        binding.lifecycleOwner = this
        iniListeners()
        initViewModel()
        return binding.root
    }

    private fun iniListeners() {
        binding.signUpTx.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.registerWithGoogle.setOnClickListener {
            registerWithGoogleRequest()
        }
        binding.registerWithFacebook.setOnClickListener {

        }
    }

    fun initViewModel() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { state ->
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
            registerViewModel.registerWithGoogle(account.idToken!!)

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
}