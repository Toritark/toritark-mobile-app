package com.toritark.app.di.module

import com.toritark.app.domain.core.language.GetDeviceLanguageCode
import com.toritark.app.domain.core.language.GetDeviceLanguageCodeImpl
import org.koin.dsl.module

actual val platformCoreModule = module {

    single<GetDeviceLanguageCode> {
        GetDeviceLanguageCodeImpl()
    }
}