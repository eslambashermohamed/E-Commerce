package com.islam.ecommerce

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.islam.ecommerce.utils.CustomCrashlyticsLogException
import com.islam.ecommerce.utils.LoginException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        listenToNetworkConnection()
    }
    fun listenToNetworkConnection(){
        ReactiveNetwork
            .observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{isConnected:Boolean ->
                FirebaseCrashlytics.getInstance().setCustomKey("connected to internet = ",isConnected)
                FirebaseCrashlytics.getInstance().recordException(LoginException("hello here"))
            }
    }


}