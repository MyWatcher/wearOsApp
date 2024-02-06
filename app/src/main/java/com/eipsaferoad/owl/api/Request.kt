package com.eipsaferoad.owl.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

class Request {

    companion object {

        private fun post(url: String, body: FormBody): String {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .post(body)
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

        fun makeRequest(url: String, body: FormBody, callback: (dto: JSONObject) -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        post(url, body)
                    }
                    withContext(Dispatchers.Main) {
                        try {
                            callback(JSONObject(response))
                        } catch (e: JSONException) {
                            Log.e("API CALL", "Error parsing JSON: $e")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("API CALL", "Error api call in: $e")
                }
            }
        }
    }
}