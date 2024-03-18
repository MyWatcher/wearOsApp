package com.eipsaferoad.owl.presentation.home

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
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import com.eipsaferoad.owl.utils.LocalStorage

@Composable
fun Home(currentHeartRate: String, navController: NavHostController) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = "Favorite Icon",
                tint = Color.Red
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                text = currentHeartRate,
                fontSize = 40.sp
            )
            Button(onClick = {
                navController.navigate(PagesEnum.SETTINGS.value)
            }) {
                Text(
                    text = "Settings",
                )
            }
    }
}

@Composable
@Preview
fun PreviewHome() {
    val navController = rememberSwipeDismissableNavController()
    OwlTheme {
            Home(currentHeartRate = "42", navController)
    }
}
