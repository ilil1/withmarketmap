package com.example.YUmarket.widget.adapter.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.YUmarket.R

class ViewPagerAdapter : PagerAdapter(){
    private var mContext : Context?=null

    fun ViewPagerAdapter(context: Context){
        mContext=context;
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.page,container,false)
        view.setBackgroundResource(position)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
       return 5
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return (view == `object`)
    }
}