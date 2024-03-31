package com.eipsaferoad.owl.presentation.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import com.eipsaferoad.owl.utils.LocalStorage

@Composable
fun Home(currentHeartRate: String, context: Context, navController: NavHostController) {
        if (currentHeartRate.toInt() < 50 && currentHeartRate.toInt() != 0) {
            navController.navigate(PagesEnum.ALARM.value)
        }
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
            Buttons(context = context, navController = navController)
    }
}

@Composable
fun Buttons(context: Context, navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            modifier = Modifier
                .width(200.dp),
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            onClick = {
                navController.navigate(PagesEnum.SETTINGS.value)
            }
        ) {
            Text(
                fontSize = 30.sp,
                text = "ALARM",
            )
        }
        Button(
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.tertiary),
            onClick = {
                LocalStorage.deleteData(context, "email")
                LocalStorage.deleteData(context, "password")
                navController.navigate(PagesEnum.LOGIN.value)
            }
        ) {
            Text(
                fontSize = 20.sp,
                text = "DISCONNECTION",
            )
        }
    }
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
fun PreviewHome() {
    val navController = rememberSwipeDismissableNavController()
    OwlTheme {
            Home(currentHeartRate = "42", LocalContext.current,  navController)
    }
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
fun PreviewButtons() {
    val navController = rememberSwipeDismissableNavController()
    OwlTheme {
        Buttons(LocalContext.current,  navController)
    }
}
