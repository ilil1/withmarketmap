package com.example.YUmarket.widget.adapter.viewholder.chat

import com.example.YUmarket.databinding.ViewholderChatlistBinding
import com.example.YUmarket.databinding.ViewholderCsItemBinding
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.chat.ChatModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.chat.ChatModelListener
import com.example.YUmarket.widget.adapter.listener.customerservice.CSModelListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder

class ChatViewHolder(
    private val binding: ViewholderChatlistBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<ChatModel>(binding, viewModel, resourcesProvider) {


    override fun reset() {

    }

    override fun bindData(listModel: ChatModel) {
        super.bindData(listModel)
        with(binding) {
            storeName.text = listModel.StoreName
            storeImage.load(listModel.ImageUrl, 0f)
            date.text = listModel.Data
            recentlyText.text = listModel.RecentlyText

        }
    }

    override fun bindViews(listModel: ChatModel, listener: AdapterListener) {
        if (listener is ChatModelListener) {
            binding.root.setOnClickListener {
                listener.onClickItem(listModel)
            }
        }
    }
}