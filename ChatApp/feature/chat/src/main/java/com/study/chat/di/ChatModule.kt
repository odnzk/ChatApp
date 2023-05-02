package com.study.chat.di

import com.study.chat.presentation.chat.elm.ChatActor
import com.study.chat.presentation.chat.elm.ChatEffect
import com.study.chat.presentation.chat.elm.ChatEvent
import com.study.chat.presentation.chat.elm.ChatReducer
import com.study.chat.presentation.chat.elm.ChatState
import com.study.chat.presentation.emoji.elm.EmojiListActor
import com.study.chat.presentation.emoji.elm.EmojiListEffect
import com.study.chat.presentation.emoji.elm.EmojiListEvent
import com.study.chat.presentation.emoji.elm.EmojiListReducer
import com.study.chat.presentation.emoji.elm.EmojiListState
import com.study.common.di.FeatureScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class ChatModule {

    @Provides
    @FeatureScope
    fun providesStore(
        reducer: ChatReducer,
        actor: ChatActor
    ): Store<ChatEvent, ChatEffect, ChatState> {
        return ElmStoreCompat(ChatState(), reducer, actor)
    }

    @Provides
    @FeatureScope
    fun providesEmojiStore(
        reducer: EmojiListReducer,
        actor: EmojiListActor
    ): Store<EmojiListEvent, EmojiListEffect, EmojiListState> {
        return ElmStoreCompat(EmojiListState(), reducer, actor)
    }

    @Provides
    @FeatureScope
    fun providesChatScope(dispatcher: CoroutineDispatcher) =
        CoroutineScope(SupervisorJob() + dispatcher)
}
