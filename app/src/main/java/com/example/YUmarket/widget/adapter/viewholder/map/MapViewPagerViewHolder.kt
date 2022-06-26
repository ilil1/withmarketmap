package com.example.YUmarket.widget.adapter.viewholder.map

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.shop.ShopInfoEntity
import com.example.YUmarket.databinding.ViewholderMapViewpagerBinding
import com.example.YUmarket.extensions.clear
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.map.MapItemModel
import com.example.YUmarket.model.map.MapMarketModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.map.MapItemListAdapterListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

class MapViewPagerViewHolder(
    private val binding: ViewholderMapViewpagerBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<MapItemModel>(binding, viewModel, resourcesProvider) {

    lateinit var market: ShopInfoEntity

    override fun reset() {
        binding.ivViewpagerProfile.clear()
    }

    override fun bindData(model: MapItemModel) {
        super.bindData(model)

        with(binding) {
            ivViewpagerProfile.load(model.itemImageUrl)
            tvViewpagerTitle.text = model.itemName
            tvViewpagerBranch.text = market.branch_name ?: "본점"
            tvViewpagerPrice.text =
                resourcesProvider.getString(R.string.price_format, model.originalPrice)
            tvViewpagerDiscountRate.text =
                resourcesProvider.getString(R.string.discount_percent_format, model.discountRate)
            tvViewpagerDiscountedPrice.text = resourcesProvider.getString(
                R.string.price_format,
                (model.originalPrice * (100.0f - model.discountRate) * 0.01f).roundToInt()
            )
            //tvViewpagerPage.text = "${adapterPosition + 1} / ${market.items.size}"
        }
    }

    override fun bindViews(model: MapItemModel, listener: AdapterListener) {
        if (listener is MapItemListAdapterListener) {
            binding.root.setOnClickListener { listener.onClickItem(model) }
        }
    }

    companion object ZoomOutTransformer : ViewPager2.PageTransformer {

        private const val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
        private const val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }

                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }

                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}

