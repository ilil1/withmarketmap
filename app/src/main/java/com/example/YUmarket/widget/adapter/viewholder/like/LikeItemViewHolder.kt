package com.example.YUmarket.widget.adapter.viewholder.like

import com.example.YUmarket.R
import com.example.YUmarket.databinding.ViewholderLikeItemListBinding
import com.example.YUmarket.extensions.clear
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.like.LikeItemModel
import com.example.YUmarket.screen.myinfo.like.LikeListViewModel
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.like.LikeListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder


class LikeItemViewHolder(
    private val binding: ViewholderLikeItemListBinding,
    viewModel: LikeListViewModel<LikeItemModel>,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<LikeItemModel>(binding, viewModel, resourcesProvider) {
    override fun reset() =
        binding.likeItemImageView.clear()

    override fun bindData(model: LikeItemModel) = with(binding) {
        super.bindData(model)

        likeItemImageView.load(model.imageUrl)
        likeItemNameTextView.text = model.itemName

        likeItemMarketAndDistanceTextView.text = resourcesProvider.getString(
            R.string.like_item_market_and_distance_format,
            model.marketName,
            model.marketDistance
        )

        likeItemDiscountTextView.text = resourcesProvider.getString(
            R.string.discount_percent_format, model.saleRatio
        )

        likeItemPriceTextView.text = resourcesProvider.getString(
            R.string.price_format, model.price
        )
    }

    override fun bindViews(model: LikeItemModel, listener: AdapterListener) {
        if (listener is LikeListener) {
            with(binding) {
                root.setOnClickListener { listener.onClick(model) }
                likeItemDeleteButton.setOnClickListener { listener.onDeleteClick(model) }
            }
        }
    }
}