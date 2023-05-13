package com.study.channels.domain.usecase

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.channels.domain.repository.ChannelRepository
import com.study.common.Validator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AddChannelUseCase @Inject constructor(
    private val validator: Validator<Channel>,
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(title: String) =
        withContext(dispatcher) {
            val channel = Channel(NOT_YET_SYNCHRONIZED_ID, title, null)
            validator.validate(channel)
            repository.addChannel(channel)
        }

}
