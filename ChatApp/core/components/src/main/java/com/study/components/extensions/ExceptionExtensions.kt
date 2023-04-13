package com.study.components.extensions

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import coil.network.HttpException
import com.google.android.material.snackbar.Snackbar
import com.study.components.R
import java.net.UnknownHostException
import com.study.ui.R as CoreR

class UserFriendlyError(
    val error: Throwable,
    @StringRes val messageRes: Int,
    @StringRes val descriptionRes: Int? = null,
    @DrawableRes val imageRes: Int? = null
)

fun Throwable.toBaseErrorMessage(): UserFriendlyError {
    val (messageRes, descriptionRes, imageRes) = when (this) {
        is UnknownHostException -> Triple(
            CoreR.string.error_connection_lost,
            CoreR.string.error_description_connection_lost,
            R.drawable.ic_no_connection
        )
        is HttpException -> Triple(
            CoreR.string.error_http,
            CoreR.string.error_description_http,
            R.drawable.ic_error
        )
        else -> Triple(
            CoreR.string.error_unknown,
            CoreR.string.error_description_unknown,
            R.drawable.ic_error
        )
    }
    return UserFriendlyError(this, messageRes, descriptionRes, imageRes)
}

inline fun showErrorSnackbar(
    view: View,
    error: Throwable,
    errorHandler: ((Throwable) -> UserFriendlyError) = Throwable::toBaseErrorMessage,
    onReloadActionListener: View.OnClickListener? = null
) {
    val userMessage = view.context.getString(errorHandler(error).messageRes)
    Snackbar.make(view, userMessage, Snackbar.LENGTH_SHORT).apply {
        onReloadActionListener?.let { setAction(R.string.error_snackbar_action, it) }
    }.show()
}
