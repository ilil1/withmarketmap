package com.example.YUmarket.screen.home.homedetail.information

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.home.TownMarketEntity
import com.example.YUmarket.screen.home.homedetail.menu.HomeMarketMenuFragment


class HomeMarketinformFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_marketinform, container, false)
    }

    companion object {

        fun newInstance(saleList: TownMarketEntity): HomeMarketinformFragment {
            Log.d("TAG", "HomeMarketMenuFragment() ")
            return HomeMarketinformFragment().apply {
            }
        }
    }
}