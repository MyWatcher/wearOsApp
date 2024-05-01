package com.eipsaferoad.owl.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

class Request {

    companion object {

        public enum class REQUEST_TYPE() {
            POST,
            PUT,
            GET,
            DELETE
        }

        private fun post(url: String, headers: Headers, body: FormBody, requestType: REQUEST_TYPE): String {
            val client = OkHttpClient()
            var request: Request
            if (requestType == REQUEST_TYPE.POST) {
                request = Request.Builder()
                    .url(url)
                    .post(body)
                    .headers(headers)
                    .build()
            } else if (requestType == REQUEST_TYPE.GET) {
                request = Request.Builder()
                    .url(url)
                    .post(body)
                    .headers(headers)
                    .build()
            } else if (requestType == REQUEST_TYPE.PUT) {
                request = Request.Builder()
                    .url(url)
                    .post(body)
                    .headers(headers)
                    .build()
            } else {
                request = Request.Builder()
                    .url(url)
                    .post(body)
                    .headers(headers)
                    .build()
            }

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

        fun makeRequest(url: String, headers: Headers, body: FormBody, requestType: REQUEST_TYPE, callback: (dto: JSONObject) -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        post(url, headers, body, requestType)
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