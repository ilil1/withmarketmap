package com.example.YUmarket.screen.home


import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.YUmarket.databinding.FragmentHomeBinding
import com.example.YUmarket.model.homelist.category.HomeListCategory
import com.example.YUmarket.screen.MainState
import com.example.YUmarket.screen.MainViewModel
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.screen.home.homelist.HomeListFragment
import com.example.YUmarket.widget.adapter.HomeListFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.sharedViewModel


class HomeFragment
    : BaseFragment<FragmentHomeBinding>() {

    private lateinit var viewPagerAdapter: HomeListFragmentPagerAdapter

    private val activityViewModel by sharedViewModel<MainViewModel>()

    // 22.01.19 Navigation SafeArgs by 정남진
    private val args by navArgs<HomeFragmentArgs>()

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun initViews() {
        super.initViews()
    }

    override fun observeData() = with(binding) {
        activityViewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.Success -> {
                    initViewPager()

                    // 22.01.19 View Pager의 현재 Item을 SafeArgs로 받아온 Tab으로 설정
                    // by 정남진
                    viewPager.setCurrentItem(args.goToTab.ordinal, false)
                }
            }
        }
    }

    private fun initViewPager() = with(binding) {
        orderChipGroup.isVisible = true

        if (::viewPagerAdapter.isInitialized.not()) {
            val homeListCategories = HomeListCategory.values()

            val homeListFragmentList = homeListCategories.map {
//                RestaurantListFragment.newInstance(it, locationLatLng)
                HomeListFragment.newInstance(it)
            }
            viewPagerAdapter = HomeListFragmentPagerAdapter(
                this@HomeFragment,
                homeListFragmentList,
//                locationLatLng
            )
            viewPager.adapter = viewPagerAdapter

            viewPager.offscreenPageLimit = homeListCategories.size

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(homeListCategories[position].categoryNameId)
            }.attach()
        }


//        if (locationLatLng != viewPagerAdapter.locationLatLng) {
//            viewPagerAdapter.locationLatLng = locationLatLng
//            viewPagerAdapter.fragmentList.forEach {
//                it.viewModel.setLocationLatLng(locationLatLng)
//            }
//        }
    }

}