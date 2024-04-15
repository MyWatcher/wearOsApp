package com.eipsaferoad.owl.core

import android.content.Context
import androidx.navigation.NavHostController
import com.eipsaferoad.owl.api.Request
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.utils.EnvEnum
import com.eipsaferoad.owl.utils.LocalStorage
import okhttp3.FormBody
import okhttp3.Headers

class Authentication {
    companion object {
        fun login(
            context: Context,
            isNew: Boolean = false,
            apiUrl: String,
            email: String,
            password: String,
            navController: NavHostController,
            setAccessToken: (token: String) -> Unit
        ) {
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
            ) { dto ->
                run {
                    val data = dto.getJSONObject("data")

                    setAccessToken(data.getString("token"))
                    if (isNew) {
                        LocalStorage.setData(context, EnvEnum.EMAIL.value, email)
                        LocalStorage.setData(context, EnvEnum.PASSWORD.value, password)
                    }
                    navController.navigate(PagesEnum.HOME.value)
                }
            }
        }
    }
}