package com.toritark.app.di.module

import com.toritark.app.di.name.DispatchersNames
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformCoreModule = module {
    single(named(DispatchersNames.IO)) { Dispatchers.Default }
}