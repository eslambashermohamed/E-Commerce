package com.islam.ecommerce

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.TextView
import android.window.SplashScreenView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.islam.ecommerce.utils.AddCartException
import com.islam.ecommerce.utils.CrashlyticsUils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
       initialSplasScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.hello).setOnClickListener{
            CrashlyticsUils.sendCustomLogToCrashlytics<AddCartException>("crash button clicked",Pair("crash key","crashlytics value"))
        }
    }

    private fun initialSplasScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val slideUp=ObjectAnimator.ofFloat(
                    splashScreenView, View.TRANSLATION_Y,0f,-splashScreenView.height.toFloat()
                )
                slideUp.interpolator=AnticipateInterpolator()
                slideUp.duration=1000L
                slideUp.doOnEnd { splashScreenView.remove() }
                slideUp.start()
            }
        }else{
            setTheme(R.style.Theme_ECommerce)
        }
    }
}