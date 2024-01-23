package com.hearos.hearo.api

import com.hearos.hearo.dto.BaseResponse
import com.hearos.hearo.dto.LoginRequest
import com.hearos.hearo.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginInterface {
    @POST("users/login")
    suspend fun postLoginUser(@Body postUserReq:LoginRequest): BaseResponse<LoginResponse>

    /*companion object {
        fun create(): UserApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://your-api-url.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(UserApiService::class.java)
        }
    }*/
}