package com.hearos.hearo.api

import com.hearos.hearo.dto.SoundRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaySoundInterface {

    @GET("sound/play/{filename}")
    suspend fun getAudioFileUrl(@Path("filename") filename: String): SoundRes
}