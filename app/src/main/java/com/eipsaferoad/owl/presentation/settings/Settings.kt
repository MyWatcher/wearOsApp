package com.eipsaferoad.owl.presentation.settings

import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.eipsaferoad.owl.R
import com.eipsaferoad.owl.api.Request
import com.eipsaferoad.owl.components.ToggleSwitch
import com.eipsaferoad.owl.components.handleDraggableModifier
import com.eipsaferoad.owl.models.Alarm
import com.eipsaferoad.owl.presentation.theme.md_theme_light_onSurfaceVariant
import com.eipsaferoad.owl.utils.EnvEnum
import com.eipsaferoad.owl.utils.LocalStorage
import com.eipsaferoad.owl.utils.getVibrationEffects
import com.eipsaferoad.owl.utils.soundPlayer
import okhttp3.Headers
import org.json.JSONObject
import kotlin.math.abs

@Composable
fun Settings(context: Context, alarms: MutableState<Alarm>, mVibrator: Vibrator, apiUrl: String, accessToken: String?) {

    LazyColumn(
        modifier = Modifier
            .padding(top = 40.dp, bottom = 40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { AlarmButton(context, alarms, apiUrl, accessToken) }
        item { VibrationButton(context, alarms, mVibrator, apiUrl, accessToken) }
        item { SoundButton(alarms, context, apiUrl, accessToken) }
    }
}

fun saveOnServer(apiUrl: String, accessToken: String?, alarms: Alarm) {
    val headers = Headers.Builder()
        .add("Authorization", "Bearer $accessToken")
        .add("Accept", "application/json")
        .build()

    val jsonBody = JSONObject().apply {
        put("isAlarmActivate", alarms.isAlarmActivate)
        put("isVibrationActivate", alarms.vibration.isActivate)
        put("vibrationLevel", alarms.vibration.actual.toInt())
        put("isSoundActivate", alarms.sound.isActivate)
        put("soundLevel", alarms.sound.actual.toInt())
        put("music", alarms.music)
        put("iconId", alarms.iconId)
    }.toString()

    Request.makeRequest(
        "$apiUrl/api/alarmPreferences",
        Request.Companion.REQUEST_TYPE.PUT,
        { println("data save on server") },
        headers,
        jsonBody,
    )
}

@Composable
fun AlarmButton(context: Context, alarms: MutableState<Alarm>, apiUrl: String, accessToken: String?) {
    var isAlarmActivate by remember { mutableStateOf(alarms.value.isAlarmActivate) }

    DisposableEffect(isAlarmActivate) {
        onDispose {
            LocalStorage.setData(context, EnvEnum.ALARM.value, if (isAlarmActivate) "1" else "0")
        }
    }

    Button(
        modifier = Modifier
            .width(200.dp)
            .height(40.dp),
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        onClick = { alarms.value.isAlarmActivate = !alarms.value.isAlarmActivate }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Activate Alarm",
                modifier = Modifier.weight(1f)
            )
            ToggleSwitch(
                isActivate = isAlarmActivate,
                action = {
                    alarms.value.isAlarmActivate = it; isAlarmActivate = it
                    saveOnServer(apiUrl, accessToken, alarms.value)
                }
            )
        }
    }
}

