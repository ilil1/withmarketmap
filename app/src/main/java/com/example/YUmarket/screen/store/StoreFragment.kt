package com.example.YUmarket.screen.store

import android.widget.ArrayAdapter
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.YUmarket.R
import com.example.YUmarket.databinding.FragmentStoreBinding
import com.example.YUmarket.model.store.StoreItemModel
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.ModelRecyclerAdapter
import com.example.YUmarket.widget.adapter.listener.store.StoreItemListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class StoreFragment : BaseFragment<FragmentStoreBinding>() {

    private val viewModel by viewModel<StoreViewModel>()
    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding(): FragmentStoreBinding =
        FragmentStoreBinding.inflate(layoutInflater)

    override fun observeData() = with(viewModel) {
        storeListData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


    }


    override fun initViews() = with(binding) {
        super.initViews()


        //    setSpinner()



        storeRec.adapter = adapter
        storeRec.layoutManager = GridLayoutManager(
            requireContext(),
            1,
            GridLayoutManager.VERTICAL,
            false
        )


    }

    override fun initState() = with(viewModel) {
        fetchData()
        super.initState()

    }


    private val adapter by lazy {
        ModelRecyclerAdapter<StoreItemModel, StoreViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            object : StoreItemListener {
                override fun onClickItem(listModel: StoreItemModel) {
                    Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }







//    private fun setSpinner(){
//        val spin = resources.getStringArray(R.array.spinner)
//        val adapter = ArrayAdapter(requireContext(), R.layout.fragment_store, spin)
//        binding.spinner.adapter = adapter
//    }


}