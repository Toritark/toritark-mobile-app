package com.toritark.app

import android.app.Application
import co.touchlab.kermit.Logger
import com.toritark.app.di.configureModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private val logger = Logger.withTag(LOG_TAG)

    override fun onCreate() {
        super.onCreate()

        initializeDi()
    }

    private fun initializeDi() {
        logger.d { "initializeDi" }

        startKoin {
            androidContext(this@App)

            configureModules()
        }
    }

    private companion object {
        private const val LOG_TAG = "App"
    }
}