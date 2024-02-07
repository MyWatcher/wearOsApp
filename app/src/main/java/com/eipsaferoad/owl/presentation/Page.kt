package com.eipsaferoad.owl.presentation

import androidx.compose.runtime.Composable

enum class PagesEnum(val value: Int) {
    LOGIN(0),
    HOME(1),
    SETTINGS(2),
}

typealias ComposableFun = @Composable () -> Unit
