package com.study.chat.presentation.actions.util.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.study.chat.R

internal enum class UiAction(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val successTextRes: Int
) {
    ADD_REACTION(
        R.drawable.ic_add_reaction,
        R.string.item_action_add_reaction,
        R.string.success_action_add_reaction
    ),
    COPY(
        R.drawable.ic_copy,
        R.string.item_action_copy,
        R.string.success_action_copied
    ),
    EDIT(R.drawable.ic_edit, R.string.item_action_edit, R.string.success_action_edit),
    DELETE(R.drawable.ic_delete, R.string.item_action_delete, R.string.success_action_deleted)
}
