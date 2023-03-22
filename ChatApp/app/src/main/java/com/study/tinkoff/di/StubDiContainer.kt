package com.study.tinkoff.di

import android.content.Context
import com.study.tinkoff.common.AndroidResourceProvider
import com.study.tinkoff.common.ResourceProvider
import com.study.tinkoff.feature.channels.data.StubChannelRepository
import com.study.tinkoff.feature.channels.domain.StreamRepository
import com.study.tinkoff.feature.chat.data.repository.StubMessageRepository
import com.study.tinkoff.feature.chat.domain.repository.MessageRepository
import com.study.tinkoff.feature.emoji.data.StubEmojiRepository
import com.study.tinkoff.feature.emoji.domain.EmojiRepository
import com.study.tinkoff.feature.profile.data.StubUserAuthRepository
import com.study.tinkoff.feature.profile.domain.UserAuthRepository
import com.study.tinkoff.feature.users.data.StubUsersRepository
import com.study.tinkoff.feature.users.domain.UsersRepository

object StubDiContainer {
    private fun providesApplicationContext() = MainApplication.appContext
    fun bindsMessageRepository(): MessageRepository = StubMessageRepository()
    fun bindsChannelsRepository(): StreamRepository = StubChannelRepository()
    fun bindsUserAuthRepository(): UserAuthRepository = StubUserAuthRepository()
    fun bindsUsersRepository(): UsersRepository = StubUsersRepository()
    fun bindsEmojiRepository(): EmojiRepository = StubEmojiRepository

    fun bindsResourceProvider(applicationContext: Context = providesApplicationContext()): ResourceProvider =
        AndroidResourceProvider(applicationContext)
}
