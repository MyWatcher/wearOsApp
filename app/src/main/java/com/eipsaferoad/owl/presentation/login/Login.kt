package com.eipsaferoad.owl.presentation.login

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.eipsaferoad.owl.components.Button
import com.eipsaferoad.owl.components.ButtonTypeEnum
import com.eipsaferoad.owl.core.Authentication
import com.eipsaferoad.owl.components.TextInput
import com.eipsaferoad.owl.presentation.theme.OwlTheme

@Composable
fun Login(context: Context, apiUrl: String, navController: NavHostController, setAccessToken: (token: String) -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextInput(placeholder = "Email", value = email.value, onChange = { value -> email.value = value })
        TextInput(placeholder = "Password", value = '.'.toString().repeat(password.value.length), onChange = { value -> password.value = value })
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
                .padding(top = 10.dp),
        ) {
            Button(
                type = ButtonTypeEnum.PRIMARY,
                content = { Text(text = "login") },
                action = {
                    Authentication.login(
                        context = context,
                        isNew = true,
                        apiUrl = apiUrl,
                        email = email.value,
                        password = password.value,
                        navController,
                        setAccessToken
                    )
                }
            )
        }
    }
}

@Composable
@Preview
fun PreviewLogin() {
    val navController = rememberSwipeDismissableNavController()
    OwlTheme {
        Box(
        ) {
            Login(context = LocalContext.current, apiUrl = "", navController = navController, setAccessToken = {})
        }
    }
}
