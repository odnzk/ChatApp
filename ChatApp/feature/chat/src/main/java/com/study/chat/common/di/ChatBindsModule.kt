package com.study.chat.common.di

import com.study.chat.actions.data.ActionsRepositoryImpl
import com.study.chat.actions.domain.repository.ActionsRepository
import com.study.chat.chat.data.ChatRepositoryImpl
import com.study.chat.chat.domain.repository.ChatRepository
import com.study.chat.edit.data.EditMessageRepositoryImpl
import com.study.chat.edit.domain.repository.EditMessageRepository
import com.study.chat.emoji.data.LocalEmojiRepository
import com.study.chat.emoji.domain.repository.EmojiRepository
import com.study.chat.common.data.TopicRepositoryImpl
import com.study.chat.common.domain.model.OutcomeMessage
import com.study.chat.common.domain.repository.TopicRepository
import com.study.chat.common.domain.util.MessageValidator
import com.study.common.validation.Validator
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface ChatBindsModule {

    @Reusable
    @Binds
    fun bindsChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Reusable
    @Binds
    fun bindsEmojiRepository(impl: LocalEmojiRepository): EmojiRepository

    @Reusable
    @Binds
    fun bindsChannelRepository(impl: TopicRepositoryImpl): TopicRepository

    @Reusable
    @Binds
    fun bindsActionsRepository(impl: ActionsRepositoryImpl): ActionsRepository

    @Reusable
    @Binds
    fun bindsEditMessageRepository(impl: EditMessageRepositoryImpl): EditMessageRepository

    @Reusable
    @Binds
    fun bindsMessageValidator(impl: MessageValidator): Validator<OutcomeMessage>
}