@Composable
fun VibrationButton(context: Context, alarms: MutableState<Alarm>, mVibrator: Vibrator, apiUrl: String, accessToken: String?) {
    var isVibrationSelected by remember { mutableStateOf(false) }
    var isVibrationActivate by remember { mutableStateOf(alarms.value.vibration.isActivate) }
    var vibrationVal by remember { mutableStateOf(alarms.value.vibration.actual) }
    var lastPosX by remember { mutableStateOf(0.0f) }
    val nbrPixelToMove by remember { mutableStateOf(70) }
    var heightBar by remember { mutableStateOf(10.dp) }
    val animatedHeightBar by animateDpAsState(targetValue = heightBar)

    DisposableEffect(isVibrationActivate, vibrationVal) {
        onDispose {
            var data = if (isVibrationActivate) "1" else "0"
            data += vibrationVal.toString()
            LocalStorage.setData(context, EnvEnum.VIBRATION_ALARM.value, data)
        }
    }

    Button(
        modifier = Modifier
            .width(200.dp)
            .height(if (isVibrationSelected) 80.dp else 40.dp),
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(backgroundColor = md_theme_light_onSurfaceVariant),
        onClick = {
            isVibrationSelected = !isVibrationSelected
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Vibration",
                )
                if (isVibrationSelected) {
                    ToggleSwitch(
                        isActivate = isVibrationActivate,
                        action = {
                            alarms.value.vibration.isActivate = it; isVibrationActivate = it
                            saveOnServer(apiUrl, accessToken, alarms.value)
                        }
                    )
                } else {
                    Icon(painter = painterResource(id = R.drawable.vibration), contentDescription = "")
                }
            }
            if (isVibrationSelected) {
                Row(
                    modifier = Modifier
                        .width(180.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "-",
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                            alarms.value.vibration.updateAlarm(false)
                            saveOnServer(apiUrl, accessToken, alarms.value)
                            if (vibrationVal > alarms.value.vibration.min.toFloat()) {
                                vibrationVal -= 1f
                                mVibrator.vibrate(getVibrationEffects()[alarms.value.vibration.actual.toInt()])
                            }
                        }
                    )
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(animatedHeightBar)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        LinearProgressIndicator(
                            progress = vibrationVal / (alarms.value.vibration.max - alarms.value.vibration.min),
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {heightBar = 10.dp}
                                    ) { change, dragAmount ->
                                        change.consume()
                                        heightBar = 20.dp
                                        if (lastPosX == 0.0f) {
                                            lastPosX = change.position.x
                                        }
                                        if (change.previousPosition.x < change.position.x && abs(
                                                lastPosX - change.position.x
                                            ) > nbrPixelToMove && vibrationVal < alarms.value.vibration.max.toFloat()
                                        ) {
                                            println("up")
                                            vibrationVal += 1f
                                            lastPosX = change.position.x
                                            mVibrator.vibrate(getVibrationEffects()[alarms.value.vibration.actual.toInt()])
                                            saveOnServer(apiUrl, accessToken, alarms.value)
                                        } else if (change.previousPosition.x > change.position.x && abs(lastPosX - change.position.x) > nbrPixelToMove && vibrationVal > alarms.value.vibration.min.toFloat()
                                        ) {
                                            println("down")
                                            vibrationVal -= 1f
                                            lastPosX = change.position.x
                                            mVibrator.vibrate(getVibrationEffects()[alarms.value.vibration.actual.toInt()])
                                            saveOnServer(apiUrl, accessToken, alarms.value)
                                        }
                                        println(vibrationVal)
                                    }
                                }
                                .height(animatedHeightBar),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = "+",
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .clickable {
                                alarms.value.vibration.updateAlarm()
                                saveOnServer(apiUrl, accessToken, alarms.value)
                                if (vibrationVal < alarms.value.vibration.max.toFloat()) {
                                    vibrationVal += 1f
                                    mVibrator.vibrate(getVibrationEffects()[alarms.value.vibration.actual.toInt()])
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun SoundButton(alarms: MutableState<Alarm>, context: Context, apiUrl: String, accessToken: String?) {
    var isSoundActivate by remember { mutableStateOf(alarms.value.sound.isActivate) }
    var soundVal by remember { mutableStateOf(alarms.value.sound.actual) }
    var isVibrationSelected by remember { mutableStateOf(false) }
    var isSoundSelected by remember { mutableStateOf(false) }
    var lastPosX by remember { mutableStateOf(0.0f) }
    val nbrPixelToMove by remember { mutableStateOf(40) }
    var heightBar by remember { mutableStateOf(10.dp) }
    val animatedHeightBar by animateDpAsState(targetValue = heightBar)

    DisposableEffect(isSoundActivate, soundVal) {
        onDispose {
            var data = if (isSoundActivate) "1" else "0"
            data += soundVal.toString()
            LocalStorage.setData(context, EnvEnum.SOUND_ALARM.value, data)
        }
    }

    Button(
        modifier = Modifier
            .width(200.dp)
            .height(if (isSoundSelected) 80.dp else 40.dp),
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(backgroundColor = md_theme_light_onSurfaceVariant),
        onClick = {
            isSoundSelected = !isSoundSelected
            if (isVibrationSelected) {
                isVibrationSelected = false
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sound",
                )
                if (isSoundSelected) {
                    ToggleSwitch(
                        isActivate = isSoundActivate,
                        action = {
                            alarms.value.sound.isActivate = it; isSoundActivate = it
                            saveOnServer(apiUrl, accessToken, alarms.value)
                        }
                    )
                } else {
                    Icon(painter = painterResource(id = R.drawable.sound), contentDescription = "")
                }
            }
            if (isSoundSelected) {
                Row(
                    modifier = Modifier
                        .width(180.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "-",
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                            alarms.value.sound.updateAlarm(false)
                            saveOnServer(apiUrl, accessToken, alarms.value)
                            if (soundVal > alarms.value.sound.min) {
                                soundVal -= 0.2f
                            }
                            soundPlayer(context, soundVal, fileId = R.raw.alarm_test)
                        }
                    )
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(animatedHeightBar)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        LinearProgressIndicator(
                            progress = soundVal / (alarms.value.sound.max - alarms.value.sound.min),
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {heightBar = 10.dp}
                                    ) { change, dragAmount ->
                                        change.consume()
                                        heightBar = 20.dp
                                        if (lastPosX == 0.0f) {
                                            lastPosX = change.position.x
                                        }
                                        if (change.previousPosition.x < change.position.x && abs(lastPosX - change.position.x) > nbrPixelToMove && soundVal < alarms.value.sound.max.toFloat()
                                        ) {
                                            soundVal += 0.2f
                                            lastPosX = change.position.x
                                            soundPlayer(
                                                context,
                                                soundVal,
                                                fileId = R.raw.alarm_test
                                            )
                                            saveOnServer(apiUrl, accessToken, alarms.value)
                                        } else if (change.previousPosition.x > change.position.x && abs(
                                                lastPosX - change.position.x
                                            ) > nbrPixelToMove && soundVal > alarms.value.sound.min.toFloat()
                                        ) {
                                            soundVal -= 0.2f
                                            lastPosX = change.position.x
                                            soundPlayer(
                                                context,
                                                soundVal,
                                                fileId = R.raw.alarm_test
                                            )
                                            saveOnServer(apiUrl, accessToken, alarms.value)
                                        }
                                    }
                                }
                                .height(animatedHeightBar),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = "+",
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                alarms.value.sound.updateAlarm()
                                saveOnServer(apiUrl, accessToken, alarms.value)
                                if (soundVal < alarms.value.sound.max.toFloat()) {
                                    soundVal += 0.2f
                                }
                                soundPlayer(context, soundVal, fileId = R.raw.alarm_test)
                            }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
fun PreviewSettings() {
    /*var alarms: MutableState<Alarm> = mutableStateOf(Alarm(AlarmType(0, 100), AlarmType(0, 100), false))
    val vibratorManager = getSystemService(ComponentActivity.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    var mVibrator: Vibrator = vibratorManager.getVibrator(vibratorManager.vibratorIds[0])

    OwlTheme {
        Settings(LocalContext.current, alarms = alarms, mVibrator = mVibrator)
    }*/
}
