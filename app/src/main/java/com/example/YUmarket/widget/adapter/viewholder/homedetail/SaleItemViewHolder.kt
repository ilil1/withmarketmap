package com.example.YUmarket.widget.adapter.viewholder.homedetail

import com.example.YUmarket.R
import com.example.YUmarket.databinding.ViewholderNewSaleItemBinding
import com.example.YUmarket.databinding.ViewholderSaleItemBinding
import com.example.YUmarket.extensions.clear
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.homelist.HomeItemModel
import com.example.YUmarket.model.homelist.TownMarketModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.home.HomeItemListener
import com.example.YUmarket.widget.adapter.listener.home.HomeMarketMenuItemListener
import com.example.YUmarket.widget.adapter.listener.home.TownMarketListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder

class SaleItemViewHolder(
    private val binding: ViewholderSaleItemBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<HomeItemModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        marketImageView.clear()
    }

    override fun bindData(model: HomeItemModel) {
        super.bindData(model)

        val disCountedPrice = model.originalPrice - model.salePrice
        val disCountPercent: Int = 100 * disCountedPrice / model.originalPrice

        with(binding) {
            // TODO 실제 데이터를 받아오는 경우 데이터가 잘 반영이 되도록 수정
            marketImageView.load(model.itemImageUrl, 16f)
            itemNameText.text = model.itemName // data
            originPriceTextView.text = resourcesProvider.getString(R.string.home_item_price_format, model.originalPrice)
            disCountPercentTextView.text = resourcesProvider.getString(
                R.string.home_item_discount_percent_format,
                disCountPercent,
                "%"
            )
            salePriceTextView.text = model.salePrice.toString()
        }
    }

    override fun bindViews(model: HomeItemModel, listener: AdapterListener) {
        if(listener is HomeMarketMenuItemListener) {
            with(binding) {
                root.setOnClickListener {
                    listener.onClickItem(model)
                }
            }
        }
    }
}
