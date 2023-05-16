package com.study.channels.addChannel.domain.useCase

import com.study.channels.addChannel.domain.repository.AddChannelRepository
import com.study.channels.shared.domain.model.Channel
import com.study.channels.shared.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.common.validation.Validator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AddChannelUseCase @Inject constructor(
    private val validator: Validator<Channel>,
    private val repository: AddChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(title: String) = withContext(dispatcher) {
        val channel = Channel(NOT_YET_SYNCHRONIZED_ID, title, null)
        validator.validate(channel)
        repository.addChannel(channel)
    }

}
