package com.study.chat.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.study.chat.data.RemoteMessageRepository
import com.study.chat.domain.exceptions.ContentHasNotLoadedException
import com.study.chat.domain.exceptions.SynchronizationException
import com.study.chat.domain.repository.MessageRepository
import com.study.chat.domain.usecase.SendMessageUseCase
import com.study.chat.domain.usecase.UpdateReactionUseCase
import com.study.chat.presentation.chat.model.UiMessage
import com.study.chat.presentation.chat.util.mapper.toUiMessageWithDateGrouping
import com.study.common.extensions.runCatchingNonCancellation
import com.study.network.StubUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

internal class ChatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val repository: MessageRepository = RemoteMessageRepository()
    private val stubUserRepository = StubUserRepository()
    private val sendMessage = SendMessageUseCase(repository, Dispatchers.Default)
    private val updateReaction = UpdateReactionUseCase(repository, Dispatchers.Default)

    private var jobObservingPagingData: Job? = null
    private val selectedChannelTitle: String by lazy(LazyThreadSafetyMode.NONE) {
        savedStateHandle.get<String>("channelTitle") ?: error("Invalid channel title")
    }
    private val selectedTopicTitle: String by lazy(LazyThreadSafetyMode.NONE) {
        savedStateHandle.get<String>("topicTitle") ?: error("Invalid topic title")
    }
    private var currUserId: Int = -1

    private val _state: MutableStateFlow<PagingData<Any>> =
        MutableStateFlow(PagingData.empty())
    val state = _state.asStateFlow()

    init {
        safeLaunch { currUserId = stubUserRepository.getCurrentUserId() }
        loadData()
    }

    fun onEvent(event: ChatFragmentEvent) = safeLaunch {
        when (event) {
            ChatFragmentEvent.Reload -> loadData()
            is ChatFragmentEvent.SendMessage -> {
                val list = checkMessagesLoaded(event.currList)
                val result = sendMessage(
                    selectedChannelTitle,
                    event.messageContent,
                    selectedTopicTitle,
                    list
                )
                _state.value = PagingData.from(result)
            }
            is ChatFragmentEvent.UpdateReaction -> {
                if (event.messageId == UiMessage.NOT_YET_SYNCHRONIZED_ID) {
                    throw SynchronizationException()
                } else {
                    val list = checkMessagesLoaded(event.currList)
                    _state.value = PagingData.from(
                        updateReaction(
                            list,
                            selectedMessageId = event.messageId,
                            selectedEmojiName = event.emojiName,
                            currentUserId = currUserId
                        )
                    )
                }
            }
            is ChatFragmentEvent.Search -> TODO()
        }
    }

    private fun loadData() {
        jobObservingPagingData?.cancel()
        jobObservingPagingData = viewModelScope.launch {
            repository.getMessages(
                channelTitle = selectedChannelTitle, topicName = selectedTopicTitle
            ).collectLatest { pagingData ->
                _state.value = pagingData.toUiMessageWithDateGrouping(currUserId)
            }
        }
    }

    private fun checkMessagesLoaded(currList: List<Any?>): List<Any> {
        if (currList.firstOrNull { it == null } != null) {
            throw ContentHasNotLoadedException()
        }
        return currList.filterNotNull()
    }

    private inline fun ViewModel.safeLaunch(crossinline action: suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.Default) {
            runCatchingNonCancellation { action() }.onFailure { throwable ->
                Timber.d(throwable)
            }
        }
}
