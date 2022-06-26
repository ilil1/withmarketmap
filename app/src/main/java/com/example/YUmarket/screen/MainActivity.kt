package com.example.YUmarket.screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.content.pm.ConfigurationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.example.YUmarket.MyApplication

import com.example.YUmarket.R
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity
import com.example.YUmarket.databinding.ActivityMainBinding
import com.example.YUmarket.screen.base.BaseActivity
import com.example.YUmarket.screen.map.MapLocationSetting.MapLocationSettingActivity
import com.example.YUmarket.screen.myLocation.MyLocationActivity
import com.example.YUmarket.util.PreferenceManager
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity
    : BaseActivity<ActivityMainBinding>() {

    private lateinit var curLoc: Location
    private val handler = Handler()
    private var doubleBackToExit = false

    companion object {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    // Navigation에 사용할 Controller
    private val navController by lazy {
        val hostContainer =
            supportFragmentManager
                .findFragmentById(R.id.fragmentContainer)
                    as NavHost

        hostContainer.navController
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter {
                it.key in locationPermissions
            }

            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) {
                setLocationListener()
            } else {
                Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingPermission")
    private fun checkLocation(): Boolean {
        val location: Location?
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            binding.locationTitleTextView.text = location?.toString()
            return true
        }
        return false
    }

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { results ->
            results.data?.getParcelableExtra<MapSearchInfoEntity>(MapLocationSettingActivity.MY_LOCATION_KEY)
                ?.let { myLocationInfo ->
                    viewModel.getReverseGeoInformation(myLocationInfo.locationLatLng)
                    viewModel.setDestinationLocation(myLocationInfo.locationLatLng)
                    Toast.makeText(this, curLoc?.toString(), Toast.LENGTH_SHORT).show()
                }
        }

    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private val viewModel by viewModel<MainViewModel>()


    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun observeData() = with(binding) {
        viewModel.locationData.observe(this@MainActivity) {
            when (it) {
                is MainState.Uninitialized -> {
                    getMyLocation()
                }

                is MainState.Loading -> {

                }

                is MainState.Success -> {
                    locationLoading.isGone = true
                    locationTitleTextView.text = it.mapSearchInfoEntity.fullAddress
                    viewModel.setDestinationLocation(it.mapSearchInfoEntity.locationLatLng)
                }

                is MainState.Error -> {
                    locationTitleTextView.text = getString(it.errorMessage)
                }
            }
        }
    }

    override fun initViews() = with(binding) {
        val url = "http://54.180.202.117/search.php"
        // 22.01.19 BottomNavigationView의 동작을 Controller를 이용하여 설정
        // by 정남진
        bottomNav.setupWithNavController(navController)



        wView.settings.apply {

            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)

        }
        wView.apply {
            webViewClient = client
            addJavascriptInterface(AndroidBridge(), "TestApp")
            // webChromeClient = chromeClient
            loadUrl(url)
        }


        //wView.loadUrl("https://www.naver.com")

//        locationTitleTextView.setOnClickListener {
//            //  Sliding()
//        }
        locationTitleTextView.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        this@MainActivity, mapInfo
                    )
                )
            }
          //  Toast.makeText(this@MainActivity, "ㄱㄷ", Toast.LENGTH_SHORT).show()
        }

    }

//        locationTitleTextView.setOnClickListener {
//            viewModel.getMapSearchInfo()?.let { mapInfo ->
//                changeLocationLauncher.launch(
//                    MyLocationActivity.newIntent(
//                        requireContext(), mapInfo
//                    )
//                )
//            }
//        }

    override fun onBackPressed() {


        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "종료하서려면 뒤로가기를 한번더 눌러주세요", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }
//        locationTitleTextView.setOnClickListener {
//            viewModel.getMapSearchInfo()?.let { mapInfo ->
//                changeLocationLauncher.launch(
//                    MyLocationActivity.newIntent(
//                        requireContext(), mapInfo
//                    )
//                )
//            }
//        }


    private fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

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

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnable) {
            permissionLauncher.launch(locationPermissions)
        }
    }

    @Suppress("MissingPermission")
    private fun setLocationListener() {
        val minTime: Long = 1500
        val minDistance = 100f

        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }

        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )

            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            curLoc = location
            viewModel.setCurrentLocation(curLoc)
//            binding.locationTitleTextView.text = "${location.latitude}, ${location.longitude}"
            viewModel.getReverseGeoInformation(
                LocationLatLngEntity(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
            val lat = curLoc.latitude
            val lon = curLoc.longitude

            var save_form =
                "{\"LATITUDE\":\"$lat\",\"LONGITUDE\":\"$lon\"}"

            PreferenceManager.setTempUserString(application, "LOCATION", save_form)
            PreferenceManager.setUserString(
                application,
                "LOCATION",
                save_form,
                "locationData"
            )

            removeLocationListener()
        }

        @SuppressLint("MissingPermission")
        private fun removeLocationListener() {
            if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
                locationManager.removeUpdates(myLocationListener)
            }
        }
    }

    private fun Sliding() {
        val slidePanel = binding.mainFrame

        val state = slidePanel.panelState
        if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            slidePanel.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
        } else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidePanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }

    private inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(arg1: String?, arg2: String?, arg3: String?) {
            handler.post {
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(
                            MapLocationSettingActivity.MY_LOCATION_KEY,
                            String.format("(%s) %s %s", arg1, arg2, arg3),
                        )
                    },
                )
                finish()
            }
        }
    }

//    private val chromeClient = object : WebChromeClient() {
//
//        override fun onCreateWindow(
//            view: WebView?,
//            isDialog: Boolean,
//            isUserGesture: Boolean,
//            resultMsg: Message?
//        ): Boolean {
//
//            val newWebView = WebView(applicationContext).apply {
//                settings.javaScriptEnabled = true
//            }
//            setContentView(newWebView)
//
//            newWebView.webChromeClient = object : WebChromeClient() {
//                override fun onJsAlert(
//                    view: WebView,
//                    url: String,
//                    message: String,
//                    result: JsResult
//                ): Boolean {
//                    super.onJsAlert(view, url, message, result)
//
//                    // 선택한 주소 출력
//                    Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
//
//                    return true
//                }
//            }
//            (resultMsg!!.obj as WebView.WebViewTransport).webView = newWebView
//            resultMsg.sendToTarget()
//
//            return true
//        }
//    }
}

