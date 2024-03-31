package com.eipsaferoad.owl.presentation.home

import android.content.Context
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ScrollingView
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
fun Home(currentHeartRate: MutableState<String>, context: Context, navController: NavHostController) {
    if (currentHeartRate.value.toInt() < 50 && currentHeartRate.value.toInt() != 0) {
        Alarm(currentHeartRate, context, navController)
    } else {
        NoAlarm(currentHeartRate.value, context, navController)
    }
}

@Composable
fun NoAlarm(currentHeartRate: String, context: Context, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.height(200.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier.padding(bottom =  100.dp, top = 70.dp),
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
                }
            }
            item {
                Buttons(context = context, navController = navController)
            }
        }
    }
}

@Composable
fun Buttons(context: Context, navController: NavHostController) {
    Column(
        modifier = Modifier.padding(bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            modifier = Modifier
                .width(150.dp),
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
            modifier = Modifier.width(150.dp),
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.tertiary),
            onClick = {
                LocalStorage.deleteData(context, "email")
                LocalStorage.deleteData(context, "password")
                navController.navigate(PagesEnum.LOGIN.value)
            }
        ) {
            Text(
                fontSize = 17.sp,
                text = "DISCONNECTION",
            )
        }
    }
}

@Composable
fun CircularColumn(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .border(5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            .clip(CircleShape),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        content()
    }
}

@Composable
fun Alarm(currentHeartRate: MutableState<String>, context: Context, navController: NavHostController) {
    CircularColumn {
        Column(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                    text = currentHeartRate.value,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "SOS",
                    fontSize = 30.sp
                )
                Button(
                    onClick = {
                        currentHeartRate.value = "100"
                    }
                ) {
                    CircleIcon(icon = Icons.Rounded.Clear, tint = MaterialTheme.colorScheme.surface)
                }
            }
        }
    }
}

@Composable
fun CircleIcon(icon: ImageVector, tint: Color) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape) // Background circle
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp),
            tint = tint,
            imageVector = icon,
            contentDescription = "Icon description"
        )
    }
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
fun PreviewHome() {
    val navController = rememberSwipeDismissableNavController()
    /*OwlTheme {
            Home(currentHeartRate = "42", LocalContext.current,  navController)
    }*/
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
fun PreviewButtons() {
    val navController = rememberSwipeDismissableNavController()
    OwlTheme {
        Buttons(LocalContext.current,  navController)
    }
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
fun PreviewAlarm() {
    val navController = rememberSwipeDismissableNavController()
    /*OwlTheme {
        Alarm(currentHeartRate = bpm, LocalContext.current,  navController)
    }*/
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
fun PreviewNoAlarm() {
    val navController = rememberSwipeDismissableNavController()
    OwlTheme {
        NoAlarm(currentHeartRate = "42", LocalContext.current,  navController)
    }
}
