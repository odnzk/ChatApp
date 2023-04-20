package com.study.components

import androidx.fragment.app.Fragment

interface FragmentPagerFactory {
    fun getFragmentCount(): Int
    fun getFragmentCreator(position: Int): () -> Fragment
}
