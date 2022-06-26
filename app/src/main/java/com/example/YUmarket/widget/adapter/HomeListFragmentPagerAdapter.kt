package com.example.YUmarket.widget.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeListFragmentPagerAdapter(
    fragment: Fragment,
    private val fragmentList: List<Fragment>,
    // TODO latlngEntity
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]
}