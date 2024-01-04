package com.study.components.customview

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.study.ui.R
import vivid.money.elmslie.android.screen.ElmDelegate
import vivid.money.elmslie.android.screen.ElmScreen

abstract class BaseBottomSheetFragment<Event : Any, Effect : Any, State : Any> :
    BottomSheetDialogFragment(),
    ElmDelegate<Event, Effect, State> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ElmScreen(this, lifecycle) { requireActivity() }
        setStyle(STYLE_NORMAL, R.style.BaseBottomSheetDialogTheme)
    }
}
