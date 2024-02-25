package com.hearos.hearo.api

import retrofit2.Call
import retrofit2.http.POST

interface VoiceApiService {
    @POST("/run/voice")
    fun runVoiceScript(): Call<VoiceResponse>
}

data class VoiceResponse(val message: String, val code: Int)