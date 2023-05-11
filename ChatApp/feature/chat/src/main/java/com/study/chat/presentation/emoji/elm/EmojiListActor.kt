package com.study.chat.presentation.emoji.elm

import com.study.chat.domain.repository.EmojiRepository
import com.study.chat.presentation.util.mapper.toUiEmojis
import com.study.common.extension.toFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class EmojiListActor @Inject constructor(private val repository: EmojiRepository) :
    Actor<EmojiListCommand, EmojiListEvent.Internal> {

    override fun execute(command: EmojiListCommand): Flow<EmojiListEvent.Internal> =
        when (command) {
            EmojiListCommand.LoadEmojiList -> toFlow { repository.getEmoji().toUiEmojis() }
                .mapEvents(
                    EmojiListEvent.Internal::LoadingSuccess,
                    EmojiListEvent.Internal::LoadingError
                )
        }

}
