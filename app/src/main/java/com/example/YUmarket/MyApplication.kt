package com.example.YUmarket

import android.app.Application
import android.content.Context
import com.example.YUmarket.di.appModule
import com.example.YUmarket.util.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        prefs = SharedPreferences(applicationContext)
        super.onCreate()
        appContext = this

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(appModule)
        }

    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {

        var appContext: Context? = null
            private set


        lateinit var prefs : SharedPreferences

    }
}