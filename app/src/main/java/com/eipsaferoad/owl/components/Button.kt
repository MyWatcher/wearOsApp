package com.eipsaferoad.owl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.eipsaferoad.owl.presentation.theme.md_theme_light_onSurfaceVariant
import com.eipsaferoad.owl.presentation.theme.md_theme_light_primary
import com.eipsaferoad.owl.presentation.theme.md_theme_light_secondary

enum class ButtonTypeEnum() {
    PRIMARY,
    SECONDARY,
    REDIRECTION,
}

@Composable
fun Button(type: ButtonTypeEnum, content: @Composable () -> Unit, action: () -> Unit) {
    val backgroundColor = when  (type) {
        ButtonTypeEnum.PRIMARY -> md_theme_light_primary
        ButtonTypeEnum.SECONDARY -> md_theme_light_secondary
        ButtonTypeEnum.REDIRECTION ->  md_theme_light_onSurfaceVariant
        else -> md_theme_light_onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .clickable { action() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}

@Composable
fun ContentExample() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "coucou")
    }
}

@Composable
@Preview
fun PreviewButtonPrimary() {
    Column(
        modifier = Modifier
            .height(50.dp)
            .width(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(type = ButtonTypeEnum.PRIMARY, content = { ContentExample() }, action = { println("cpicpi") })
    }
}

@Composable
@Preview
fun PreviewButtonSecondary() {
    Column(
        modifier = Modifier
            .height(50.dp)
            .width(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(type = ButtonTypeEnum.SECONDARY, content = { ContentExample() }, action = { println("cpicpi") })
    }
}

@Composable
@Preview
fun PreviewButtonRedirection() {
    Column(
        modifier = Modifier
            .height(50.dp)
            .width(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(type = ButtonTypeEnum.REDIRECTION, content = { ContentExample() }, action = { println("cpicpi") })
    }
}