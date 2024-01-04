package com.study.channels.presentation.channels.util.pager

import androidx.fragment.app.Fragment

internal interface FragmentPagerFactory {
    fun getFragmentCount(): Int
    fun getFragmentCreator(position: Int): () -> Fragment
}
