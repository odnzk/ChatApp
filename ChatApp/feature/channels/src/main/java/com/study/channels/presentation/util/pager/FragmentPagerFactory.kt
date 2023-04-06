package com.study.channels.presentation.util.pager

import androidx.fragment.app.Fragment

internal interface FragmentPagerFactory {
    fun getFragmentCount(): Int
    fun getFragmentCreator(position: Int): () -> Fragment
}
