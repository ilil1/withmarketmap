package com.example.YUmarket.widget.adapter.viewholder.like

import com.example.YUmarket.R
import com.example.YUmarket.databinding.ViewholderLikeMarketListBinding
import com.example.YUmarket.extensions.clear
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.like.LikeMarketModel
import com.example.YUmarket.screen.myinfo.like.LikeListViewModel
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.like.LikeListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder

class LikeMarketViewHolder(
    private val binding: ViewholderLikeMarketListBinding,
    viewModel: LikeListViewModel<LikeMarketModel>,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<LikeMarketModel>(binding, viewModel, resourcesProvider) {
    override fun reset() =
        binding.likeMarketImageView.clear()

    override fun bindData(model: LikeMarketModel) = with(binding) {
        super.bindData(model)

        likeMarketImageView.load(model.imageUrl)
        likeMarketNameTextView.text = model.marketName
        likeMarketDistanceTextView.text =
            resourcesProvider.getString(R.string.distance_format, model.distance)

        if (model.isOpen) {
            likeMarketStateChip.apply {
                chipBackgroundColor = resourcesProvider.getColorStateList(R.color.yellow)
                text = resourcesProvider.getString(R.string.market_open)
            }
        }
    }

    override fun bindViews(model: LikeMarketModel, listener: AdapterListener) {
        if (listener is LikeListener) {
            with(binding) {
                root.setOnClickListener { listener.onClick(model) }
                likeMarketDeleteButton.setOnClickListener { listener.onDeleteClick(model) }
            }
        }
    }
}