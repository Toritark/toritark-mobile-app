package com.toritark.app.di.module

import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.domain.core.language.GetDeviceLanguageCode
import com.toritark.app.domain.core.language.GetDeviceLanguageCodeImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformCoreModule = module {

    single(named(DispatchersNames.IO)) { Dispatchers.IO }

    single<GetDeviceLanguageCode> {
        GetDeviceLanguageCodeImpl()
    }
}