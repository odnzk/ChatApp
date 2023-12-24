package com.study.chat.common.di

import com.study.chat.actions.presentation.ActionsFragment
import com.study.chat.actions.presentation.elm.ActionsEffect
import com.study.chat.actions.presentation.elm.ActionsEvent
import com.study.chat.actions.presentation.elm.ActionsState
import com.study.chat.chat.presentation.ChatFragment
import com.study.chat.chat.presentation.elm.ChatEffect
import com.study.chat.chat.presentation.elm.ChatEvent
import com.study.chat.chat.presentation.elm.ChatState
import com.study.chat.edit.presentation.EditMessageFragment
import com.study.chat.edit.presentation.elm.EditMessageEffect
import com.study.chat.edit.presentation.elm.EditMessageEvent
import com.study.chat.edit.presentation.elm.EditMessageState
import com.study.chat.emoji.presentation.EmojiListFragment
import com.study.chat.emoji.presentation.elm.EmojiListEffect
import com.study.chat.emoji.presentation.elm.EmojiListEvent
import com.study.chat.emoji.presentation.elm.EmojiListState
import com.study.common.di.FeatureScope
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import vivid.money.elmslie.android.storeholder.StoreHolder

@FeatureScope
@Component(
    dependencies = [ChatDep::class],
    modules = [ChatBindsModule::class, ChatModule::class]
)
internal interface ChatComponent {
    fun inject(fragment: ChatFragment)
    fun inject(fragment: EmojiListFragment)
    fun inject(fragment: ActionsFragment)
    fun inject(fragment: EditMessageFragment)

    val chatScope: CoroutineScope
    val chatStoreHolder: StoreHolder<ChatEvent, ChatEffect, ChatState>
    val emojiStoreHolder: StoreHolder<EmojiListEvent, EmojiListEffect, EmojiListState>
    val actionsStoreHolder: StoreHolder<ActionsEvent, ActionsEffect, ActionsState>
    val editMessageStoreHolder: StoreHolder<EditMessageEvent, EditMessageEffect, EditMessageState>

    @Component.Factory
    interface Factory {
        fun create(dependencies: ChatDep): ChatComponent
    }
}

