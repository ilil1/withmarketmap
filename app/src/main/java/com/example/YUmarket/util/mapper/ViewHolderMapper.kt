package com.example.YUmarket.util.mapper

import com.example.YUmarket.util.provider.ResourcesProvider
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.YUmarket.databinding.*
import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model
import com.example.YUmarket.model.like.LikeItemModel
import com.example.YUmarket.model.like.LikeMarketModel
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.screen.myinfo.like.LikeListViewModel
import com.example.YUmarket.screen.store.StoreViewModel
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder
import com.example.YUmarket.widget.adapter.viewholder.chat.ChatViewHolder

import com.example.YUmarket.widget.adapter.viewholder.cs.CSViewHolder
import com.example.YUmarket.widget.adapter.viewholder.home.HomeItemModelViewHolder
import com.example.YUmarket.widget.adapter.viewholder.home.TownMarketViewHolder
import com.example.YUmarket.widget.adapter.viewholder.homedetail.SaleItemReviewViewHolder
import com.example.YUmarket.widget.adapter.viewholder.homedetail.SaleItemViewHolder
import com.example.YUmarket.widget.adapter.viewholder.homemain.NearbyMarketViewHolder
import com.example.YUmarket.widget.adapter.viewholder.homemain.NewSaleItemViewHolder
import com.example.YUmarket.widget.adapter.viewholder.like.LikeItemViewHolder
import com.example.YUmarket.widget.adapter.viewholder.like.LikeMarketViewHolder
import com.example.YUmarket.widget.adapter.viewholder.map.MapViewPagerViewHolder
import com.example.YUmarket.widget.adapter.viewholder.store.StoreViewHolder
import com.example.YUmarket.widget.adapter.viewholder.suggest.SuggestViewHolder

object ViewHolderMapper {
    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {

        val inflater = LayoutInflater.from(parent.context)

        return when (type) {
            CellType.HOME_MAIN_MARKET_CELL -> {
                NearbyMarketViewHolder(
                    ViewholderNearbyMarketBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            CellType.HOME_MAIN_ITEM_CELL -> {
                NewSaleItemViewHolder(
                    ViewholderNewSaleItemBinding.inflate(inflater),
                    viewModel, resourcesProvider
                )
            }

            CellType.HOME_TOWN_MARKET_CELL -> {
                TownMarketViewHolder(
                    ViewholderTownMarketBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            CellType.HOME_ITEM_CELL -> {
                HomeItemModelViewHolder(
                    ViewholderHomeItemBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            CellType.CUSTOMER_SERVICE_CELL -> {
                CSViewHolder(
                    ViewholderCsItemBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }
            CellType.SUGGEST_CELL -> {
                SuggestViewHolder(
                    ViewholderSuggestSeasonBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            CellType.HOME_DETAIL_ITEM_CELL -> {
                SaleItemViewHolder(
                    ViewholderSaleItemBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            CellType.LIKE_MARKET_CELL -> LikeMarketViewHolder(
                ViewholderLikeMarketListBinding.inflate(inflater, parent, false),
                viewModel as LikeListViewModel<LikeMarketModel>,
                resourcesProvider
            )


            CellType.LIKE_ITEM_CELL -> LikeItemViewHolder(
                ViewholderLikeItemListBinding.inflate(inflater, parent, false),
                viewModel as LikeListViewModel<LikeItemModel>,
                resourcesProvider
            )


            CellType.MAP_ITEM_CELL -> MapViewPagerViewHolder(
                ViewholderMapViewpagerBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.CHATTING_CELL -> ChatViewHolder(
                ViewholderChatlistBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )

            CellType.STORE_CELL -> StoreViewHolder(
                ViewholderStoreListBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )
            CellType.HOME_REVIEW_ITEM_CELL -> {
                SaleItemReviewViewHolder(
                    ViewholderSaleItemReviewBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }



        } as ModelViewHolder<M>

    }



}