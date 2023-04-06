package com.study.channels.presentation.util.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

internal class PagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentFactory: FragmentPagerFactory
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragmentFactory.getFragmentCount()
    override fun createFragment(position: Int): Fragment {
        return fragmentFactory.getFragmentCreator(position).invoke()
    }
}

