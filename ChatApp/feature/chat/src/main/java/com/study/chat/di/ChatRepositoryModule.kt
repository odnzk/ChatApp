package com.study.chat.di

import com.study.chat.data.LocalEmojiRepository
import com.study.chat.data.MessageRepositoryImpl
import com.study.chat.domain.repository.EmojiRepository
import com.study.chat.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface ChatRepositoryModule {

    @Reusable
    @Binds
    fun bindsMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Reusable
    @Binds
    fun bindsEmojiRepository(impl: LocalEmojiRepository): EmojiRepository

}
