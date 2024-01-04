package com.study.chat.common.di.module

import com.study.chat.actions.presentation.elm.ActionsActor
import com.study.chat.actions.presentation.elm.ActionsEffect
import com.study.chat.actions.presentation.elm.ActionsEvent
import com.study.chat.actions.presentation.elm.ActionsReducer
import com.study.chat.actions.presentation.elm.ActionsState
import com.study.chat.common.data.source.local.message.dao.MessageDao
import com.study.chat.common.data.source.local.message.dao.ReactionDao
import com.study.chat.common.di.ChatDatabase
import com.study.chat.edit.presentation.elm.EditMessageActor
import com.study.chat.edit.presentation.elm.EditMessageEffect
import com.study.chat.edit.presentation.elm.EditMessageEvent
import com.study.chat.edit.presentation.elm.EditMessageReducer
import com.study.chat.edit.presentation.elm.EditMessageState
import com.study.chat.emoji.presentation.elm.EmojiListActor
import com.study.chat.emoji.presentation.elm.EmojiListEffect
import com.study.chat.emoji.presentation.elm.EmojiListEvent
import com.study.chat.emoji.presentation.elm.EmojiListReducer
import com.study.chat.emoji.presentation.elm.EmojiListState
import com.study.common.di.FeatureScope
import com.study.components.di.ManualStoreHolder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class ChatModule {

    @Provides
    @FeatureScope
    fun providesEmojiStore(
        reducer: EmojiListReducer, actor: EmojiListActor
    ): StoreHolder<EmojiListEvent, EmojiListEffect, EmojiListState> =
        ManualStoreHolder { ElmStoreCompat(EmojiListState(), reducer, actor) }

    @Provides
    @FeatureScope
    fun providesActionsStore(
        reducer: ActionsReducer, actor: ActionsActor
    ): StoreHolder<ActionsEvent, ActionsEffect, ActionsState> =
        ManualStoreHolder { ElmStoreCompat(ActionsState(), reducer, actor) }

    @Provides
    @FeatureScope
    fun providesEditMessageStore(
        reducer: EditMessageReducer, actor: EditMessageActor
    ): StoreHolder<EditMessageEvent, EditMessageEffect, EditMessageState> =
        ManualStoreHolder { ElmStoreCompat(EditMessageState(), reducer, actor) }

    @Provides
    @FeatureScope
    fun providesChatScope(dispatcher: CoroutineDispatcher) =
        CoroutineScope(SupervisorJob() + dispatcher)

    @Provides
    @FeatureScope
    fun providesMessagesDao(chatDatabase: ChatDatabase): MessageDao = chatDatabase.messagesDao()

    @Provides
    @FeatureScope
    fun providesReactionsDao(chatDatabase: ChatDatabase): ReactionDao = chatDatabase.reactionsDao()
}
