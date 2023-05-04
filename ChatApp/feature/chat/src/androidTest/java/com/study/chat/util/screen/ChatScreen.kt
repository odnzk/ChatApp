package com.study.chat.util.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.study.chat.R
import com.study.chat.presentation.chat.ChatFragment
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KTextInputLayout
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import com.study.components.R as ComponentsR

class ChatScreen : KScreen<ChatScreen>() {
    private val loadingBar = KProgressBar { withId(ComponentsR.id.view_screen_state_pb_loading) }
    private val btnTryAgain = KButton { withId(ComponentsR.id.view_screen_state_btn_try_again) }
    private val tvError = KTextView { withId(ComponentsR.id.view_screen_state_tv_error) }
    private val tvErrorDescription =
        KTextView { withId(ComponentsR.id.view_screen_state_tv_error_description) }
    private val ivError = KImageView { withId(ComponentsR.id.view_screen_state_iv_error) }

    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val inputViewTextInputLayout = KTextInputLayout { withId(R.id.view_input_til_message) }
    val chatTopic = KTextView { withId(R.id.fragment_chat_tv_topic_title) }
    val messageList =
        KRecyclerView({ withId(R.id.fragment_chat_rv_chat) }, { itemType { MessageItem(it) } })

    class MessageItem(parent: Matcher<View>) : KRecyclerItem<MessageItem>(parent) {
        val content = KTextView(parent) { withId(R.id.view_message_tv_content) }
        val reactions = KView(parent) { withId(R.id.view_message_fl_emoji) }

        fun checkIfDisplayed() {
            isDisplayed()
            content.isDisplayed()
        }

    }

    fun checkScreenStateView(state: TestState) {
        when (state) {
            TestState.SUCCESS -> {
                loadingBar.isNotDisplayed()
                tvError.isNotDisplayed()
                tvErrorDescription.isNotDisplayed()
                btnTryAgain.isNotDisplayed()
                ivError.isNotDisplayed()
            }
            TestState.ERROR -> {
                loadingBar.isNotDisplayed()
                tvError.isDisplayed()
                ivError.isDisplayed()
            }
            TestState.LOADING -> {
                loadingBar.isNotDisplayed()
                tvError.isNotDisplayed()
                tvErrorDescription.isNotDisplayed()
                btnTryAgain.isNotDisplayed()
                ivError.isNotDisplayed()
            }
        }
    }
}

