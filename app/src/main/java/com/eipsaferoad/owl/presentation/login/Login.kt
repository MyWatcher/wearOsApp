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

@Composable
fun Login(changePage: (page: Int) -> Unit) {
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
                Log.d("Login", "email: ${email.value} password: ${password.value}")
                changePage(PagesEnum.HOME.value)
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
            Login(changePage = {})
        }
    }
}
