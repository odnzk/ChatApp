package com.study.chat.actions.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.study.chat.R

internal enum class UiAction(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val successTextRes: Int
) {
    ADD_REACTION(
        R.drawable.ic_baseline_add_reaction_24,
        R.string.item_action_add_reaction,
        R.string.success_action_add_reaction
    ),
    COPY(
        R.drawable.ic_baseline_copy_24,
        R.string.item_action_copy,
        R.string.success_action_copied
    ),
    EDIT(R.drawable.ic_baseline_edit_24, R.string.item_action_edit, R.string.success_action_edit),
    DELETE(
        R.drawable.ic_baseline_delete_24,
        R.string.item_action_delete,
        R.string.success_action_deleted
    )
}
