package com.toritark.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.ads.provider.AdsProvider
import com.toritark.app.domain.ads.provider.AndroidAdsProvider
import com.toritark.app.domain.billing.interactor.BillingInteractor
import com.toritark.app.presentation.main.app.AppTheme
import com.toritark.app.presentation.main.app.MainApp
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    private val logger = Logger.withTag(LOG_TAG)

    private lateinit var adsProvider: AdsProvider
    private lateinit var adsInteractor: AdsInteractor
    private lateinit var billingInteractor: BillingInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        initializeAnalytics()
        initializeAds()
        initializeBilling()

        setContent {
            AndroidAppView()
        }
    }

    private fun initializeAnalytics() {
        // TODO
    }

    private fun initializeAds() {
        logger.d { "initializeAds" }

        adsProvider = get()
        (adsProvider as AndroidAdsProvider).setActivity(this)

        adsInteractor = get()

        adsInteractor.initialize()
    }

    private fun initializeBilling() {
        logger.d { "initializeBilling" }

        billingInteractor = get()
        billingInteractor.initialize()
    }

    override fun onDestroy() {
        logger.d { "onDestroy" }

        (adsProvider as AndroidAdsProvider).setActivity(null)

        super.onDestroy()
    }

    private companion object {
        private const val LOG_TAG = "MainActivity"
    }
}

@Composable
fun AndroidAppView() {
    AppTheme {
        MainApp()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AndroidAppView()
}