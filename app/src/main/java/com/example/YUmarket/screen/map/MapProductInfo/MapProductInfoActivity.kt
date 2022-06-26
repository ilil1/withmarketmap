package com.example.YUmarket.screen.map.MapProductInfo

import android.content.Context
import android.content.Intent
import com.example.YUmarket.databinding.ActivityMapProductInfoBinding
import com.example.YUmarket.extensions.load
import com.example.YUmarket.model.map.MapItemModel
import com.example.YUmarket.screen.base.BaseActivity
import kotlin.math.roundToInt


class MapProductInfoActivity : BaseActivity<ActivityMapProductInfoBinding>() {

    override fun initViews(){
        val item = intent.extras?.getParcelable<MapItemModel>(MAP_ITEM_MODEL_KEY)

        with(binding){
            if (item != null) {
                ivProductInfoProfile.load(item.itemImageUrl)
            }
            tvProductName.text = item?.itemName
            tvDiscountedPrice.text = "${(item!!.originalPrice * (100.0f - item!!.discountRate) * 0.01f).roundToInt()}원"
        }
    }

    override fun getViewBinding(): ActivityMapProductInfoBinding =
        ActivityMapProductInfoBinding.inflate(layoutInflater)

    override fun observeData() = Unit

    companion object {
        private const val MAP_ITEM_MODEL_KEY = "MAP_ITEM_MODEL_KEY"

        fun newIntent(context: Context, mapItemModel: MapItemModel) =
            Intent(context, MapProductInfoActivity::class.java).apply {
                putExtra(MAP_ITEM_MODEL_KEY, mapItemModel)
            }
    }
}