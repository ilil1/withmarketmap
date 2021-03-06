package com.example.YUmarket.screen.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.entity.shop.ShopInfoEntity
import com.example.YUmarket.databinding.DialogFilterBinding
import com.example.YUmarket.databinding.FragmentMapBinding
import com.example.YUmarket.model.map.MapItemModel
import com.example.YUmarket.screen.MainState
import com.example.YUmarket.screen.MainViewModel
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.screen.map.MapProductInfo.MapProductInfoActivity
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.ModelRecyclerAdapter
import com.example.YUmarket.widget.adapter.listener.map.MapItemListAdapterListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder
import com.example.YUmarket.widget.adapter.viewholder.map.MapViewPagerViewHolder
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

// ?????? ?????? : https://github.com/foreknowledge/my-places
// ?????? ?????? : https://navermaps.github.io/android-map-sdk/guide-ko/2-1.html

class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {

    private val mapViewModel by viewModel<MapViewModel>()
    private val activityViewModel by sharedViewModel<MainViewModel>()
    private val resourcesProvider by inject<ResourcesProvider>()

    private val viewPagerAdapter by lazy {
        object : ModelRecyclerAdapter<MapItemModel, MainViewModel>(
            listOf(), activityViewModel, resourcesProvider,
            object : MapItemListAdapterListener {
                override fun onClickItem(mapItemModel: MapItemModel) {
                    startActivity(
                        MapProductInfoActivity.newIntent(
                            this@MapFragment.requireContext(),
                            mapItemModel
                        )
                    )
                }
            }
        ) {
            private lateinit var market: ShopInfoEntity

            fun registerStore(market: ShopInfoEntity) {
                this.market = market
                //super.submitList(market.) // ?????? ????????? ?????????
            }

            override fun onBindViewHolder(
                holder: ModelViewHolder<MapItemModel>,
                position: Int
            ) {
                if (holder is MapViewPagerViewHolder) {
                    holder.market = market
                    super.onBindViewHolder(holder, position)
                }
            }
        }
    }

    companion object {
        const val TAG = "MapFragment"
        private const val DISTANCE = 300

        fun newInstance() = MapFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

    }

    private lateinit var naverMap: NaverMap
    private lateinit var geocoder: Geocoder
    private var infoWindow: InfoWindow? = null

    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationManager: LocationManager

    private val locationListener: LocationListener by lazy {
        LocationListener { location ->
            activityViewModel.curLocation = location
        }
    }

    private var filterCategoryOptions = mutableListOf<CheckBox>()
    private var filterCategoryChecked = mutableListOf<Boolean>()
    private lateinit var chkAll: CheckBox
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    private val dialogBinding by lazy {
        val displayRectangle = Rect()
        requireActivity().window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        DialogFilterBinding.inflate(layoutInflater).apply {
            root.minimumHeight = (displayRectangle.width() * 0.9f).toInt()
            root.minimumHeight = (displayRectangle.height() * 0.9f).toInt()
        }
    }

    private var isListenerAdded : Boolean = false

    override fun getViewBinding(): FragmentMapBinding =
        FragmentMapBinding.inflate(layoutInflater)

    override fun observeData() {

        mapViewModel.data.observe(viewLifecycleOwner) {
            when (it) {
                is MapState.Uninitialized -> {
                    mapViewModel.getApiShopList()
                }
                is MapState.Loading -> {}
                // ?????? ????????? ??? ???????????? ????????? ??????
                is MapState.Success -> {}//onSuccess(it)
                is MapState.Error -> {}
            }
        }

        activityViewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.Uninitialized -> {}
                is MainState.Loading -> {}
                is MainState.Success -> {
                    mapViewModel.updateLocation(it.mapSearchInfoEntity.locationLatLng)
                    removeAllMarkers()
                }
                is MainState.Error -> {}
            }
        }
    }

