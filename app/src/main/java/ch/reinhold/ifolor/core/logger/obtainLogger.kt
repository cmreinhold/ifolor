package ch.reinhold.ifolor.core.logger

import android.util.Log
import ch.reinhold.ifolor.BuildConfig

fun obtainLogger(tag: String) = Logger(tag)

class Logger(private val tag: String) {

    fun debug(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun error(message: String, e: Throwable) {
        Log.e(tag, message, e)
    }
}
