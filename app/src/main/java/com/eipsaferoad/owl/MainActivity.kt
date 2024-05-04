package com.eipsaferoad.owl

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.ambient.AmbientLifecycleObserver
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.ambient.AmbientModeSupport.AmbientCallback
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.eipsaferoad.owl.api.Request
import com.eipsaferoad.owl.core.Authentication
import com.eipsaferoad.owl.heartRate.HeartRateService
import com.eipsaferoad.owl.models.Alarm
import com.eipsaferoad.owl.models.SoundAlarm
import com.eipsaferoad.owl.models.VibrationAlarm
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.alarm.Alarm
import com.eipsaferoad.owl.presentation.home.Home
import com.eipsaferoad.owl.presentation.login.Login
import com.eipsaferoad.owl.presentation.settings.Settings
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import com.eipsaferoad.owl.utils.EnvEnum
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
import org.json.JSONObject

class MainActivity : ComponentActivity(),
    AmbientModeSupport.AmbientCallbackProvider,
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    private lateinit var mVibrator: Vibrator
    private lateinit var vibrationEffectSingle: VibrationEffect
    private var bpm: MutableState<String> = mutableStateOf("0")
    private var alarms: MutableState<Alarm> = mutableStateOf(Alarm(VibrationAlarm(), SoundAlarm(1, 0), false, "", 0))
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

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val filter = IntentFilter()
        lifecycle.addObserver(ambientObserver)
        activityContext = this
        checkPermission(android.Manifest.permission.BODY_SENSORS, 100)
        checkPermission(android.Manifest.permission.VIBRATE, 100);
        filter.addAction("updateHR")
        registerReceiver(broadcastReceiver, filter)
        setTheme(android.R.style.Theme_DeviceDefault)
        initVibration()
        url.value = ReadEnvVar.readEnvVar(this, ReadEnvVar.EnvVar.API_URL)
        val alarm = LocalStorage.getData(this, EnvEnum.ALARM.value)
        if (!alarm.isNullOrEmpty()) {
            alarms.value.isAlarmActivate = alarm == "1";
        }
        val vibration = LocalStorage.getData(this, EnvEnum.VIBRATION_ALARM.value)
        if (!vibration.isNullOrEmpty()) {
            alarms.value.vibration.isActivate = vibration[0] == '1';
            alarms.value.vibration.actual = vibration.substring(1).toFloat()
        }
        val sound = LocalStorage.getData(this, EnvEnum.SOUND_ALARM.value)
        if (!sound.isNullOrEmpty()) {
            alarms.value.sound.isActivate = sound[0] == '1';
            alarms.value.sound.actual = sound.substring(1).toFloat()
        }

        setContent {
            WearApp(this, bpm, alarms, url.value, { token -> accessToken.value = token }, mVibrator, vibrationEffectSingle, accessToken)
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

    private fun initVibration() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
                mVibrator = vibratorManager.getVibrator(vibratorManager.vibratorIds[0])
            } else {
                mVibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            }
            vibrationEffectSingle = VibrationEffect.createOneShot(500, VibrationEffect.EFFECT_HEAVY_CLICK)
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
                    .add("Authorization", "Bearer ${accessToken.value}")
                    .build()
                Request.makeRequest("${url.value}/api/heart-rate", headers, formBody, Request.Companion.REQUEST_TYPE.POST) {}
            }
        }
    }
}

@Composable
fun WearApp(context: Context, currentHeartRate: MutableState<String>, alarms: MutableState<Alarm>, apiUrl: String, setAccessToken: (token: String) -> Unit, mVibrator: Vibrator, vibrationEffectSingle: VibrationEffect, accessToken: MutableState<String?>) {
    val navController = rememberSwipeDismissableNavController()
    val email = LocalStorage.getData(context, EnvEnum.EMAIL.value);
    val password = LocalStorage.getData(context, EnvEnum.PASSWORD.value);
    if (email != null && password != null) {
        Authentication.login(context, apiUrl = apiUrl, email = email, password = password, navController =  navController , setAccessToken =  setAccessToken)
        if (!accessToken.value.isNullOrEmpty()) {
            val headers = Headers.Builder()
                .add("Authorization", "Bearer ${accessToken.value}")
                .add("Accept", "application/json")
                .build()
            val formBody = FormBody.Builder()
                .build()
            Request.makeRequest(
                "$apiUrl/api/alarmPreferences",
                headers,
                formBody,
                Request.Companion.REQUEST_TYPE.GET
            ) { dto ->
                run {
                    val jsonObject = JSONObject(dto)
                    val dataArray = jsonObject.getJSONArray("data").getJSONObject(0)
                    alarms.value.isAlarmActivate = dataArray.getString("isAlarmActivate").equals("true")
                    alarms.value.vibration.isActivate = dataArray.getString("isVibrationActivate").equals("true")
                    alarms.value.sound.isActivate = dataArray.getString("isSoundActivate").equals("true")
                    alarms.value.iconId = dataArray.getString("iconId").toInt()
                    alarms.value.music = dataArray.getString("music")
                    alarms.value.vibration.actual = dataArray.getString("vibrationLevel").toFloat()
                    alarms.value.sound.actual = dataArray.getString("soundLevel").toFloat()
                }
            }
        }
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
                    Home(currentHeartRate, context, navController, alarms, mVibrator)
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
                    Settings(context, alarms, mVibrator, apiUrl, accessToken.value )
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
    /*var bpm: MutableState<String> = mutableStateOf("0")
    var alarms: MutableState<Alarm> = mutableStateOf(Alarm(AlarmType(0, 100), AlarmType(0, 100), false))*/
    /*WearApp(LocalContext.current , bpm, alarms, "", {})*/
}
