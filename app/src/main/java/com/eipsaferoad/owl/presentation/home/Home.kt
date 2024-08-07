package com.eipsaferoad.owl.presentation.home

import android.content.Context
import android.os.Vibrator
import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.eipsaferoad.owl.R
import com.eipsaferoad.owl.components.Button
import com.eipsaferoad.owl.components.ButtonTypeEnum
import com.eipsaferoad.owl.components.DisplayIcon
import com.eipsaferoad.owl.models.Alarm
import com.eipsaferoad.owl.models.SoundAlarm
import com.eipsaferoad.owl.models.VibrationAlarm
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import com.eipsaferoad.owl.utils.EnvEnum
import com.eipsaferoad.owl.utils.KeysEnum
import com.eipsaferoad.owl.utils.LocalStorage
import com.eipsaferoad.owl.utils.getVibrationEffects
import com.eipsaferoad.owl.utils.soundPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource

@Composable
fun Home(currentHeartRate: MutableState<String>, context: Context, navController: NavHostController, alarms: MutableState<Alarm>, mVibrator: Vibrator) {
    if (alarms.value.isAlarmActivate && currentHeartRate.value.toInt() < 50 && currentHeartRate.value.toInt() != 0) {
        Alarm(context, currentHeartRate, alarms, mVibrator)
    } else {
        NoAlarm(currentHeartRate.value, context, navController)
    }
}

@Composable
fun NoAlarm(currentHeartRate: String, context: Context, navController: NavHostController) {
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = 1
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.height(200.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier.padding(top = 80.dp, start = 10.dp, end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        type = ButtonTypeEnum.REDIRECTION,
                        content = {
                            Text(
                                fontSize = 17.sp,
                                text = "DISCONNECTION",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        },
                        action = {
                            LocalStorage.deleteData(context, EnvEnum.EMAIL.value)
                            LocalStorage.deleteData(context, EnvEnum.PASSWORD.value)
                            navController.navigate(PagesEnum.LOGIN.value)
                        }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "logout",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 10.dp)
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(bottom =  100.dp, top = 70.dp),
                            verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DisplayIcon(imageVector = Icons.Rounded.Favorite, tint = Color.Red)
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
                Column(
                    modifier = Modifier.padding(bottom = 60.dp, start = 10.dp, end = 10.dp)
                ) {
                    Button(
                        type = ButtonTypeEnum.PRIMARY,
                        content = {
                            Text(
                                fontSize = 30.sp,
                                text = "ALARM",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 15.dp)
                            )
                        },
                        action = {
                            navController.navigate(PagesEnum.SETTINGS.value)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MultiColorBorderCircularColumn(
    borderColors: List<Color>,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "")

    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 5.dp,
                color = Color.Transparent,
                shape = CircleShape
            )
            .graphicsLayer(
                rotationZ = rotation
            ),
        contentColor = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 7.dp,
                    brush = borderBrushMultiColor(borderColors),
                    shape = CircleShape
                )
                .background(MaterialTheme.colorScheme.background)
        ) {
            content()
        }
    }
}

@Composable
fun borderBrushMultiColor(colors: List<Color>): Brush {
    return Brush.linearGradient(
        colors = colors,
    )
}

@Composable
fun Alarm(context: Context, currentHeartRate: MutableState<String>, alarms: MutableState<Alarm>, mVibrator: Vibrator) {

    LaunchedEffect(Unit) {
        while(true) {
            if (alarms.value.isAlarmActivate && alarms.value.vibration.isActivate) {
                Log.d("PADOU", "vibration are called here")
                mVibrator.vibrate(getVibrationEffects()[alarms.value.vibration.actual.toInt()])
            }
            delay(2000)
        }
    }

    LaunchedEffect(Unit) {
        while(true) {
            if (alarms.value.isAlarmActivate && alarms.value.sound.isActivate) {
                soundPlayer(context, alarms.value.sound.actual, fileId = R.raw.alarm_test)
            }
            delay(2000)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MultiColorBorderCircularColumn(
            borderColors = listOf(
                MaterialTheme.colorScheme.secondary,
                Color.Transparent,
                MaterialTheme.colorScheme.secondary,
            )
        ) {}
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
                Box(modifier = Modifier.size(50.dp)) {
                    Button(
                        type = ButtonTypeEnum.REDIRECTION,
                        content = {
                            CircleIcon(
                                icon = Icons.Rounded.Clear,
                                tint = MaterialTheme.colorScheme.surface
                            )
                        },
                        action = {
                            currentHeartRate.value = "100"
                        }
                    )
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
    /*val bpm: MutableState<String> = mutableStateOf("0")
    val navController = rememberSwipeDismissableNavController()
    OwlTheme {
            Home(bpm, LocalContext.current,  navController, true)
    }*/
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
fun PreviewAlarm() {
    /*val bpm: MutableState<String> = mutableStateOf("0")
    OwlTheme {
        Alarm(currentHeartRate = bpm)
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
