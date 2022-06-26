package com.example.YUmarket.screen.myinfo.like


import com.example.YUmarket.databinding.FragmentLikeBinding
import com.example.YUmarket.model.like.LikeCategory
import com.google.android.material.tabs.TabLayoutMediator
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.widget.adapter.HomeListFragmentPagerAdapter

class LikeFragment
    : BaseFragment<FragmentLikeBinding>() {


    private lateinit var viewPagerAdapter: HomeListFragmentPagerAdapter

    override fun getViewBinding(): FragmentLikeBinding =
        FragmentLikeBinding.inflate(layoutInflater)

    override fun initViews() {
        super.initViews()
        if (!::viewPagerAdapter.isInitialized) initViewPager()
    }

    private fun initViewPager() = with(binding) {
        val likeCategories = LikeCategory.values()

        val likeListFragments = likeCategories.map {
            LikeListFragment.newInstance(it)
        }

        viewPagerAdapter = HomeListFragmentPagerAdapter(
            this@LikeFragment,
            fragmentList = likeListFragments
        )

        likeFragmentViewPager.adapter = viewPagerAdapter
        likeFragmentViewPager.offscreenPageLimit = likeCategories.size

        TabLayoutMediator(likeFragmentTabLayout, likeFragmentViewPager) { tab, position ->
            tab.setText(likeCategories[position].likeCategoryId)
        }.attach()
    }

    override fun observeData() {

    }

}