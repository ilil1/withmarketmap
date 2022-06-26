package com.example.YUmarket.widget.adapter.viewholder.suggest


import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.databinding.ViewholderSuggestSeasonBinding
import com.example.YUmarket.extensions.clear
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.homelist.SuggestItemModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener
import com.example.YUmarket.widget.adapter.listener.home.SuggestListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder

class SuggestViewHolder(
    private val binding : ViewholderSuggestSeasonBinding,
    viewModel : BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<SuggestItemModel>(binding, viewModel,resourcesProvider)
{
    override fun reset() = with(binding){
     ImageView.clear()
    }


    override fun bindData(model: SuggestItemModel) {
        super.bindData(model)

        with(binding){
            ImageView.load(model.marketImageUrl,16f)
            name.text = model.marketName // data
        }


    }
    override fun bindViews(model: SuggestItemModel, listener: AdapterListener) {
      if(listener is SuggestListener){
          with(binding){
              root.setOnClickListener {
                  listener.onClickItem(model)
              }
          }
      }
    }
}