package com.example.YUmarket.screen.myinfo


import android.os.Bundle
import com.example.YUmarket.databinding.ActivityMyinfoBinding
import com.example.YUmarket.screen.base.BaseActivity


class MyInfoActivity : BaseActivity<ActivityMyinfoBinding>() {


    override fun getViewBinding(): ActivityMyinfoBinding =
        ActivityMyinfoBinding.inflate(layoutInflater)

    override fun observeData() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initViews() = with(binding) {
        super.initViews()


    }

}