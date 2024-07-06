package com.eipsaferoad.owl.presentation

import androidx.compose.runtime.Composable

enum class PagesEnum(val value: String) {
    LOGIN("login"),
    HOME("home"),
    SETTINGS("settings"),
}

typealias ComposableFun = @Composable () -> Unit
