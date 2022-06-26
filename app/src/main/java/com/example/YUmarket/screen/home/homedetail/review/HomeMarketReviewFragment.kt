package com.example.YUmarket.screen.home.homedetail.review

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.home.TownMarketEntity
import com.example.YUmarket.databinding.FragmentHomeMarketReviewBinding
import com.example.YUmarket.model.homelist.HomeReviewModel
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.ModelRecyclerAdapter
import com.example.YUmarket.widget.adapter.listener.home.HomeMarketReviewListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class HomeMarketReviewFragment : BaseFragment<FragmentHomeMarketReviewBinding>() {

    private val resourcesProvider by inject<ResourcesProvider>()
    private val viewModel by viewModel<HomeMarketReviewViewModel>()

    override fun getViewBinding(): FragmentHomeMarketReviewBinding
            = FragmentHomeMarketReviewBinding.inflate(layoutInflater)

    private val ItemsReviewAdapter by lazy {
        ModelRecyclerAdapter<HomeReviewModel, HomeMarketReviewViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            object : HomeMarketReviewListener {
                override fun onClickItem(model: HomeReviewModel) {
                    // TODO 22.01.25 start detail market activity when clicked
                    Toast.makeText(requireContext(), model.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        ReviewRecyclerView.adapter = ItemsReviewAdapter
        ReviewRecyclerView.layoutManager = LinearLayoutManager(this@HomeMarketReviewFragment.context)
    }

    override fun initState() {
        super.initState()
        viewModel.fetchData()
    }

    override fun observeData() {
        viewModel.chatListData.observe(viewLifecycleOwner) {
            ItemsReviewAdapter.submitList(it)
        }
    }

    companion object {

        fun newInstance(saleList: TownMarketEntity): HomeMarketReviewFragment {
            Log.d("TAG", "HomeMarketReviewFragment() ")
            return HomeMarketReviewFragment().apply {
            }
        }
    }
}