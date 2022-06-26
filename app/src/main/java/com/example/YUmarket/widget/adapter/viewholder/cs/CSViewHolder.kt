package com.example.YUmarket.widget.adapter.viewholder.cs

import com.example.YUmarket.databinding.ViewholderCsItemBinding
import com.example.YUmarket.model.customerservicelist.CSModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.customerservice.CSModelListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder

/**
 * @author HeeTae Heo(main),
 * Geonwoo Kim, Doyeop Kim, Namjin Jeong, Eunho Bae (sub)
 * @since
 * @throws
 * @description
 */

class CSViewHolder(
    private val binding: ViewholderCsItemBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<CSModel>(binding,viewModel,resourcesProvider){

    override fun reset() = Unit

    override fun bindData(listModel: CSModel) {
        super.bindData(listModel)
        with(binding) {
            questionText.text = listModel.csTitle

        }
    }


    override fun bindViews(listModel: CSModel, listener: AdapterListener) = with(binding){
        if (listener is CSModelListener) {
            binding.root.setOnClickListener {
                listener.onClickItem(listModel)
            }
        }
    }







}

