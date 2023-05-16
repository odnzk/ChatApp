package com.study.chat.shared.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.cancel
import kotlin.properties.Delegates.notNull

internal interface ChatDepProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dep: ChatDep

    companion object : ChatDepProvider by ChatDepStore
}

object ChatDepStore : ChatDepProvider {
    override var dep: ChatDep by notNull()
}

internal class ChatComponentViewModel : ViewModel() {
    val chatComponent = DaggerChatComponent.factory().create(ChatDepProvider.dep)
    override fun onCleared() {
        super.onCleared()
        chatComponent.run {
            chatScope.cancel()
            chatStoreHolder.store.stop()
            actionsStoreHolder.store.stop()
            editMessageStoreHolder.store.stop()
            emojiStoreHolder.store.stop()
        }
    }
}
