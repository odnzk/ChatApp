package com.study.chat.di

import com.study.chat.presentation.actions.elm.ActionsActor
import com.study.chat.presentation.actions.elm.ActionsEffect
import com.study.chat.presentation.actions.elm.ActionsEvent
import com.study.chat.presentation.actions.elm.ActionsReducer
import com.study.chat.presentation.actions.elm.ActionsState
import com.study.chat.presentation.chat.elm.ChatActor
import com.study.chat.presentation.chat.elm.ChatEffect
import com.study.chat.presentation.chat.elm.ChatEvent
import com.study.chat.presentation.chat.elm.ChatReducer
import com.study.chat.presentation.chat.elm.ChatState
import com.study.chat.presentation.edit.elm.EditMessageActor
import com.study.chat.presentation.edit.elm.EditMessageEffect
import com.study.chat.presentation.edit.elm.EditMessageEvent
import com.study.chat.presentation.edit.elm.EditMessageReducer
import com.study.chat.presentation.edit.elm.EditMessageState
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
    ): Store<ChatEvent, ChatEffect, ChatState> = ElmStoreCompat(ChatState(), reducer, actor)

    @Provides
    @FeatureScope
    fun providesEmojiStore(
        reducer: EmojiListReducer, actor: EmojiListActor
    ): Store<EmojiListEvent, EmojiListEffect, EmojiListState> =
        ElmStoreCompat(EmojiListState(), reducer, actor)

    @Provides
    @FeatureScope
    fun providesActionsStore(
        reducer: ActionsReducer, actor: ActionsActor
    ): Store<ActionsEvent, ActionsEffect, ActionsState> =
        ElmStoreCompat(ActionsState(), reducer, actor)

    @Provides
    @FeatureScope
    fun providesEditMessageStore(
        reducer: EditMessageReducer, actor: EditMessageActor
    ): Store<EditMessageEvent, EditMessageEffect, EditMessageState> {
        return ElmStoreCompat(EditMessageState(), reducer, actor)
    }

    @Provides
    @FeatureScope
    fun providesChatScope(dispatcher: CoroutineDispatcher) =
        CoroutineScope(SupervisorJob() + dispatcher)
}
