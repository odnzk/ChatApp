package com.study.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.study.network.util.EnumConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val ZULIP_BASE_URL =
        "https://tinkoff-android-spring-2023.zulipchat.com/api/v1/"
    private const val USERNAME = BuildConfig.USERNAME
    private const val API_KEY = BuildConfig.PASSWORD
    private const val AUTH_HEADER = "Authorization"
    private const val CONNECTION_TIMEOUT_SEC = 20L

    @OptIn(ExperimentalSerializationApi::class)
    private fun providesRetrofit(okHttpClient: OkHttpClient = providesOkHttp()): Retrofit {
        return Retrofit.Builder().baseUrl(ZULIP_BASE_URL).client(okHttpClient).addConverterFactory(
            Json { ignoreUnknownKeys = true }
                .asConverterFactory("application/json".toMediaType())
        )
            .addConverterFactory(EnumConverterFactory())
            .build()
    }

    private fun providesOkHttp(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor).addInterceptor(authInterceptor).build()
    }

    fun providesApi(retrofit: Retrofit = providesRetrofit()): ZulipApi {
        return retrofit.create(ZulipApi::class.java)
    }

    private val authInterceptor = Interceptor { chain ->
        val credentials = Credentials.basic(USERNAME, API_KEY)
        val request: Request =
            chain.request().newBuilder().addHeader(AUTH_HEADER, credentials).build()
        chain.proceed(request)
    }
}
