package com.hearos.hearo.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyVoiceInterface {
    @Multipart
    @POST("/voice/addvoice")
    fun uploadVoice(@Part file: MultipartBody.Part): Call<Void>
}