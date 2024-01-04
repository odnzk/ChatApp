package com.study.components.ext

import android.graphics.Color
import android.view.View
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.study.common.search.NothingFoundForThisQueryException
import com.study.components.R
import com.study.components.model.UiError
import com.study.network.model.ConnectionLostException
import com.study.network.model.NetworkException
import com.study.ui.R as CoreR

fun Throwable.toBaseErrorMessage(): UiError = when (this) {
    is ConnectionLostException -> UiError(
        this,
        messageRes = CoreR.string.error_connection_lost,
        descriptionRes = CoreR.string.error_description_connection_lost,
        imageRes = R.drawable.ic_no_connection
    )
    is NetworkException -> UiError(
        this,
        messageRes = CoreR.string.error_network,
        descriptionRes = CoreR.string.error_description_network,
        descriptionArgs = message,
        imageRes = R.drawable.ic_no_connection
    )
    is NothingFoundForThisQueryException -> UiError(
        this,
        messageRes = CoreR.string.error_not_found,
        descriptionRes = CoreR.string.error_description_not_found,
        imageRes = R.drawable.ic_not_found
    )
    else -> UiError(
        this,
        messageRes = CoreR.string.error_unknown,
        descriptionRes = CoreR.string.error_description_unknown,
        imageRes = R.drawable.ic_error
    )
}

inline fun ViewBinding.showErrorSnackbar(
    error: Throwable,
    errorHandler: ((Throwable) -> UiError) = Throwable::toBaseErrorMessage,
    customMessage: String? = null,
    onReloadActionListener: View.OnClickListener? = null
) {
    val displayedMessage = customMessage ?: errorHandler(error).getMessage(root.context)
    Snackbar.make(root, displayedMessage, Snackbar.LENGTH_SHORT).apply {
        setTextColor(Color.WHITE)
        onReloadActionListener?.let { setAction(R.string.error_snackbar_action, it) }
    }.show()
}
