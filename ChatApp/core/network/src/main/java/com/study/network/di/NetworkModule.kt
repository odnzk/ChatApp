package com.study.network.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.study.network.BuildConfig
import com.study.network.ConnectionManager
import com.study.network.ZulipApi
import com.study.network.dataSource.ChannelRemoteDataSource
import com.study.network.dataSource.MessageRemoteDataSource
import com.study.network.dataSource.UserDataSource
import com.study.network.util.AuthInterceptor
import com.study.network.util.CacheInterceptor
import com.study.network.util.EnumConverterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.study.network.util.HttpLoggingInterceptor as DaggerHttpLoggingInterceptor

@Module
internal class NetworkModule {

    @Provides
    @AuthInterceptor
    fun providesAuthInterceptor(userCredentials: UserCredentials): Interceptor =
        Interceptor { chain ->
            val credentials = Credentials.basic(userCredentials.username, userCredentials.password)
            val request = chain.request().newBuilder().addHeader(AUTH_HEADER, credentials).build()
            chain.proceed(request)
        }

    @Provides
    @DaggerHttpLoggingInterceptor
    fun providesHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @CacheInterceptor
    fun providesCacheInterceptor(connectionManager: ConnectionManager): Interceptor =
        Interceptor { chain ->
            var request = chain.request()
            request = if (connectionManager.isConnected()) {
                request.newBuilder().header("Cache-Control", "public, max-age=$CACHE_MAX_AGE")
                    .build()
            } else request
            chain.proceed(request)
        }

    @Provides
    @Singleton
    @OptIn(ExperimentalSerializationApi::class)
    fun providesRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addConverterFactory(EnumConverterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttp(
        context: Context,
        @AuthInterceptor authInterceptor: Interceptor,
        @DaggerHttpLoggingInterceptor httpLoggingInterceptor: Interceptor,
        @CacheInterceptor cacheInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, CACHE_SIZE))
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(cacheInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ZulipApi {
        return retrofit.create(ZulipApi::class.java)
    }

    @Provides
    @Reusable
    fun providesNetworkImpl(
        messageDataSource: MessageRemoteDataSource,
        streamDataSource: ChannelRemoteDataSource,
        userDataSource: UserDataSource
    ): NetworkImpl = object : NetworkImpl {
        override val messageDataSource: MessageRemoteDataSource = messageDataSource
        override val streamDataSource: ChannelRemoteDataSource = streamDataSource
        override val userDataSource: UserDataSource = userDataSource
    }

    companion object {
        private const val AUTH_HEADER = "Authorization"
        private const val CONNECTION_TIMEOUT_SEC = 5L
        private const val CACHE_SIZE = (10 * 1024 * 1024).toLong()
        private const val CACHE_MAX_AGE = 10
    }
}
