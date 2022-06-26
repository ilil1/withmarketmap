package com.example.YUmarket.screen.myLocation

import android.app.Activity
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.YUmarket.databinding.ActivitySearchAddressBinding
import com.example.YUmarket.screen.map.MapLocationSetting.MapLocationSettingActivity.Companion.MY_LOCATION_KEY

class SearchAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchAddressBinding

    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.webViewAddress.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
        }

        binding.webViewAddress.apply {
            webViewClient = client
            addJavascriptInterface(AndroidBridge(), "TestApp")
            webChromeClient = chromeClient

            loadUrl("http://13.124.45.228/search.php")
        }
    }

    // TODO : search.php에서 arg1 매개변수 안넣게 하기
    private inner class AndroidBridge { // 웹에서 JavaScript로 android 함수를 호출할 수 있도록 도와줌
        @JavascriptInterface
        open fun setAddress(arg1: String?, arg2: String?, arg3: String?) { // search.php에서 호출되는 함수
            handler.post {
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(
                            MY_LOCATION_KEY,
                            String.format("%s %s", arg2, arg3),
                        )
                    },
                )
                finish()
            }
        }
    }

    // TODO : 제거?
    private val client: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return false
        }

        // TODO : 제거?
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
        }
    }

    private val chromeClient = object : WebChromeClient() {

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {

            val newWebView = WebView(this@SearchAddressActivity).apply {
                settings.javaScriptEnabled = true
            }
            setContentView(newWebView)

            newWebView.webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(
                    view: WebView,
                    url: String,
                    message: String,
                    result: JsResult
                ): Boolean {
                    super.onJsAlert(view, url, message, result)

                    // 선택한 주소 출력
//                    Toast.makeText(
//                        this@SearchAddressActivity,
//                        "결과 : " + result.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()

                    return true
                }
            }
            (resultMsg!!.obj as WebView.WebViewTransport).webView = newWebView
            resultMsg.sendToTarget()

            return true
        }
    }
}