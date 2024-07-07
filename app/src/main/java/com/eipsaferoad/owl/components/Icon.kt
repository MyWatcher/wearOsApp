package com.eipsaferoad.owl.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.eipsaferoad.owl.presentation.theme.OwlTheme

@Composable
fun DisplayIcon(imageVector: ImageVector, tint: Color) {
        Icon(
            imageVector,
            contentDescription = "Favorite Icon",
            tint = tint
        )
}

@Composable
@Preview
fun PreviewDisplayIcon() {
    OwlTheme {
        DisplayIcon(Icons.Rounded.Favorite, Color.Blue)
    }
}
