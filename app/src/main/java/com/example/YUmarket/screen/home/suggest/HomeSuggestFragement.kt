package com.example.YUmarket.screen.home.suggest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.YUmarket.R
import com.example.YUmarket.databinding.FragmentSuggestBinding
import com.example.YUmarket.screen.MainActivity
import com.example.YUmarket.screen.base.BaseFragment

class HomeSuggestFragement : BaseFragment<FragmentSuggestBinding>() {


    override fun getViewBinding(): FragmentSuggestBinding =
        FragmentSuggestBinding.inflate(layoutInflater)

    override fun observeData() {}


    override fun initState() {
        super.initState()
    }

    override fun initViews() = with(binding) {
        super.initViews()

        //  name.text = arguments?.getString("pass")

        var data = arguments?.getString("nameData")
        name.text = data.toString()

        back.setOnClickListener {
//            activity?.let {
//                var intent = Intent(context, MainActivity::class.java)
//                startActivity(intent)
//            }
//            backStack()(
            activity?.finish()
        }
    }



    private fun backStack() {
        activity?.finish()
    }

    companion object {
        const val TAG = "HomeSuggestFragment"

        fun newInstance(): HomeSuggestFragement {
            return HomeSuggestFragement().apply {

            }
        }
    }


}