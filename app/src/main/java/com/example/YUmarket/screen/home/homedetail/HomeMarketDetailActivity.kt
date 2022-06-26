package com.example.YUmarket.screen.home.homedetail

import android.content.Context
import android.content.Intent
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.YUmarket.data.entity.home.TownMarketEntity
import com.example.YUmarket.databinding.ActivityHomeMarketDetailBinding
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.homelist.category.HomeListCategory
import com.example.YUmarket.model.homelist.category.HomeMarketDetailCategory
import com.example.YUmarket.screen.base.BaseActivity
import com.example.YUmarket.screen.home.homedetail.information.HomeMarketinformFragment
import com.example.YUmarket.screen.home.homedetail.menu.HomeMarketMenuFragment
import com.example.YUmarket.screen.home.homedetail.review.HomeMarketReviewFragment
import com.example.YUmarket.screen.home.homelist.HomeListFragment
import com.example.YUmarket.screen.home.homemain.HomeMainFragment
import com.example.YUmarket.widget.adapter.HomeMarketDetailListFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class HomeMarketDetailActivity
    : BaseActivity<ActivityHomeMarketDetailBinding>() {

    private val viewModel by viewModel<HomeMarketDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<TownMarketEntity>(HomeMainFragment.TownMarket_KEY)
        )
    }

    override fun getViewBinding()
    = ActivityHomeMarketDetailBinding.inflate(layoutInflater)

    companion object {
        fun newIntent(context: Context, townMarketEntity: TownMarketEntity)
        = Intent(context, HomeMarketDetailActivity::class.java).apply {
            putExtra(HomeMainFragment.TownMarket_KEY, townMarketEntity)
        }
    }

    override fun initState() {
        super.initState()
        viewModel.fetchData()
    }

    override fun initViews() {
        super.initViews()

        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun observeData() = viewModel.HomeMarketDetailStateLiveData.observe(this) {
        when (it) {
            is HomeMarketDetailState.Loading -> {
                handleLoading()
            }
            is HomeMarketDetailState.Success -> {
                handleSuccess(it)
            }
            else -> Unit
        }
    }

    private lateinit var viewPagerAdapter: HomeMarketDetailListFragmentPagerAdapter

    private fun initViewPager(state : TownMarketEntity)  {

//프래그먼트 마다 띄워줘야할 배경을 바꿔준다.
        viewPagerAdapter = HomeMarketDetailListFragmentPagerAdapter(
            this,
            listOf(
                HomeMarketMenuFragment.newInstance(state),
                HomeMarketinformFragment.newInstance(state),
                HomeMarketReviewFragment.newInstance(state),
            )
        )
        val homeMarketDetailCategory = HomeMarketDetailCategory.values()

        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
        //binding.menuAndReviewViewPager.offscreenPageLimit = HomeMarketDetailCategory.values().size

        TabLayoutMediator(binding.menuAndReviewTabLayout, binding.menuAndReviewViewPager) { tab, position ->
            tab.setText(homeMarketDetailCategory[position].categoryNameId)
        }.attach()

    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: HomeMarketDetailState.Success) = with(binding) {

        progressBar.isGone = true

        val townMarketEntity = state.townMarketEntity

        //callButton.isGone = restaurantEntity.restaurantTelNumber == null


        //TownMarketImage.load("https://picsum.photos/200")
        TownMarketMainTitleTextView.text = townMarketEntity.marketName

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(townMarketEntity)
        }

//        deliveryTimeText.text =
//            getString(R.string.delivery_expected_time, restaurantEntity.deliveryTimeRange.first, restaurantEntity.deliveryTimeRange.second)
//        deliveryTipText.text =
//            getString(R.string.delivery_tip_range, restaurantEntity.deliveryTipRange.first, restaurantEntity.deliveryTipRange.second)
//
//        likeText.setCompoundDrawablesWithIntrinsicBounds(
//            ContextCompat.getDrawable(this@RestaurantDetailActivity, if (state.isLiked == true) {
//                R.drawable.ic_heart_enable
//            } else {
//                R.drawable.ic_heart_disable
//            }),
//            null, null, null
//        )
//
//        if (::viewPagerAdapter.isInitialized.not()) {
//            initViewPager(state.restaurantEntity.restaurantInfoId, state.restaurantEntity.restaurantTitle, state.restaurantFoodList)
//        }
//
//        notifyBasketCount(state.foodMenuListInBasket)
//
//        val (isClearNeed, afterAction) = state.isClearNeedInBasketAndAction
//
//        if (isClearNeed) {
//            alertClearNeedInBasket(afterAction)
//        }
    }
}