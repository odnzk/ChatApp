package com.study.network.di

import coil.ImageLoader
import com.study.network.api.AuthApi
import com.study.network.api.ChannelsApi
import com.study.network.api.MessagesApi
import com.study.network.api.UsersApi

interface NetworkProvider {
    val messagesApi: MessagesApi
    val channelsApi: ChannelsApi
    val usersApi: UsersApi
    val authApi: AuthApi
    val imageLoader: ImageLoader
}
