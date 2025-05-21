package com.toritark.app.data.core.model

sealed class Environment {
    data object Local : Environment()
    data object Production : Environment()
}