//    private fun onSuccess(state: MapState.Success) {
//
//        if(!viewModel.getMarkers().isNullOrEmpty())
//            removeAllMarkers()
//
//        //val markets = state.markets
//        //viewModel.setMarkers(markets.mapIndexed(::createMarketMarkerOnMap))
//
//        for (marker in viewModel.getMarkers()!!) {
//
//            marker.setOnClickListener {
//                // idx?????? setOnClickListener?????? ????????? index??? ???????????? ???????????? zIndex??? ??????
//                this@MapFragment.infoWindow?.close()
//                this@MapFragment.infoWindow = InfoWindow()
//                this@MapFragment.infoWindow?.adapter =
//                    object : InfoWindow.DefaultTextAdapter(requireContext()) {
//                        override fun getText(infoWindow: InfoWindow): CharSequence {
//                            return infoWindow.marker?.tag as CharSequence
//                        }
//                    }
//                this@MapFragment.infoWindow?.open(marker)
//
//                // ????????? ????????? ???????????? fbtnViewPager2??? ???????????? ??????
//                viewPagerAdapter.registerStore(markets[marker.zIndex])
//                // binding.viewPager2.adapter = viewPagerAdapter
//                binding.viewPager2.visibility = View.VISIBLE
//                binding.fbtnCloseViewPager.visibility = View.VISIBLE
//                true
//            }
//        }
//    }

    private fun initViewPager() = with(binding) {
        // TODO ?????? ?????? ??????
        // ???????????? ??????????????? ????????? MapItemModel ?????????
        viewPager2.apply {
            adapter = viewPagerAdapter
            setPageTransformer(MapViewPagerViewHolder.ZoomOutTransformer)
        }
    }

    private fun initDialog() {

        builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)

        chkAll = dialogBinding.all

        with(dialogBinding) {
            filterCategoryOptions.addAll(
                arrayOf(
                    foodBeverage, service, fashionAccessories,
                    supermarket, fashionClothes, etc
                )
            )
        }

        chkAll.setOnClickListener {
            filterCategoryOptions.forEach { checkBox ->
                checkBox.isChecked = chkAll.isChecked
            }
        }

        filterCategoryOptions.forEach { checkBox ->
            filterCategoryChecked.add(true) // btnclose ??? ??? ture ????????? ?????????
            checkBox.setOnClickListener {
                for (_checkBox in filterCategoryOptions) {
                    if (!_checkBox.isChecked) {
                        chkAll.isChecked = false
                        return@setOnClickListener
                    }
                }
                chkAll.isChecked = true
            }
        }

        dialogBinding.btnCloseFilter.setOnClickListener {

            var check = true
            for (i in 0 until filterCategoryOptions.size) {
                filterCategoryOptions[i].isChecked = filterCategoryChecked[i]
                if (!filterCategoryOptions[i].isChecked)
                    check = false
            }
            chkAll.isChecked = check

            dialog.dismiss()
            if (dialogBinding.root.parent != null) {
                (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
            }
        }

        dialogBinding.btnFilterReset.setOnClickListener {

            filterCategoryOptions.forEach { it.isChecked = true }

            var check = true
            for (item in filterCategoryOptions)
                if (!item.isChecked) {
                    check = false
                }

            if (check) chkAll.isChecked = true
        }

        dialogBinding.btnFilterApply.setOnClickListener {

            var noChk = true
            for (item in filterCategoryOptions) {
                if (item.isChecked) {
                    noChk = false
                    break
                }
            }

            if (noChk) {
                Toast.makeText(requireContext(), "????????? ?????? ?????? ??????????????? ???????????? ?????????.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0 until filterCategoryOptions.size)
                filterCategoryChecked[i] = filterCategoryOptions[i].isChecked

            updateMarker()

            dialog.dismiss()
            if (dialogBinding.root.parent != null) {
                (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
            }
        }

        builder.setView(dialogBinding.root)
        builder.create()
    }

    private fun removeAllMarkers() {
        mapViewModel.getMarkers()!!.forEach { marker ->
            marker.map = null
        }
        binding.viewPager2.visibility = View.GONE
        binding.fbtnCloseViewPager.visibility = View.GONE
        infoWindow?.close()
        binding.btnCloseMarkers.visibility = View.GONE
    }

    override fun initViews() = with(binding) {
        Log.d("initViews", "initViews")

        initMap()
        initDialog()
        initViewPager()

        mapView.getMapAsync(this@MapFragment)

        binding.btnCloseMarkers.setOnClickListener {
            removeAllMarkers()
        }

        btnCurLocation.setOnClickListener {
            try {
                mapViewModel.getMap()?.cameraPosition = CameraPosition(
                        LatLng(activityViewModel.getCurrentLocation().latitude,
                                activityViewModel.getCurrentLocation().longitude),
                    15.0)
            } catch (ex: Exception) {
                Toast.makeText(context, "????????? ???", Toast.LENGTH_SHORT).show()
            }
        }

        btnDestLocation.setOnClickListener {
            try {
                mapViewModel.getMap()?.cameraPosition = CameraPosition(
                    LatLng(activityViewModel.getDestinationLocation().latitude,
                            activityViewModel.getDestinationLocation().longitude),
                    15.0)
            } catch (ex: Exception) {
                Toast.makeText(context, "????????? ???", Toast.LENGTH_SHORT).show()
            }
        }

        btnFilter.setOnClickListener {
            for (item in filterCategoryOptions)
                filterCategoryChecked.add(item.isChecked)

            dialog = builder.show()
        }

        fbtnCloseViewPager.setOnClickListener {
            viewPager2.visibility = View.GONE
            fbtnCloseViewPager.visibility = View.GONE

            infoWindow?.close()
        }

        btnSearchAround.setOnClickListener {

            try {
                updateMarker()
            } catch (ex: Exception) {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            }

        /*
           int nRadius : ?????? ?????????. 0~33?????? ?????? ??????. 1??? 300m??? ???????????? 33??? ????????? 9900m??? ??????

           ???????????? ????????? ?????? ?????????????????? ????????? ??? ????????????.
           ????????? ??????????????? ????????? ????????? ???????????? ???????????? ???????????? ?????? ??????????????? ????????? ??????????????? ????????????.
           ?????? ????????? ????????? ??????????????? ????????? ?????? ?????????????????? ???????????? ??????, ????????? ????????? CalledFromWrongThreadException??? ???????????????.
           ???, ??????????????? ????????? ???????????? ???????????? ?????? ??????????????? ??????????????? ????????? ???????????? ????????? ???????????? ????????????.

           ????????? ????????? ??????????????? ?????? ?????? ????????? ???????????? ?????? ????????? ???????????? ????????? ??????????????? ??????????????? ????????????
           ????????? ???????????? ???????????? ?????? ??????????????? ???????????? ?????? ???????????? ??????????????? ????????? ??? ????????????.
           ????????? 1000?????? ????????? ??????????????? ??????????????? ???????????? ????????? ????????? ??? ?????? ??????????????? ????????? ???????????? ???????????????.
        */
        }
    }

    private fun updateLocationOverlay(destLoc:LocationLatLngEntity) {
        naverMap.locationOverlay.position = LatLng(destLoc.latitude, destLoc.longitude)
        naverMap.locationOverlay.isVisible = true
        naverMap.locationOverlay.circleRadius =
            (DISTANCE / naverMap.projection.metersPerPixel).toInt()
    }

    private fun setMarkerListener(markets: List<ShopInfoEntity>) {
        for (marker in mapViewModel.getMarkers()!!) {
            marker.setOnClickListener {
                // idx?????? setOnClickListener?????? ????????? index??? ???????????? ???????????? zIndex??? ??????

                this@MapFragment.infoWindow?.close()
                this@MapFragment.infoWindow = InfoWindow()
                this@MapFragment.infoWindow?.adapter =
                    object : InfoWindow.DefaultTextAdapter(requireContext()) {
                        override fun getText(infoWindow: InfoWindow): CharSequence {
                            return infoWindow.marker?.tag as CharSequence
                        }
                    }
                this@MapFragment.infoWindow?.open(marker)

                // ????????? ????????? ???????????? fbtnViewPager2??? ???????????? ??????
                viewPagerAdapter.registerStore(markets[marker.zIndex])
                //binding.viewPager2.adapter = viewPagerAdapter
                binding.viewPager2.visibility = View.VISIBLE
                binding.fbtnCloseViewPager.visibility = View.VISIBLE
                true
            }
        }
    }

    private fun updateMarker() {

        mapViewModel.deleteMarkers()

        // ?????? ????????? ?????? id??? ??????????????? true??????
        var markets : List<ShopInfoEntity> = mutableListOf()
        var temp = arrayListOf<Marker>()
        var i = 0

        mapViewModel.data.observe(viewLifecycleOwner) {
            when (it) {
                is MapState.Uninitialized -> {}
                is MapState.Loading -> {}
                is MapState.Success -> {
                    markets = mapViewModel.getShopEntityList()!!
                }
                is MapState.Error -> {}
            }
        }

        val destLoc = activityViewModel.getDestinationLocation()
        updateLocationOverlay(destLoc)

        if(!isListenerAdded) {
            naverMap.addOnCameraChangeListener { i, b ->
                val destLoc = activityViewModel.getDestinationLocation()
                updateLocationOverlay(destLoc)
            }
            isListenerAdded = true
        }

        markets?.let {

            repeat(markets.size) {

                val dist = mapViewModel.calDist(
                    destLoc.latitude,
                    destLoc.longitude,
                    markets[i].latitude,
                    markets[i].longitude)

                if (filterCategoryChecked[mapViewModel.getCategoryNum(markets[i].category)] && dist < DISTANCE )
                {
                    temp += Marker().apply {
                        position = LatLng(markets[i].latitude, markets[i].longitude)
                        icon = MarkerIcons.BLACK
                        tag = markets[i].shop_name
                        zIndex = i
                    }
                }
                i++
            }

            mapViewModel.setMarkers(temp)

            binding.viewPager2.visibility = View.GONE
            binding.fbtnCloseViewPager.visibility = View.GONE
            mapViewModel.deleteMarkers()
            mapViewModel.showMarkersOnMap()

            setMarkerListener(markets)

            if(temp.size > 0)
                binding.btnCloseMarkers.visibility = View.VISIBLE
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMap() = with(binding) {

        locationSource = FusedLocationSource(this@MapFragment, LOCATION_PERMISSION_REQUEST_CODE)

        try {
            val destLocation = activityViewModel.getDestinationLocation()
            mapViewModel.setDestinationLocation(destLocation)
        } catch (ex: Exception) {
            Toast.makeText(context, "destLocation ???????????? ???", Toast.LENGTH_SHORT).show()
        }

        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            1f,
            locationListener
        )

        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            1f,
            locationListener
        )
    }

    override fun onMapReady(map: NaverMap) {

        naverMap = map.apply {
            this.locationSource = this@MapFragment.locationSource //?????? ???????????? ?????????
            locationTrackingMode = LocationTrackingMode.NoFollow
            uiSettings.isLocationButtonEnabled = true
            uiSettings.isScaleBarEnabled = true
            uiSettings.isCompassEnabled = true
        }

        mapViewModel.setMap(naverMap)

        /* TODO
            geocoder.getFromLocationName()
         */
        try {
            mapViewModel.firstupdateLocation()
        } catch (ex: Exception) {
            Toast.makeText(context, "?????? ????????? ???", Toast.LENGTH_SHORT).show()
        }
    }
}