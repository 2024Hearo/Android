package com.hearos.hearo.api

import com.hearos.hearo.dto.BaseResponse
import com.hearos.hearo.dto.ChatInviteRes
import com.hearos.hearo.dto.ChatListRes
import com.hearos.hearo.dto.ChatRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatInterface {
    @POST("/api/invite")
    suspend fun inviteChat(@Body chatRequest : ChatRequest): BaseResponse<ChatInviteRes>

    @GET("/api/")
    suspend fun getChatList(
        @Header("Authorization") accessToken : String): BaseResponse<List<ChatListRes>>

}