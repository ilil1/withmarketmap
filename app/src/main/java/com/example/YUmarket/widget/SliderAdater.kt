package com.example.YUmarket.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.YUmarket.R
import com.example.YUmarket.model.suggest.SliderItem

class SliderAdater internal constructor(
    sliderItems : MutableList<SliderItem>,
    viewPager2: ViewPager2
): RecyclerView.Adapter<SliderAdater.SliderViewHolder>() {

    private val sliderItems : List<SliderItem>
    private val viewPager2 : ViewPager2

    init {
        this.sliderItems = sliderItems
        this.viewPager2 = viewPager2
    }


    class SliderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val imageView : ImageView = itemView.findViewById(R.id.pagerimage)




        fun image(sliderItem : SliderItem){
            imageView.setImageResource(sliderItem.image)
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
       return SliderViewHolder(
           LayoutInflater.from(parent.context).inflate(
               R.layout.slide_item_container,
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
    holder.image(sliderItems[position])
        if(position == sliderItems.size - 2){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
      return sliderItems.size
    }

    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }


}