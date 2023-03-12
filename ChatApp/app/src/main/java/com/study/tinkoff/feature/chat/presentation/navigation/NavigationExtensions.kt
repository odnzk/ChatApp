package com.study.tinkoff.feature.chat.presentation.navigation

import androidx.navigation.NavController
import com.study.tinkoff.feature.chat.presentation.ChatFragmentDirections

fun NavController.toSelectEmojiFragment() =
    navigate(ChatFragmentDirections.actionChatFragmentToSelectEmojiFragment())
