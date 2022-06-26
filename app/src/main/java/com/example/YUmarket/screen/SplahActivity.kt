package com.example.YUmarket.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.YUmarket.R
import com.example.YUmarket.databinding.ActivitySplahBinding
import com.example.YUmarket.screen.base.BaseActivity
import com.example.YUmarket.screen.login.LoginActivity

class SplahActivity : BaseActivity<ActivitySplahBinding>() {
    override fun getViewBinding(): ActivitySplahBinding =
        ActivitySplahBinding.inflate(layoutInflater)

    override fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            val intent = Intent(this,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
                       finish()
        },DURATION)
    }
    companion object{
        private const val DURATION : Long = 3000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
