package com.example.YUmarket.screen.orderlist

import com.example.YUmarket.databinding.FragmentOrderListBinding
import com.example.YUmarket.screen.base.BaseFragment

class OrderListFragment : BaseFragment<FragmentOrderListBinding>() {

    override fun getViewBinding() =
        FragmentOrderListBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        const val TAG = "OrderListFragment"

        fun newInstance(): OrderListFragment {
            return OrderListFragment()
        }
    }


}