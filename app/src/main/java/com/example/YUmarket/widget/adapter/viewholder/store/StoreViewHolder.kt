package com.example.YUmarket.widget.adapter.viewholder.store

import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.databinding.ViewholderStoreListBinding
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.store.StoreItemModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.store.StoreItemListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder

class StoreViewHolder(
    private val binding: ViewholderStoreListBinding,
    viewmodel:BaseViewModel,
    resourcesProvider: ResourcesProvider,
):ModelViewHolder<StoreItemModel>(binding,viewmodel,resourcesProvider) {

    override fun reset() {}

    override fun bindViews(model: StoreItemModel, listener: AdapterListener) {
        if(listener is StoreItemListener){
            binding.root.setOnClickListener {
                listener.onClickItem(model)
            }
        }
    }

    override fun bindData(model: StoreItemModel) {
         super.bindData(model)
        with(binding) {
            storeImage.load(model.url, 0f)
            storeName.text = model.name
            storeService.text = model.menu
            km.text = model.distance
        }
    }

}