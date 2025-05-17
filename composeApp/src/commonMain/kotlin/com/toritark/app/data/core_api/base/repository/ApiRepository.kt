package com.toritark.app.data.core_api.base.repository

import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher

interface ApiRepository

abstract class BaseApiRepository(
    protected val httpClient: HttpClient,
    protected val ioDispatcher: CoroutineDispatcher,
    protected val defaultDispatcher: CoroutineDispatcher,
) : ApiRepository