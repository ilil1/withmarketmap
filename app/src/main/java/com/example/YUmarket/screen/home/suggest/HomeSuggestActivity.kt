package com.example.YUmarket.screen.home.suggest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.example.YUmarket.R
import com.example.YUmarket.databinding.ActivitySuggestBinding
import com.example.YUmarket.databinding.FragmentSuggestBinding
import com.example.YUmarket.screen.MainActivity
import com.example.YUmarket.screen.base.BaseActivity
import org.koin.experimental.builder.getArguments

class HomeSuggestActivity : BaseActivity<ActivitySuggestBinding>() {


    var fragment = HomeSuggestFragement
    private val subFragment = HomeSuggestFragement()
    override fun getViewBinding(): ActivitySuggestBinding =
        ActivitySuggestBinding.inflate(layoutInflater)

    override fun observeData() {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right)

        val pass = intent
        var nameData = pass.getStringExtra("data")

        Toast.makeText(this,nameData,Toast.LENGTH_SHORT).show()
//        val bundle = Bundle()
//        bundle.putString("nameData",nameData)
        subFragment.arguments?.putString("nameData",nameData)

//        val bundle = Bundle()
//        bundle.putString("nameData",nameData)

        supportFragmentManager.beginTransaction().
        replace(R.id.suggest_fragment,fragment.newInstance())
            .commit()
    }
    override fun initViews()  {
        super.initViews()


    }



}