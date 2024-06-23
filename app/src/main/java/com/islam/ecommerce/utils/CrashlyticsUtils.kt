package com.islam.ecommerce.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsUils {
    fun sendLogToCrashlytics(msg: String, vararg keys: String) {
        keys.forEach {
            FirebaseCrashlytics.getInstance().setCustomKey(it, msg)

        }
        FirebaseCrashlytics.getInstance().recordException(CustomCrashlyticsLogException(msg))
    }
    fun sendLogToCrashlytics(msg: String, vararg keys: Pair<String,String>) {
        keys.forEach {key->
            FirebaseCrashlytics.getInstance().setCustomKey(key.first, key.second)

        }
        FirebaseCrashlytics.getInstance().recordException(CustomCrashlyticsLogException(msg))
    }
    inline fun<reified T:Exception> sendCustomLogToCrashlytics(msg: String,vararg keys: Pair<String,String>) {
        keys.forEach {key->
            FirebaseCrashlytics.getInstance().setCustomKey(key.first, key.second)

        }
        var excption=T::class.java.getConstructor(String::class.java).newInstance(msg)
        FirebaseCrashlytics.getInstance().recordException(excption)
    }
}

class CustomCrashlyticsLogException(message: String) : Exception(message)
class AddCartException(message:String):Exception(message)
