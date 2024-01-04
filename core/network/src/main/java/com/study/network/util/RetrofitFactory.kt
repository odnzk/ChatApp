package com.study.network.util

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

internal class RetrofitFactory @Inject constructor() {
    fun create(baseUrl: String, client: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl.plus(API_PATH))
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addConverterFactory(EnumConverterFactory())
            .build()
    }

    companion object {
        private const val API_PATH = "/api/v1/"
    }
}