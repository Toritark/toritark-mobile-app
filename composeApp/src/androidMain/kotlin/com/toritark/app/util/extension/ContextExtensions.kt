package com.toritark.app.util.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

val Context.activity: Activity?
    get() {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.activity
            else -> null
        }
    }