package com.eipsaferoad.owl.presentation.login

import android.content.Context
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
import com.eipsaferoad.owl.api.Request
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.presentation.components.TextInput
import com.eipsaferoad.owl.presentation.theme.OwlTheme
import com.eipsaferoad.owl.utils.LocalStorage
import okhttp3.FormBody
import okhttp3.Headers

fun login(context: Context, apiUrl: String, email: String, password: String, changePage: (page: Int) -> Unit, setAccessToken: (token: String) -> Unit) {
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
            LocalStorage.setData(context, "email", email)
            LocalStorage.setData(context, "password", password)
            changePage(PagesEnum.HOME.value)
        }
    }
}

@Composable
fun Login(context: Context, apiUrl: String, changePage: (page: Int) -> Unit, setAccessToken: (token: String) -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextInput(placeholder = "Email", value = email.value, onChange = { value -> email.value = value })
        TextInput(placeholder = "Password", value = '.'.toString().repeat(password.value.length), onChange = { value -> password.value = value })
        Button(
            modifier = Modifier
                .width(100.dp)
                .padding(top = 10.dp),
            onClick = {
                login(context = context, apiUrl = apiUrl, email = email.value, password = password.value, changePage, setAccessToken)
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
            /*Login(apiUrl = "", changePage = {}, setAccessToken = {})*/
        }
    }
}
