package com.example.YUmarket.screen.myinfo.customerservice.configuration


import androidx.navigation.Navigation
import com.example.YUmarket.BuildConfig
import com.example.YUmarket.R
import com.example.YUmarket.databinding.FragmentConfigurationBinding
import com.example.YUmarket.screen.base.BaseFragment


/**
 * @author HeeTae Heo(main),
 * Geonwoo Kim, Doyeop Kim, Namjin Jeong, Eunho Bae (sub)
 * @since
 * @throws
 * @description
 */

class ConfigurationFragment : BaseFragment<FragmentConfigurationBinding>() {

    val versionNumber = BuildConfig.VERSION_NAME

    override fun getViewBinding(): FragmentConfigurationBinding =
        FragmentConfigurationBinding.inflate(layoutInflater)


    override fun observeData() = with(binding) {
        initViews()
    }

    private fun backMove() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_configurationFragment_to_myInfoFragment)
        }
        backStack()


    }


    override fun initViews() = with(binding) {
        binding.versionCode.text = versionNumber
        binding.configurationLeft.setOnClickListener { backMove() }
    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }

}



