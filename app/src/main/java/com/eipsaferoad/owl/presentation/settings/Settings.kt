package com.eipsaferoad.owl.presentation.settings

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.SwitchDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import com.eipsaferoad.owl.utils.LocalStorage

@Composable
fun Settings(context: Context, navController: NavHostController) {
    val isAlarmActive = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            onClick = { isAlarmActive.value = !isAlarmActive.value } // Toggle boolean on click
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
                    checked = isAlarmActive.value,
                    onCheckedChange = { isAlarmActive.value = it }
                )
            }
        }
        Button(
            modifier = Modifier
                .width(200.dp)
                .blur(radius = 20.dp),
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            onClick = {}
        ) {
            Text(
                text = "Vibration",
            )
        }
        Button(
            modifier = Modifier
                .width(200.dp)
                .blur(radius = 2.dp),
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            onClick = {}
        ) {
            Text(
                text = "Sound",
            )
        }
    }
}

@Composable
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
fun PreviewSettings() {
    val navController = rememberSwipeDismissableNavController()
    OwlTheme {
        Settings(LocalContext.current, navController = navController)
    }
}
