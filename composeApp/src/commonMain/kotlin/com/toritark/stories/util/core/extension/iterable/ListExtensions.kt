package com.toritark.stories.util.core.extension.iterable

fun <T> List<T>.replaceItemAt(index: Int, newItem: T): List<T> {
    return mapIndexed { i, existingItem ->
        if (i == index) newItem else existingItem
    }
}