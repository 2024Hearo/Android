package com.hearos.hearo.utils

import com.google.gson.GsonBuilder
import com.hearos.hearo.api.ChatInterface
import com.hearos.hearo.api.PlaySoundInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService {

    companion object {

        private const val BASE_URL = "http://34.168.33.254:8080/"

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttpClient에 로깅 인터셉터 추가
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS) // 연결 타임아웃 10초
            .readTimeout(300, TimeUnit.SECONDS)    // 읽기 타임아웃 30초
            .writeTimeout(150, TimeUnit.SECONDS)   // 쓰기 타임아웃 15초
            .addInterceptor(loggingInterceptor)   // 로깅 인터셉터 추가 (이미 설정한 경우)
            .build()


        private val retrofit by lazy {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .client(OkHttpClient.Builder().apply {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        val chatApi: ChatInterface = retrofit.create(ChatInterface::class.java)
        val MypageApi: PlaySoundInterface by lazy {
            retrofit.create(PlaySoundInterface::class.java)
        }
    }
}
