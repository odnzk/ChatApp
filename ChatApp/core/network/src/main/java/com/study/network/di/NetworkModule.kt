package com.study.network.di

import android.content.Context
import coil.ImageLoader
import coil.request.CachePolicy
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.study.network.BuildConfig
import com.study.network.BuildConfig.BASE_URL
import com.study.network.ZulipApi
import com.study.network.di.util.AuthInterceptor
import com.study.network.di.util.CacheInterceptor
import com.study.network.di.util.CoilAuthInterceptor
import com.study.network.di.util.NetworkCredentials
import com.study.network.util.ConnectionManager
import com.study.network.util.EnumConverterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.study.network.di.util.HttpLoggingInterceptor as DaggerHttpLoggingInterceptor

@Module
internal class NetworkModule {
    @Provides
    @Singleton
    @NetworkCredentials
    fun providesCredentials(): String =
        Credentials.basic(BuildConfig.USERNAME, BuildConfig.PASSWORD)

    @Provides
    @AuthInterceptor
    fun providesAuthInterceptor(@NetworkCredentials credentials: String): Interceptor =
        Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader(AUTH_HEADER, credentials).build()
            chain.proceed(request)
        }

    @Provides
    @CoilAuthInterceptor
    fun providesImageAuthInterceptor(@NetworkCredentials credentials: String): Interceptor =
        Interceptor { chain ->
            val request = if (chain.request().url.toString().contains(USER_UPLOADS_PATH)) {
                chain.request().newBuilder().addHeader(AUTH_HEADER, credentials).build()
            } else chain.request()
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
            val cacheControl = if (connectionManager.isConnected()) {
                CacheControl.Builder().maxAge(CACHE_MAX_AGE, TimeUnit.SECONDS).build()
            } else {
                CacheControl.Builder().maxStale(CACHE_MAX_STALE, TimeUnit.DAYS).build()
            }
            request = request.newBuilder().cacheControl(cacheControl).build()
            chain.proceed(request)
        }

    @Provides
    @Singleton
    @OptIn(ExperimentalSerializationApi::class)
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        return Retrofit.Builder().baseUrl(BASE_URL.plus(API_PATH)).client(okHttpClient)
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
        api: ZulipApi,
        imageLoader: ImageLoader
    ): NetworkImpl = object : NetworkImpl {
        override val zulipApi: ZulipApi = api
        override val imageLoader: ImageLoader = imageLoader
    }

    @Provides
    @Reusable
    fun providesCoilLoader(
        @CoilAuthInterceptor authInterceptor: Interceptor, context: Context
    ): ImageLoader = ImageLoader.Builder(context)
        .okHttpClient { OkHttpClient.Builder().addInterceptor(authInterceptor).build() }
        .memoryCachePolicy(CachePolicy.ENABLED)
        .crossfade(true)
        .build()


    companion object {
        private const val AUTH_HEADER = "Authorization"
        private const val USER_UPLOADS_PATH = "/user_uploads/"
        private const val CONNECTION_TIMEOUT_SEC = 5L
        private const val CACHE_SIZE = (10 * 1024 * 1024).toLong()
        private const val CACHE_MAX_AGE = 10
        private const val CACHE_MAX_STALE = 5
        private const val API_PATH = "/api/v1/"
    }
}
