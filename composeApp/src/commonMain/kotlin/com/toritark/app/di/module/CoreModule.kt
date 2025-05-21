@file:OptIn(ExperimentalSerializationApi::class)

package com.toritark.app.di.module

import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.domain.core.debug.IsDebug
import com.toritark.app.domain.core.debug.IsDebugImpl
import com.toritark.app.domain.core.env.GetEnvironment
import com.toritark.app.domain.core.env.GetEnvironmentImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreModule: Module

val coreModule = module {
    includes(platformCoreModule)
    
    single(named(DispatchersNames.DEFAULT)) { Dispatchers.Default }
    single(named(DispatchersNames.IO)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(DispatchersNames.MAIN)) { Dispatchers.Main }

    single { IsDebugImpl() } bind IsDebug::class

    single<GetEnvironment> {
        GetEnvironmentImpl()
    }

    single {
        val isDebug: IsDebug = get()

        Json {
            prettyPrint = isDebug()
            ignoreUnknownKeys = true
        }
    }
}

