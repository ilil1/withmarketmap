package com.example.YUmarket.screen.myinfo

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.media.Image
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.YUmarket.databinding.CustomDialogBinding

class CustomDialog(private val context : Activity) {

    private lateinit var binding : CustomDialogBinding
    private val dlg = Dialog(context)

    fun show(ct_text : String , top_text:String , Image : Int){
        binding = CustomDialogBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(binding.root)
        dlg.setCancelable(false)

        binding.centerText.text = ct_text

        binding.back.setOnClickListener {
            dlg.dismiss()
        }

        binding.redBtn.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }


}