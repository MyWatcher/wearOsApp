package com.eipsaferoad.owl

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.wear.ambient.AmbientLifecycleObserver
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.ambient.AmbientModeSupport.AmbientCallback
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.eipsaferoad.owl.api.Request
import com.eipsaferoad.owl.heartRate.HeartRateService
import com.eipsaferoad.owl.models.Alarm
import com.eipsaferoad.owl.models.AlarmType
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.alarm.Alarm
import com.eipsaferoad.owl.presentation.home.Home
import com.eipsaferoad.owl.presentation.login.Login
import com.eipsaferoad.owl.presentation.settings.Settings
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import com.eipsaferoad.owl.utils.LocalStorage
import com.eipsaferoad.owl.utils.ReadEnvVar
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import okhttp3.FormBody
import okhttp3.Headers

class MainActivity : ComponentActivity(),
    AmbientModeSupport.AmbientCallbackProvider,
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    private var bpm: MutableState<String> = mutableStateOf("0")
    private var alarms: MutableState<Alarm> = mutableStateOf(Alarm(AlarmType(0, 100), AlarmType(0, 100), false))
    private var accessToken: MutableState<String?> = mutableStateOf(null)
    private var url: MutableState<String> = mutableStateOf("")
    private var activityContext: Context? = null
    private val ambientCallback = object : AmbientLifecycleObserver.AmbientLifecycleCallback {
        override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
        }

        override fun onExitAmbient() {
        }

        override fun onUpdateAmbient() {
        }
    }
    private val ambientObserver = AmbientLifecycleObserver(this, ambientCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(ambientObserver)
        activityContext = this
        checkPermission(android.Manifest.permission.BODY_SENSORS, 100);
        val filter = IntentFilter()
        filter.addAction("updateHR")
        registerReceiver(broadcastReceiver, filter)
        setTheme(android.R.style.Theme_DeviceDefault)
        url.value = ReadEnvVar.readEnvVar(this, ReadEnvVar.EnvVar.API_URL)
        setContent {
            WearApp(this, bpm, alarms, url.value) { token -> accessToken.value = token }
        }
    }

    override fun onPause() {
        super.onPause()
        Intent(this, HeartRateService::class.java).also { intent ->
            startService(intent);
        }
        Toast.makeText(this, "Streaming will continue in the background", Toast.LENGTH_LONG).show();
        try {
            Wearable.getDataClient(activityContext!!).removeListener(this)
            Wearable.getMessageClient(activityContext!!).removeListener(this)
            Wearable.getCapabilityClient(activityContext!!).removeListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            Wearable.getDataClient(activityContext!!).addListener(this)
            Wearable.getMessageClient(activityContext!!).addListener(this)
            Wearable.getCapabilityClient(activityContext!!)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getAmbientCallback(): AmbientCallback = MyAmbientCallback()

    private inner class MyAmbientCallback : AmbientCallback() {
        override fun onEnterAmbient(ambientDetails: Bundle) {
            super.onEnterAmbient(ambientDetails)
        }

        override fun onUpdateAmbient() {
            super.onUpdateAmbient()
        }

        override fun onExitAmbient() {
            super.onExitAmbient()
        }
    }

    override fun onStart() {
        super.onStart();

        Intent(this, HeartRateService::class.java).also { intent ->
            startService(intent);
        }

    }

    override fun onDataChanged(p0: DataEventBuffer) {
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
    }

    override fun onMessageReceived(p0: MessageEvent) {
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        }
    }

    private var broadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            val newBpm: Any = intent?.extras?.get("bpm") ?: return;
            bpm.value = newBpm.toString()
            if (!accessToken.value.isNullOrEmpty()) {
                val formBody = FormBody.Builder()
                    .add("heartRate", bpm.value)
                    .build()
                val headers = Headers.Builder()
                    .add("Authorization", "Bearer ${accessToken.value}")  // Replace "coucou" with your actual token
                    .build()
                Request.makeRequest("${url.value}/api/heart-rate", headers, formBody) {}
            }
        }
    }
}

fun login(apiUrl: String, email: String, password: String, navController: NavHostController, setAccessToken: (token: String) -> Unit) {
    val headers = Headers.Builder()
        .build()
    val formBody = FormBody.Builder()
        .add("email", email)
        .add("password", password)
        .build()
    Request.makeRequest(
        "$apiUrl/api/auth/login",
        headers,
        formBody
    ) {
            dto ->
        run {
            val data = dto.getJSONObject("data")

            setAccessToken(data.getString("token"))
            navController.navigate(PagesEnum.HOME.value)
        }
    }
}

@Composable
fun WearApp(context: Context, currentHeartRate: MutableState<String>, alarms: MutableState<Alarm>, apiUrl: String, setAccessToken: (token: String) -> Unit) {
    val navController = rememberSwipeDismissableNavController()
    val email = LocalStorage.getData(context, "email");
    val password = LocalStorage.getData(context, "password");
    if (email != null && password != null) {
        login(apiUrl = apiUrl, email = email, password = password, navController , setAccessToken)
    }

    OwlTheme {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = PagesEnum.LOGIN.value
        ) {
            composable(PagesEnum.HOME.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    TimeText()
                    Home(currentHeartRate, context, navController, alarms.value.isAlarmActivate)
                }
            }
            composable(PagesEnum.LOGIN.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    TimeText()
                    Login(context, apiUrl, navController, setAccessToken)
                }
            }
            composable(PagesEnum.SETTINGS.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    TimeText()
                    Settings(context, navController, alarms)
                }
            }
            composable(PagesEnum.ALARM.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    TimeText()
                    Alarm(currentHeartRate.value, context, navController)
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    var bpm: MutableState<String> = mutableStateOf("0")
    var alarms: MutableState<Alarm> = mutableStateOf(Alarm(AlarmType(0, 100), AlarmType(0, 100), false))
    WearApp(LocalContext.current , bpm, alarms, "", {})
}
