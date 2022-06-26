package com.example.YUmarket.screen.myinfo.customerservice.list.detail

import androidx.navigation.Navigation
import com.example.YUmarket.R
import com.example.YUmarket.databinding.FragmentDetailBinding
import com.example.YUmarket.model.customerservicelist.ImageData
import com.example.YUmarket.screen.base.BaseFragment


class CSDetailFragment : BaseFragment<FragmentDetailBinding>() {


    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun observeData() {

    }

    override fun initViews() = with(binding) {
        super.initViews()
        val csData = arguments?.getParcelable<ImageData>("data")
        title.text = csData?.csTitle.toString()
        contentTitle.text = csData?.csContentTitle.toString()
        contentBody.text = csData?.csContentBody.toString()


        uturn.setOnClickListener {
            changeFragment()
        }

    }

    private fun changeFragment() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_CSDetailFragment_to_CSFragment)
        }
    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }

    }


}


