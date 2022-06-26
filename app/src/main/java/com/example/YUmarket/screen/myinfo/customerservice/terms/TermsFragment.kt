package com.example.YUmarket.screen.myinfo.customerservice.terms

import androidx.navigation.Navigation
import com.example.YUmarket.R
import com.example.YUmarket.databinding.FragmentTermsBinding
import com.example.YUmarket.screen.base.BaseFragment


class TermsFragment : BaseFragment<FragmentTermsBinding>() {

    private fun backMove() {
        view?.let { it1 ->
            Navigation.findNavController(it1).navigate(R.id.action_termsFragment_to_myInfoFragment)
        }
        backStack()
    }

    override fun getViewBinding(): FragmentTermsBinding =
        FragmentTermsBinding.inflate(layoutInflater)

    override fun observeData()  {}

    override fun initViews() {

        binding.configurationLeft.setOnClickListener {
            backMove()
        }

    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }


}