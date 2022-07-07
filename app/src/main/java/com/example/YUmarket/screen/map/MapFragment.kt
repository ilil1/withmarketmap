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

// 코드 참고 : https://github.com/foreknowledge/my-places
// 공식 문서 : https://navermaps.github.io/android-map-sdk/guide-ko/2-1.html

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
                //super.submitList(market.) // 상점 아이템 넣어줌
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
    private lateinit var layout: View

    private var filterCategoryOptions = mutableListOf<CheckBox>()
    private var filterCategoryChecked = mutableListOf<Boolean>()

    /**
     * 지도에서 사용할 목적지 마커
     */

    private lateinit var geocoder: Geocoder
    private var infoWindow: InfoWindow? = null

    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationManager: LocationManager

    private val locationListener: LocationListener by lazy {
        LocationListener { location ->
            activityViewModel.curLocation = location
        }
    }

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

    /*
        서버에서 식당 정보를 받아와서 배열에 담고, repeat 안에서 배열을 돌면서 마커에 이름과 좌표를 넣는다.
    */


    override fun getViewBinding(): FragmentMapBinding =
        FragmentMapBinding.inflate(layoutInflater)

    override fun observeData() {

        mapViewModel.data.observe(viewLifecycleOwner) {
            when (it) {
                is MapState.Uninitialized -> {
                    mapViewModel.getApiShopList()
                }
                is MapState.Loading -> {}
                // 마커 정보를 다 가져오면 지도에 출력
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
//                // idx로는 setOnClickListener에서 마커의 index를 못찾아서 고유값인 zIndex로 대체
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
//                // 여기서 오픈한 말풍선은 fbtnViewPager2를 클릭하면 제거
//                viewPagerAdapter.registerStore(markets[marker.zIndex])
//                // binding.viewPager2.adapter = viewPagerAdapter
//                binding.viewPager2.visibility = View.VISIBLE
//                binding.fbtnCloseViewPager.visibility = View.VISIBLE
//                true
//            }
//        }
//    }

    private fun initViewPager() = with(binding) {
        // TODO 식당 정보 삽입
        // 뷰페이저 어댑터에는 오로지 MapItemModel 배열만
        viewPager2.apply {
            adapter = viewPagerAdapter
            setPageTransformer(MapViewPagerViewHolder.ZoomOutTransformer)
        }
    }

    private lateinit var chkAll: CheckBox

    private fun initDialog() {

        builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)

        val displayRectangle = Rect()
        val window = requireActivity().window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layout = inflater.inflate(R.layout.dialog_filter, null)
        layout.minimumWidth = ((displayRectangle.width() * 0.9f).toInt())
        layout.minimumHeight = ((displayRectangle.height() * 0.9f).toInt())

        chkAll = layout.findViewById(R.id.all)
        filterCategoryOptions.add(layout.findViewById(R.id.food_beverage))
        filterCategoryOptions.add(layout.findViewById(R.id.service))
        filterCategoryOptions.add(layout.findViewById(R.id.fashion_accessories))
        filterCategoryOptions.add(layout.findViewById(R.id.supermarket))
        filterCategoryOptions.add(layout.findViewById(R.id.fashion_clothes))
        filterCategoryOptions.add(layout.findViewById(R.id.etc))

        for (checkBox in filterCategoryOptions) {
            filterCategoryChecked.add(true)

            checkBox.setOnClickListener { // checkOnListener안한 이유는 직접 터치하지 않고 체크박스의 체크를 설정할때 불필요하게 호출됨
                for (_checkBox in filterCategoryOptions)
                    if (!_checkBox.isChecked) {
                        chkAll.isChecked = false // 이떄 all 리스너 동작 -> all은 클릭 리스너로 바꿈
                        return@setOnClickListener
                    }

                chkAll.isChecked = true
            }
        }

        chkAll.setOnClickListener {
            for (item in filterCategoryOptions)
                item.isChecked = chkAll.isChecked // 아이템마다 리스너 전부 동작
        }

        layout.findViewById<ImageButton>(R.id.btn_close_filter).setOnClickListener {
            // 필터 열때 저장했던 체크정보 다시 UI에 적용
            var check = true
            for (i in 0 until filterCategoryOptions.size) {
                filterCategoryOptions[i].isChecked = filterCategoryChecked[i]
                if (!filterCategoryOptions[i].isChecked)
                    check = false
            }
            chkAll.isChecked = check

            dialog.dismiss()
            (layout.parent as ViewGroup).removeView(layout)
        }

        layout.findViewById<Button>(R.id.btn_filter_apply).setOnClickListener {

            var noChk = true
            for (item in filterCategoryOptions)
                if (item.isChecked) {
                    noChk = false
                    break
                }

            if (noChk) {
                Toast.makeText(context, "적어도 하나 이상 카테고리를 선택해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0 until filterCategoryOptions.size)
                filterCategoryChecked[i] = filterCategoryOptions[i].isChecked

            updateMarker()

            dialog.dismiss()
            (layout.parent as ViewGroup).removeView(layout)
        }

        layout.findViewById<Button>(R.id.btn_filter_reset).setOnClickListener {
            for (i in 0 until filterCategoryOptions.size) {
                filterCategoryOptions[i].isChecked = true
            }
            // checkonlistener를 안해줘서 각각의 체크박스들이 반응을 안해서 여기서 다시 해줌
            var check = true

            for (item in filterCategoryOptions)
                if (!item.isChecked) {
                    check = false // 이떄 all 리스너 동작 -> all은 클릭 리스너로 바꿈
                }
            if (check) chkAll.isChecked = true
        }

        builder.setView(layout)
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
                Toast.makeText(context, "초기화 중", Toast.LENGTH_SHORT).show()
            }
        }

        btnDestLocation.setOnClickListener {
            try {
                mapViewModel.getMap()?.cameraPosition = CameraPosition(
                    LatLng(activityViewModel.getDestinationLocation().latitude,
                            activityViewModel.getDestinationLocation().longitude),
                    15.0)
            } catch (ex: Exception) {
                Toast.makeText(context, "초기화 중", Toast.LENGTH_SHORT).show()
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
           int nRadius : 검색 반경값. 0~33까지 설정 가능. 1은 300m를 나타내며 33의 경우는 9900m를 의미

           오버레이 객체는 아무 스레드에서나 생성할 수 있습니다.
           그러나 오버레이의 속성은 스레드 안전성이 보장되지 않으므로 여러 스레드에서 동시에 접근해서는 안됩니다.
           특히 지도에 추가된 오버레이의 속성은 메인 스레드에서만 접근해야 하며, 그렇지 않으면 CalledFromWrongThreadException이 발생합니다.
           단, 오버레이가 지도에 추가되지 않았다면 다른 스레드에서 오버레이의 속성에 접근해도 예외가 발생하지 않습니다.

           따라서 대량의 오버레이를 다룰 경우 객체를 생성하고 초기 옵션을 지정하는 작업은 백그라운드 스레드에서 수행하고
           지도에 추가하는 작업만을 메인 스레드에서 수행하면 메인 스레드를 효율적으로 사용할 수 있습니다.
           다음은 1000개의 마커를 백그라운드 스레드에서 생성하고 속성을 지정한 후 메인 스레드에서 지도에 추가하는 예제입니다.
        */
        }
    }

    private fun updateLocationOverlay(destLoc:LocationLatLngEntity) {
        naverMap.locationOverlay.position = LatLng(destLoc.latitude, destLoc.longitude)
        naverMap.locationOverlay.isVisible = true
        naverMap.locationOverlay.circleRadius =
            (DISTANCE / naverMap.projection.metersPerPixel).toInt()
    }

    /**
     * 네이버 지도상 마커를 모두 없애는 method
     */
    private fun deleteMarkers() {
        for (marker in mapViewModel.getMarkers()!!) {
            marker.map = null
        }
    }

    /**
     * 네이버 지도상에 마커를 표시
     */
    private fun showMarkersOnMap() {
        for (marker in mapViewModel.getMarkers()!!) {
            marker.map = mapViewModel.getMap()
            mapViewModel.setMarkerIconAndColor(marker, mapViewModel.getCategoryNum(mapViewModel.getShopEntityList()?.get(marker.zIndex)!!.category))
        }
    }

    private fun searchAround() {
        deleteMarkers()
        binding.viewPager2.visibility = View.GONE
        binding.fbtnCloseViewPager.visibility = View.GONE
        showMarkersOnMap()
    }

    private fun setMarkerListener(markets: List<ShopInfoEntity>) {
        for (marker in mapViewModel.getMarkers()!!) {
            marker.setOnClickListener {
                // idx로는 setOnClickListener에서 마커의 index를 못찾아서 고유값인 zIndex로 대체

                this@MapFragment.infoWindow?.close()
                this@MapFragment.infoWindow = InfoWindow()
                this@MapFragment.infoWindow?.adapter =
                    object : InfoWindow.DefaultTextAdapter(requireContext()) {
                        override fun getText(infoWindow: InfoWindow): CharSequence {
                            return infoWindow.marker?.tag as CharSequence
                        }
                    }
                this@MapFragment.infoWindow?.open(marker)

                // 여기서 오픈한 말풍선은 fbtnViewPager2를 클릭하면 제거
                viewPagerAdapter.registerStore(markets[marker.zIndex])
                //binding.viewPager2.adapter = viewPagerAdapter
                binding.viewPager2.visibility = View.VISIBLE
                binding.fbtnCloseViewPager.visibility = View.VISIBLE
                true
            }
        }
    }

    private fun updateMarker() {

        deleteMarkers()

        // 가게 돌면서 가게 id에 해당하는게 true라면
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

            searchAround()
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
            Toast.makeText(context, "destLocation 가져오는 중", Toast.LENGTH_SHORT).show()
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
            this.locationSource = this@MapFragment.locationSource //현재 위치값을 넘긴다
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
            Toast.makeText(context, "위치 초기화 중", Toast.LENGTH_SHORT).show()
        }
    }
}