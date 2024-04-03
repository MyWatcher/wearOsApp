package com.eipsaferoad.owl.presentation.settings

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.SwitchDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.eipsaferoad.owl.models.Alarm
import com.eipsaferoad.owl.models.AlarmType
import com.eipsaferoad.owl.presentation.theme.OwlTheme

@Composable
fun Settings(context: Context, navController: NavHostController, alarms: MutableState<Alarm>) {
    var isAlarmActivate by remember { mutableStateOf(false) }
    var isSoundActivate by remember { mutableStateOf(false) }
    var isVibrationActivate by remember { mutableStateOf(false) }
    var isVibrationSelected by remember { mutableStateOf(false) }
    var isSoundSelected by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .padding(top = 50.dp, bottom = 50.dp)
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Button(
                modifier = Modifier.width(200.dp).height(40.dp),
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
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
                    Switch(
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF00275B),
                            checkedTrackColor = Color(0xFF00275B),
                            uncheckedThumbColor = Color(0xFF00275B),
                            uncheckedTrackColor = Color(0xFF8D9497)
                        ),
                        checked = isAlarmActivate,
                        onCheckedChange = {
                            alarms.value.isAlarmActivate = it; isAlarmActivate = it
                        }
                    )
                }
            }
        }
        item {
            Button(
                modifier = Modifier
                    .width(200.dp)
                    .height(if (isVibrationSelected) 80.dp else 40.dp),
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
                onClick = {
                    isVibrationSelected = !isVibrationSelected
                    if (isSoundSelected) {
                        isSoundSelected = false
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = if (isVibrationSelected) 0.dp else 10.dp, bottom = 10.dp),
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
                            Switch(
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00275B),
                                    checkedTrackColor = Color(0xFF00275B),
                                    uncheckedThumbColor = Color(0xFF00275B),
                                    uncheckedTrackColor = Color(0xFF8D9497)
                                ),
                                checked = isVibrationActivate,
                                onCheckedChange = {
                                    alarms.value.vibration.isActivate = it; isVibrationActivate = it
                                }
                            )
                        }
                    }
                    if (isVibrationSelected) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "-")
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                LinearProgressIndicator(
                                    progress = alarms.value.vibration.actual / (alarms.value.vibration.max - alarms.value.vibration.min),
                                    modifier = Modifier
                                        .height(5.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Text(text = "+")
                        }
                    }
                }
            }
        }
        item {
            Button(
                modifier = Modifier
                    .width(200.dp)
                    .height(if (isSoundSelected) 80.dp else 40.dp),
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
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
                        .padding(top = if (isSoundSelected) 0.dp else 10.dp, bottom = 10.dp),
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
                            Switch(
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00275B),
                                    checkedTrackColor = Color(0xFF00275B),
                                    uncheckedThumbColor = Color(0xFF00275B),
                                    uncheckedTrackColor = Color(0xFF8D9497)
                                ),
                                checked = isSoundActivate,
                                onCheckedChange = {
                                    alarms.value.sound.isActivate = it; isSoundActivate = it
                                }
                            )
                        }
                    }
                    if (isSoundSelected) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "-")
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                LinearProgressIndicator(
                                    progress = alarms.value.sound.actual / (alarms.value.sound.max - alarms.value.sound.min),
                                    modifier = Modifier
                                        .height(5.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Text(text = "+")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
fun PreviewSettings() {
    val navController = rememberSwipeDismissableNavController()
    var alarms: MutableState<Alarm> = mutableStateOf(Alarm(AlarmType(0, 100), AlarmType(0, 100), false))
    OwlTheme {
        Settings(LocalContext.current, navController = navController, alarms)
    }
}
