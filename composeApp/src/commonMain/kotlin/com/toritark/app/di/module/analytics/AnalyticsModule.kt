package com.toritark.app.di.module.analytics

import org.koin.dsl.module

val analyticsModule = module {

    includes(analyticsPlatformModule)
}