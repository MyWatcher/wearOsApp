package com.eipsaferoad.owl.presentation.login

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
import okhttp3.FormBody

fun login(apiUrl: String, email: String, password: String, changePage: (page: Int) -> Unit) {
    val formBody = FormBody.Builder()
        .add("email", email)
        .add("password", password)
        .build()
    Request.makeRequest(
        "$apiUrl/api/auth/login",
        formBody
    ) {
        dto ->
        run {
            changePage(PagesEnum.HOME.value)
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
