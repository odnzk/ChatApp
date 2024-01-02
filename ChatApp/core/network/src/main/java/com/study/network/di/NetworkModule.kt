package com.study.network.di

import android.content.Context
import coil.ImageLoader
import coil.request.CachePolicy
import com.study.auth.api.Authentificator
import com.study.network.BuildConfig
import com.study.network.BuildConfig.BASE_URL
import com.study.network.api.AuthApi
import com.study.network.api.ChannelsApi
import com.study.network.api.MessagesApi
import com.study.network.api.UsersApi
import com.study.network.di.annotations.AuthInterceptor
import com.study.network.di.annotations.AuthorizedOkHttp
import com.study.network.di.annotations.AuthorizedRetrofit
import com.study.network.di.annotations.CacheInterceptor
import com.study.network.di.annotations.CoilAuthInterceptor
import com.study.network.util.ConnectionManager
import com.study.network.util.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.study.network.di.annotations.HttpLoggingInterceptor as DaggerHttpLoggingInterceptor

@Module
internal class NetworkModule {

    @Provides
    @AuthInterceptor
    fun providesAuthInterceptor(authentificator: Authentificator): Interceptor =
        Interceptor { chain ->
            val credentials = runBlocking {
                Credentials.basic(authentificator.getUsername(), authentificator.getApiKey())
            }
            val request = chain.request().newBuilder().addHeader(AUTH_HEADER, credentials).build()
            chain.proceed(request)
        }

    @Provides
    @CoilAuthInterceptor
    fun providesImageAuthInterceptor(authentificator: Authentificator): Interceptor =
        Interceptor { chain ->
            val credentials = runBlocking {
                Credentials.basic(authentificator.getUsername(), authentificator.getApiKey())
            }
            val request = chain.request()
                .takeIf { it.url.toString().contains(USER_UPLOADS_PATH) }?.newBuilder()
                ?.addHeader(AUTH_HEADER, credentials)?.build()
                ?: chain.request()
            chain.proceed(request)
        }

    // todo create separate retrofits

    @Provides
    @DaggerHttpLoggingInterceptor
    fun providesHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @CacheInterceptor
    fun providesCacheInterceptor(connectionManager: ConnectionManager): Interceptor =
        Interceptor { chain ->
            val cacheControl = if (connectionManager.isConnected()) {
                CacheControl.Builder().maxAge(CACHE_MAX_AGE, TimeUnit.SECONDS).build()
            } else {
                CacheControl.Builder().maxStale(CACHE_MAX_STALE, TimeUnit.DAYS).build()
            }
            chain.proceed(chain.request().newBuilder().cacheControl(cacheControl).build())
        }

    @Provides
    @Singleton
    @AuthorizedRetrofit
    fun providesAuthorizedRetrofit(
        @AuthorizedOkHttp okHttpClient: OkHttpClient,
        retrofitFactory: RetrofitFactory
    ): Retrofit {
        return retrofitFactory.create(BASE_URL, okHttpClient)
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, retrofitFactory: RetrofitFactory): Retrofit {
        return retrofitFactory.create(BASE_URL, okHttpClient)
    }

    @Provides
    @Singleton
    @AuthorizedOkHttp
    fun providesAuthorizedOkHttp(
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
    fun providesOkHttp(
        @DaggerHttpLoggingInterceptor httpLoggingInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesMessagesApi(@AuthorizedRetrofit retrofit: Retrofit): MessagesApi {
        return retrofit.create(MessagesApi::class.java)
    }

    @Provides
    @Singleton
    fun providesChannelsApi(@AuthorizedRetrofit retrofit: Retrofit): ChannelsApi {
        return retrofit.create(ChannelsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesUsersApi(@AuthorizedRetrofit retrofit: Retrofit): UsersApi {
        return retrofit.create(UsersApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Reusable
    fun providesNetworkImpl(
        messagesApi: MessagesApi,
        channelsApi: ChannelsApi,
        usersApi: UsersApi,
        authApi: AuthApi,
        imageLoader: ImageLoader
    ): NetworkProvider = object : NetworkProvider {
        override val messagesApi: MessagesApi = messagesApi
        override val channelsApi: ChannelsApi = channelsApi
        override val usersApi: UsersApi = usersApi
        override val authApi: AuthApi = authApi
        override val imageLoader: ImageLoader = imageLoader
    }

    @Provides
    @Reusable
    fun providesCoilLoader(
        @CoilAuthInterceptor authInterceptor: Interceptor,
        context: Context
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
    }
}
