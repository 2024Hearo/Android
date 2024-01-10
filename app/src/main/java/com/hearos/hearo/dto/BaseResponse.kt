package com.hearos.hearo.dto

data class BaseResponse<T> (
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: T? = null,
    )
