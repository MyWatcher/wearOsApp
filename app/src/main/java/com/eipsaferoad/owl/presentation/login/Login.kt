package com.eipsaferoad.owl.presentation.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.components.TextInput
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import okhttp3.FormBody

fun makeApiCall(url: String, email: String, password: String): String {
    val client = OkHttpClient()
    val formBody = FormBody.Builder()
        .add("email", email)
        .add("password", password)
        .build()
    val request = Request.Builder()
        .url(url)
        .post(formBody)
        .build()

    try {
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Error: Request failed with code ${response.code}")
        }
        return response.body!!.string()
    } catch (e: Exception) {
        throw e
    }
}
fun login(apiUrl: String, email: String, password: String, changePage: (page: Int) -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            val response = withContext(Dispatchers.IO) {
                makeApiCall("$apiUrl/api/auth/login", email, password)
            }
            withContext(Dispatchers.Main) {
                try {
                    val jsonObject = JSONObject(response)
                    val data = jsonObject.getJSONObject("data")
                    val token = data.getString("token")
                    val message = jsonObject.getString("message")
                    changePage(PagesEnum.HOME.value)
                } catch (e: JSONException) {
                    Log.e("Login", "Error parsing JSON: $e")
                }
            }
        } catch (e: Exception) {
            Log.e("Login", "Error logging in: $e")
        }
    }
}

@Composable
fun Login(apiUrl: String, changePage: (page: Int) -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextInput(placeholder = "Email", value = email.value, onChange = { value -> email.value = value })
        TextInput(placeholder = "Password", value = password.value, onChange = { value -> password.value = value })
        Button(
            modifier = Modifier
                .width(100.dp)
                .padding(top = 10.dp),
            onClick = {
                login(apiUrl = apiUrl, email = email.value, password = password.value, changePage)
            }
        ) {
            Text(text = "login")
        }
    }
}

@Composable
@Preview
fun PreviewLogin() {
    OwlTheme {
        Box(
        ) {
            Login(apiUrl = "", changePage = {})
        }
    }
}
