package com.example.YUmarket.widget.adapter.viewholder.restaurant

import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.databinding.ViewholderRestaurantBinding
import com.example.YUmarket.model.homelist.HomeItemModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.home.HomeListListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<HomeItemModel>(binding, viewModel,resourcesProvider) {
    override fun reset() = Unit

    override fun bindData(listModel: HomeItemModel) {
        super.bindData(listModel)
      //  binding.restaurantTitle.text = listModel.title
    }

    override fun bindViews(listModel: HomeItemModel, listener: AdapterListener) {
        if (listener is HomeListListener) {
            binding.root.setOnClickListener {
                listener.onClickItem(listModel)
            }
        }
    }
}