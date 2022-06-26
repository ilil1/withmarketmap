package com.example.YUmarket.screen.myinfo.customerservice.list

import com.example.YUmarket.util.provider.ResourcesProvider
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.YUmarket.R
import com.example.YUmarket.databinding.FragmentCsListBinding
import com.example.YUmarket.model.customerservicelist.CSModel
import com.example.YUmarket.model.customerservicelist.ImageData
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.widget.adapter.ModelRecyclerAdapter
import com.example.YUmarket.widget.adapter.listener.customerservice.CSModelListener
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * @author HeeTae Heo(main),
 * Geonwoo Kim, Doyeop Kim, Namjin Jeong, Eunho Bae (sub)
 * @since
 * @throws
 * @description
 */

class CSListFragment : BaseFragment<FragmentCsListBinding>() {


    private val viewModel by viewModel<CSListViewModel> {
        parametersOf(csCategory)
    }
    private val csCategory by lazy {
        arguments?.getSerializable(CS_CATEGORY_KEY) as CSCategory
    }


//    private val viewModel by viewModel<CSListViewModel> {
//       // parametersOf(args.csCategory)
//    }
//
//   // private val args by navArgs<CSFragmentArgs>()

    override fun getViewBinding(): FragmentCsListBinding =
        FragmentCsListBinding.inflate(layoutInflater)

    override fun observeData() = with(viewModel) {
        csListData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private val resourcesProvider by inject<ResourcesProvider>()


    private val adapter by lazy {
        ModelRecyclerAdapter<CSModel, CSListViewModel>(
            listOf(), viewModel, resourcesProvider,
            object : CSModelListener {
                override fun onClickItem(listModel: CSModel) {
                    val data = ImageData(listModel.csTitle, listModel.csContentTitle,listModel.csContentBody,listModel.csAuthor)
                    val bundle = Bundle()
                    bundle.putParcelable("data", data)
                    view?.let{
                        backStack()
                    }
                    view?.let { it1 ->
                        Navigation.findNavController(it1)
                            .navigate(R.id.action_CSCenterFragment_to_CSDetailFragment, bundle)
                    }
//                        val intent = Intent(context, CSDetailFragment::class.java).apply {
//                           val data = ImageData(listModel.csTitle,listModel.csContent,listModel.csAuthor)
//                            putExtra(CS_CATEGORY_KEY,data)
//                            putExtra("CSTitle", listModel.csTitle)
//                            putExtra("CSContent", listModel.csContent)
//                            putExtra("CSAuthor", listModel.csAuthor)
//                            putExtra("CSid", listModel.id)
                    //                       }
//                        startActivity(intent)
                }
            }

        )

    }

    override fun initState() = with(viewModel) {
        fetchData()
        super.initState()
    }

    override fun initViews() = with(viewModel) {

        super.initViews()
        binding.csRecyclerView.adapter = adapter
        binding.csRecyclerView.layoutManager = LinearLayoutManager(this@CSListFragment.context)




    }

    companion object {
        const val CS_CATEGORY_KEY = "CSCategoryKey"
        fun newInstance(csCategory: CSCategory): CSListFragment {
            val bundle = Bundle().apply {
                putSerializable(CS_CATEGORY_KEY, csCategory)
            }

            return CSListFragment().apply {
                arguments = bundle
            }
        }
    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }
}

