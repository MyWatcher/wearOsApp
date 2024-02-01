package com.eipsaferoad.owl.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @POST
    fun postHeartRate(
        @Url url: String,
        @Header("accept") acceptHeader: String,
        @Header("Authorization") authorizationHeader: String,
        @Header("Content-Type") contentTypeHeader: String,
        @Body body: HeartRateDto
    ): Call<SuccessResponse>

    @POST
    fun postLogin(
        @Url url: String,
        @Header("accept") acceptHeader: String,
        @Header("Authorization") authorizationHeader: String,
        @Header("Content-Type") contentTypeHeader: String,
        @Body body: LoginDto
    ): Call<SuccessResponse>
}