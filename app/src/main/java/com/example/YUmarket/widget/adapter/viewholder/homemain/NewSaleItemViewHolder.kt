package com.example.YUmarket.widget.adapter.viewholder.homemain
import com.example.YUmarket.R
import com.example.YUmarket.databinding.ViewholderNewSaleItemBinding
import com.example.YUmarket.extensions.clear
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.homelist.HomeItemModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.home.HomeItemListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder


class NewSaleItemViewHolder(
    private val binding: ViewholderNewSaleItemBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<HomeItemModel>(binding, viewModel, resourcesProvider) {


    override fun reset() = with(binding) {
        itemImageView.clear()
    }

    override fun bindData(model: HomeItemModel) {
        super.bindData(model)

        val disCountedPrice = model.originalPrice - model.salePrice
        val disCountPercent: Int = 100 * disCountedPrice / model.originalPrice

        with(binding) {
            itemImageView.load(model.itemImageUrl, 16f)
            itemDistanceTextView.text = "0.1"
            distanceUnitTextView.text =
                resourcesProvider.getString(R.string.distance_unit_kilometer)
            marketNameOfItemTextView.text = model.townMarketModel.marketName
            itemReviewCountTextView.text = model.reviewQuantity.toString()
            itemReviewTextView.text = resourcesProvider.getString(R.string.review)
            itemLikeCountTextView.text = model.likeQuantity.toString()
            //itemLikeTextView.text = resourcesProvider.getString(R.string.like)

            itemNameTextView.text = model.itemName
            originPriceTextView.text =
                resourcesProvider.getString(R.string.home_item_price_format, model.originalPrice)
            disCountPercentTextView.text = resourcesProvider.getString(
                R.string.home_item_discount_percent_format,
                disCountPercent,
                "%"
            )
            salePriceTextView.text =
                resourcesProvider.getString(R.string.home_item_price_format, model.salePrice)
            itemStockTextView.text =
                resourcesProvider.getString(R.string.home_item_stock, model.stockQuantity)
        }
    }

    override fun bindViews(model: HomeItemModel, listener: AdapterListener) = with(binding) {
        if (listener is HomeItemListener) {
            root.setOnClickListener {
                listener.onClickItem(model)
            }
        }
    }
}