package com.hearos.hearo.api

import com.hearos.hearo.dto.BaseResponse
import com.hearos.hearo.dto.ChatRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatInterface {
    @POST("/api/invite")
    suspend fun inviteChat(@Body ChatRequest : ChatRequest): BaseResponse<String>
}