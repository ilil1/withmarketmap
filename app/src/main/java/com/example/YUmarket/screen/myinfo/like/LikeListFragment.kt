package com.example.YUmarket.screen.myinfo.like

import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.YUmarket.databinding.FragmentLikeListBinding
import com.example.YUmarket.model.Model
import com.example.YUmarket.model.like.LikeCategory
import com.example.YUmarket.model.like.LikeItemModel
import com.example.YUmarket.model.like.LikeMarketModel
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.ModelRecyclerAdapter
import com.example.YUmarket.widget.adapter.listener.like.LikeListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.qualifier.named

class LikeListFragment<T : Model> : BaseFragment<FragmentLikeListBinding>() {
    override fun getViewBinding() =
        FragmentLikeListBinding.inflate(layoutInflater)

    private val category by lazy {
            requireArguments().getSerializable(FRAGMENT_CATEGORY) as LikeCategory
    }

    private lateinit var viewModel: LikeListViewModel<T>
    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<T, LikeListViewModel<T>>(
            listOf<T>(), viewModel, resourcesProvider,
            object : LikeListener {
                override fun onClick(model: Model) {
                    if (category == LikeCategory.MARKET) {
                        Toast.makeText(
                            context,
                            "LikeMarketModel : ${(model as LikeMarketModel).marketName}",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Toast.makeText(
                            context,
                            "LikeItemModel : ${(model as LikeItemModel).itemName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun onDeleteClick(model: Model) {
                    viewModel.delete(model as T)
                }
            }
        )
    }

    override fun initState() {
        viewModel = getViewModel(qualifier = named(category))
        super.initState()
    }

    override fun initViews() {
        super.initViews()

        binding.likeListRecyclerView.adapter = adapter
    }

    override fun observeData() {
        viewModel.likeData.observe(viewLifecycleOwner) {
            when (it) {
                is LikeState.Uninitialized -> viewModel.fetchData()

                is LikeState.Loading -> {
                    // TODO: 2022.02.21 Handle loading
                }

                is LikeState.Success<*> -> adapter.submitList(it.modelList)

                is LikeState.Error -> {
                    // TODO: 2022.02.21 Handle error
                }
            }
        }
    }

    companion object {
        const val FRAGMENT_CATEGORY = "FRAGMENT_CATEGORY"

        fun newInstance(category: LikeCategory): LikeListFragment<out Model> {
            val likeListFragment = if (category == LikeCategory.MARKET) {
                LikeListFragment<LikeMarketModel>()
            } else {
                LikeListFragment<LikeItemModel>()
            }

            return likeListFragment.apply {
                arguments = bundleOf(FRAGMENT_CATEGORY to category)
            }
        }
    }
}