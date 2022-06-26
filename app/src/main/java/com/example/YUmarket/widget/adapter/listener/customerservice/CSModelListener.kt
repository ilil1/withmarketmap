package com.example.YUmarket.widget.adapter.listener.customerservice


import com.example.YUmarket.model.customerservicelist.CSModel
import com.example.YUmarket.widget.adapter.listener.AdapterListener

interface CSModelListener: AdapterListener {
    fun onClickItem(listModel: CSModel)
}
