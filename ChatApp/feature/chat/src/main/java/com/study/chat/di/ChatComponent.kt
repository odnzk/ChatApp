package com.study.chat.di

import com.study.chat.presentation.actions.ActionsFragment
import com.study.chat.presentation.chat.ChatFragment
import com.study.chat.presentation.edit.EditMessageFragment
import com.study.chat.presentation.emoji.EmojiListFragment
import com.study.common.di.FeatureScope
import dagger.Component

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

    @Component.Factory
    interface Factory {
        fun create(dependencies: ChatDep): ChatComponent
    }
}

