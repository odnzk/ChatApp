package com.study.chat.emoji.presentation.elm

import com.study.chat.chat.presentation.util.mapper.toUiEmojis
import com.study.chat.emoji.domain.usecase.GetEmojiListUseCase
import com.study.common.ext.toFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class EmojiListActor @Inject constructor(private val getEmojiListUseCase: GetEmojiListUseCase) :
    Actor<EmojiListCommand, EmojiListEvent.Internal> {

    override fun execute(command: EmojiListCommand): Flow<EmojiListEvent.Internal> =
        when (command) {
            EmojiListCommand.LoadEmojiList -> toFlow { getEmojiListUseCase().toUiEmojis() }
                .mapEvents(
                    EmojiListEvent.Internal::LoadingSuccess,
                    EmojiListEvent.Internal::LoadingError
                )
        }

}
