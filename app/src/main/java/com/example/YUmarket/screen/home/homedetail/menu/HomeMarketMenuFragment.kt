package com.example.YUmarket.screen.home.homedetail.menu


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.home.TownMarketEntity
import com.example.YUmarket.databinding.FragmentHomeMarketMenuBinding
import com.example.YUmarket.model.homelist.HomeItemModel
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.ModelRecyclerAdapter
import com.example.YUmarket.widget.adapter.listener.home.HomeMarketMenuItemListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class HomeMarketMenuFragment : BaseFragment<FragmentHomeMarketMenuBinding>() {

    override fun getViewBinding() = FragmentHomeMarketMenuBinding.inflate(layoutInflater)

    private val SaleList
            by lazy<TownMarketEntity> { arguments?.getParcelable(SALE_LIST_KEY)!! }

    private val viewModel by viewModel<HomeMarketMenuViewModel>()
    private val resourcesProvider by inject<ResourcesProvider>()

    private val newSaleItemsAdapter by lazy {
        ModelRecyclerAdapter<HomeItemModel, HomeMarketMenuViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            object : HomeMarketMenuItemListener {
                override fun onClickItem(model: HomeItemModel) {
                    // TODO 22.01.25 start detail market activity when clicked
                    Toast.makeText(requireContext(), model.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        restaurantRecyclerView.adapter = newSaleItemsAdapter
        restaurantRecyclerView.layoutManager = LinearLayoutManager(this@HomeMarketMenuFragment.context)
    }

    override fun initState() {
        super.initState()
        viewModel.fetchData()
    }

    override fun observeData() = with(viewModel) {

        itemData.observe(viewLifecycleOwner) {
            when (it) {

                is HomeMarketMenuState.Uninitialized -> {

                }

                is HomeMarketMenuState.Loading -> {

                }

                is HomeMarketMenuState.ListLoaded -> {
                    viewModel.setItemFilterTest(SaleList)
                }

                is HomeMarketMenuState.Success<*> -> with(binding) {

                    disCountPercentTextView.text = it.allSaleList.size.toString() + "개"
                    newSaleItemsAdapter.submitList(it.modelList)

                    checkboxTitleTextView.setOnCheckedChangeListener { buttonView, isChecked ->
                        when (isChecked){
                            true -> newSaleItemsAdapter.submitList(it.saleList)
                            false -> newSaleItemsAdapter.submitList(it.modelList)
                        }
                    }
                }

                is HomeMarketMenuState.Error -> {
                    Toast.makeText(
                        context,
                        R.string.cannot_load_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        const val SALE_LIST_KEY = "saleList"

        fun newInstance(saleList: TownMarketEntity): HomeMarketMenuFragment {
            Log.d("TAG", "HomeMarketMenuFragment() ")
            val bundle = Bundle().apply {
                putParcelable(SALE_LIST_KEY, saleList)
            }
            return HomeMarketMenuFragment().apply {
                arguments = bundle
            }
        }
    }
}