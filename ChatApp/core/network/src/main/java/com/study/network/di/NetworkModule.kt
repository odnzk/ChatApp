package com.study.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.study.network.ZulipApi
import com.study.network.repository.MessageDataSource
import com.study.network.repository.StreamDataSource
import com.study.network.repository.UserDataSource
import com.study.network.util.AuthInterceptor
import com.study.network.util.EnumConverterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import com.study.network.util.HttpLoggingInterceptor as DaggerHttpLoggingInterceptor

@Module
internal class NetworkModule {

    @Reusable
    @Provides
    @AuthInterceptor
    fun providesAuthInterceptor(credentials: String): Interceptor = Interceptor { chain ->
        val request: Request =
            chain.request().newBuilder().addHeader(AUTH_HEADER, credentials).build()
        chain.proceed(request)
    }

    @Reusable
    @Provides
    @DaggerHttpLoggingInterceptor
    fun providesHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Reusable
    @OptIn(ExperimentalSerializationApi::class)
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder().baseUrl(ZULIP_BASE_URL).client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addConverterFactory(EnumConverterFactory())
            .build()
    }

    @Provides
    @Reusable
    fun providesOkHttp(
        @AuthInterceptor authInterceptor: Interceptor,
        @DaggerHttpLoggingInterceptor httpLoggingInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor).build()
    }

    @Provides
    @Reusable
    fun providesApi(retrofit: Retrofit): ZulipApi {
        return retrofit.create(ZulipApi::class.java)
    }

    @Provides
    @Reusable
    fun providesNetworkImpl(
        messageDataSource: MessageDataSource,
        streamDataSource: StreamDataSource,
        userDataSource: UserDataSource
    ): NetworkImpl = object : NetworkImpl {
        override val messageDataSource: MessageDataSource = messageDataSource
        override val streamDataSource: StreamDataSource = streamDataSource
        override val userDataSource: UserDataSource = userDataSource
    }

    companion object {
        private const val ZULIP_BASE_URL =
            "https://tinkoff-android-spring-2023.zulipchat.com/api/v1/"
        private const val AUTH_HEADER = "Authorization"
        private const val CONNECTION_TIMEOUT_SEC = 10L
    }
}
