package com.hearos.hearo.dto

import com.google.gson.annotations.SerializedName

data class ResponseBody(
    val message: String,
    val code: Int

)

data class SoundRes(
    val url: String
)
