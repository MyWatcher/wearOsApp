package com.eipsaferoad.owl.api

data class HeartRateDto(val heartRate: String)

data class SuccessResponse(
    val message: String,
    val data: List<Int>
)

data class FailureResponse(
    val success: Boolean,
    val message: String
)
