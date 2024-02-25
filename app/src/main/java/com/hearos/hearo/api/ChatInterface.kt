package com.hearos.hearo.api

import com.hearos.hearo.dto.*
import retrofit2.Call
import retrofit2.http.*

interface ChatInterface {

    @POST("/invite")
    suspend fun inviteChat(@Body chatRequest : ChatRequest): ChatInviteRes

    @POST("/accept-invite")
    suspend fun acceptInvite(@Body chatRequest : ChatAcceptRequest): InvitedRes

    @POST("/reject-invite")
    suspend fun rejectInvite(@Body chatRequest : ChatRejectRequest): InvitedRes

    @GET("/home")
    suspend fun getRemind(@Query("name") name: String,
                          @Query("userid") userId: String): HomeRes

    @GET("/video/play/{filename}")
    suspend fun getSign(@Path("filename") filename: String): SoundRes

}