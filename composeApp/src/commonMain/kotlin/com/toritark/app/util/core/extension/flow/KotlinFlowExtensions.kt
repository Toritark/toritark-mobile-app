package com.toritark.app.util.core.extension.flow

import kotlinx.coroutines.flow.*

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flow<T>.ignoreValue(): Flow<Unit> {
    return this.map { }
}

inline fun unitFlow(crossinline block: suspend FlowCollector<Unit>.() -> Unit): Flow<Unit> {
    return flow {
        block()
        emit(Unit)
    }
}

fun <T> errorFlow(throwable: Throwable): Flow<T> {
    return flow {
        throw throwable
    }
}

inline fun <T> typedFlow(crossinline block: suspend FlowCollector<T>.() -> T): Flow<T> {
    return flow {
        val value = block()

        emit(value)
    }
}

inline fun <T> optionalTypedFlow(crossinline block: suspend FlowCollector<T>.() -> T?): Flow<T> {
    return flow {
        val value = block()

        if (value != null) {
            emit(value)
        }
    }
}

fun <T> Flow<T>.repeat(times: Int): Flow<T> {
    val map = mutableMapOf<T, Int>()
    return transform { value ->
        val count = (map[value] ?: 0) + 1
        map[value] = count
        if (count >= times) {
            return@transform emit(value)
        }
    }
}
