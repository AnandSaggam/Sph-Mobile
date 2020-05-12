package com.dlminfosoft.sphmobile

import android.app.Application
import com.dlminfosoft.sphmobile.di.dataModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * This is main application class, load first when app started
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            this@BaseApplication, listOf(dataModule)
        )
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}