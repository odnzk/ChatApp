package com.study.chat.presentation.chat.elm

import com.study.chat.domain.usecase.GetAllMessagesUseCase
import com.study.chat.domain.usecase.GetCurrentUserIdUseCase
import com.study.chat.domain.usecase.SearchMessagesUseCase
import com.study.chat.domain.usecase.SendMessageUseCase
import com.study.chat.domain.usecase.UpdateReactionUseCase
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class ChatStoreFactory(
    channelTitle: String,
    channelTopicTitle: String,
    getAllMessage: GetAllMessagesUseCase,
    sendMessage: SendMessageUseCase,
    updateReaction: UpdateReactionUseCase,
    searchMessages: SearchMessagesUseCase,
    getCurrentUserId: GetCurrentUserIdUseCase
) {
    private val store by lazy {
        ElmStoreCompat(
            ChatState(channelTitle = channelTitle, channelTopicTitle = channelTopicTitle),
            ChatReducer(),
            ChatActor(
                getAllMessageUseCase = getAllMessage,
                sendMessageUseCase = sendMessage,
                updateReactionUseCase = updateReaction,
                searchMessagesUseCase = searchMessages,
                getCurrentUserId = getCurrentUserId
            )
        )
    }

    fun create() = store
}
