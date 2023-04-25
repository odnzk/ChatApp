package com.study.components.extensions

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.study.components.R
import retrofit2.HttpException
import java.net.UnknownHostException
import com.study.ui.R as CoreR

class UiError(
    val error: Throwable,
    @StringRes val messageRes: Int,
    @StringRes val descriptionRes: Int? = null,
    @DrawableRes val imageRes: Int? = null
)

class NothingFoundForThisQueryException : RuntimeException()

fun Throwable.toBaseErrorMessage(): UiError = when (this) {
    is UnknownHostException, is HttpException -> UiError(
        this,
        CoreR.string.error_connection_lost,
        CoreR.string.error_description_connection_lost,
        R.drawable.ic_no_connection
    )
    is NothingFoundForThisQueryException -> UiError(
        this,
        CoreR.string.error_not_found,
        CoreR.string.error_description_not_found,
        R.drawable.ic_not_found
    )
    else -> UiError(
        this,
        CoreR.string.error_unknown,
        CoreR.string.error_description_unknown,
        R.drawable.ic_error
    )
}

inline fun showErrorSnackbar(
    view: View,
    error: Throwable,
    errorHandler: ((Throwable) -> UiError) = Throwable::toBaseErrorMessage,
    onReloadActionListener: View.OnClickListener? = null
) {
    val userMessage = view.context.getString(errorHandler(error).messageRes)
    Snackbar.make(view, userMessage, Snackbar.LENGTH_SHORT).apply {
        onReloadActionListener?.let { setAction(R.string.error_snackbar_action, it) }
    }.show()
}
