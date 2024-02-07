package com.eipsaferoad.owl.presentation.settings

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import com.eipsaferoad.owl.utils.LocalStorage

@Composable
fun Settings(context: Context, changePage: (page: Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            LocalStorage.deleteData(context, "email")
            LocalStorage.deleteData(context, "password")
            changePage(PagesEnum.LOGIN.value)
        }) {
            Text(
                text = "Disconnection",
            )
        }
    }
}

@Composable
@Preview
fun PreviewSettings() {
    OwlTheme {
        /*Settings(changePage = {})*/
    }
}
