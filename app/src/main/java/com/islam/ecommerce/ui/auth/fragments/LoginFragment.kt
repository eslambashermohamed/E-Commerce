package com.islam.ecommerce.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
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
import com.islam.ecommerce.data.models.user.UserDetailsModel
import com.islam.ecommerce.databinding.FragmentLoginBinding
import com.islam.ecommerce.ui.auth.ForgetPasswordFragment
import com.islam.ecommerce.ui.auth.viewmodel.LoginViewModel
import com.islam.ecommerce.ui.auth.viewmodel.LoginViewModelFactory
import com.islam.ecommerce.ui.common.views.ProgressDialog
import com.islam.ecommerce.ui.home.MainActivity
import com.islam.ecommerce.ui.showSnakeBarError
import com.islam.ecommerce.utils.CrashlyticsUils
import com.islam.ecommerce.utils.LoginException
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManager: LoginManager by lazy { LoginManager.getInstance() }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val progressDialog by lazy {
        ProgressDialog.createProgressDialog(requireActivity())
    }

    val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginViewModel = loginViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViewModel()
    }

    fun initViewModel() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect { state ->
                state?.let { resources ->
                    when (resources) {
                        is Resource.Loading -> {
                            progressDialog.show()
                        }

                        is Resource.Success -> {
                            progressDialog.dismiss()

                            Toast.makeText(requireActivity(), "success", Toast.LENGTH_LONG).show()
                            goToHome()
                        }

                        is Resource.Error -> {
                            progressDialog.dismiss()
                            val msg =
                                resources.exception?.message ?: getString(R.string.generic_err_msg)
                            view?.showSnakeBarError(
                                resources.exception?.message ?: getString(R.string.generic_err_msg)
                            )
                            // logAuthIssueToCrashlytics(msg, "Login Error")
                        }
                    }
                }
            }
        }
    }


    fun loginWithGoogleRequest() {
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
                handleSignInResult(task)
            } else {
                view?.showSnakeBarError(getString(R.string.google_sign_in_field_msg))
            }
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            loginViewModel.loginWithGoogle(account.idToken!!)

        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
            val msg = e.message ?: getString(R.string.generic_err_msg)
            logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun goToHome() {
        requireActivity().startActivity(Intent(activity, MainActivity::class.java).apply {
            // flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUils.LOGIN_KEY to msg,
            CrashlyticsUils.LOGIN_PROVIDER to provider,
        )
    }

    private fun initListeners() {
        binding.loginWithGoogle.setOnClickListener {
            loginWithGoogleRequest()
        }
        binding.loginWithFacebook.setOnClickListener {
            if (isLoggedIn()) {
                goToHome()
            } else {
                loginWithFacebook()
            }
        }
        binding.register.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.forgotPassword.setOnClickListener{
            val forgetPasswordFragment=ForgetPasswordFragment()
            forgetPasswordFragment.show(parentFragmentManager,"forget password")
        }
    }

    private fun signOut() {
        loginManager.logOut()
    }

    private fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    private fun loginWithFacebook() {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken.token
                loginViewModel.loginWithFacebook(token)
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