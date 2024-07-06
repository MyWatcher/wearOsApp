package com.eipsaferoad.owl.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.SwitchDefaults
import com.eipsaferoad.owl.presentation.theme.md_theme_dark_tertiary
import com.eipsaferoad.owl.presentation.theme.md_theme_light_surface

@Composable
fun ToggleSwitch(isActivate: Boolean, action: () -> Unit) {
    Switch(
        colors = SwitchDefaults.colors(
            checkedThumbColor = md_theme_light_surface,
            checkedTrackColor = md_theme_light_surface,
            uncheckedThumbColor = md_theme_light_surface,
            uncheckedTrackColor = md_theme_dark_tertiary
        ),
        checked = isActivate,
        onCheckedChange = {
            action()
        }
    )
}

@Composable
@Preview
fun PreviewToggleSwitchDeactivated() {
    Column(
        modifier = Modifier
            .height(50.dp)
            .width(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToggleSwitch(false, {})
    }
}

@Composable
@Preview
fun PreviewToggleSwitchActivated() {
    Column(
        modifier = Modifier
            .height(50.dp)
            .width(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToggleSwitch(true, {})
    }
}
