package com.hearos.hearo.utils

import com.hearos.hearo.api.ChatInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitService {

    companion object {

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(ApiRepository.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val chatApi = retrofit.create<ChatInterface>(ChatInterface::class.java)
    }
}