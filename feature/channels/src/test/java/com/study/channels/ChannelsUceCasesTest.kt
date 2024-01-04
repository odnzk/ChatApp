package com.study.channels

import com.study.channels.common.domain.repository.ChannelRepository
import com.study.channels.common.domain.model.Channel
import com.study.common.validation.Validator
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
internal class ChannelsUceCasesTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val repository: ChannelRepository = mockk()
    private val validator: Validator<Channel> = spyk()

    private companion object{
        const val CHANNEL_TITLE = "channel title"
    }
    // TODO()

    // suspend operator fun invoke(title: String) = withContext(dispatcher) {
    //        val channel = Channel(notYetSynchronizedChannelId.random(), title, null)
    //        validator.validate(channel)
    //        repository.addChannel(channel)
    //    }
//    @Test
//    fun `test add channel use case`(){
//        val useCase = AddChannelUseCase(validator, repository, dispatcher)
//
//        runTest {
//            useCase(CHANNEL_TITLE)
//        }
//    }

}