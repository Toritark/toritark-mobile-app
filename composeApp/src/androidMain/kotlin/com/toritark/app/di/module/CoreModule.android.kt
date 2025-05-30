package com.toritark.app.di.module

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.domain.core.language.GetDeviceLanguageCode
import com.toritark.app.domain.core.language.GetDeviceLanguageCodeImpl
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformCoreModule = module {

    single(named(DispatchersNames.IO)) { Dispatchers.IO }

    factory {
        val context: Context = get()
        context.contentResolver
    }

    single {
        val context: Context = get()

        ImageLoader.Builder(context)
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.05)
                    .build()
            }
            .build()
    }

    single<GetDeviceLanguageCode> { GetDeviceLanguageCodeImpl() }
}