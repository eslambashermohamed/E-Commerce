package com.islam.ecommerce.ui.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.islam.ecommerce.R
import com.islam.ecommerce.ui.auth.AuthActivity
import com.islam.ecommerce.ui.common.viewmodels.UserViewModel
import com.islam.ecommerce.ui.common.viewmodels.UserViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initialSplasScreen()
        super.onCreate(savedInstanceState)
        val isLoggedIn = runBlocking {
            userViewModel.isUserLoggedIn().first()
        }
        if (!isLoggedIn) {
            goToAuthActivity()
            return
        }
        setContentView(R.layout.activity_main)
        initViewModel()
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            val userDetails = runBlocking { userViewModel.getUserDetails().first() }
            userViewModel.userDetailsState.collect {

            }

        }
    }


    private fun initialSplasScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView, View.TRANSLATION_Y, 0f, -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 1000L
                slideUp.doOnEnd { splashScreenView.remove() }
                slideUp.start()
            }
        } else {
            setTheme(R.style.Theme_ECommerce)
        }
    }
}