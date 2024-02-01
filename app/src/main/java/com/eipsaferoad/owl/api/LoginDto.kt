package com.eipsaferoad.owl.api

data class LoginDto(val email: String, val password: String)

data class LoginSuccessResponse(
    val message: String,
    val data: List<Int>
)

data class LoginFailureResponse(
    val success: Boolean,
    val message: String
)
