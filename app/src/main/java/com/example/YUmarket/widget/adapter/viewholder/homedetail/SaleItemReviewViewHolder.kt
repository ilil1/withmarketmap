package com.example.YUmarket.widget.adapter.viewholder.homedetail

import com.example.YUmarket.databinding.ViewholderSaleItemReviewBinding
import com.example.YUmarket.model.homelist.HomeReviewModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder

class SaleItemReviewViewHolder(
    private val binding: ViewholderSaleItemReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<HomeReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
    }

    override fun bindData(model: HomeReviewModel) {
        super.bindData(model)
    }

    override fun bindViews(model: HomeReviewModel, listener: AdapterListener) {
    }
}