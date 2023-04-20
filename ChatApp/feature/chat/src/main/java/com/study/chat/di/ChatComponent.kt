package com.study.chat.di

import com.study.chat.presentation.chat.ChatFragment
import com.study.chat.presentation.emoji.EmojiListFragment
import com.study.common.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    dependencies = [ChatDep::class],
    modules = [ChatRepositoryModule::class, ChatModule::class]
)
internal interface ChatComponent {
    fun inject(fragment: ChatFragment)
    fun inject(fragment: EmojiListFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: ChatDep): ChatComponent
    }
}

