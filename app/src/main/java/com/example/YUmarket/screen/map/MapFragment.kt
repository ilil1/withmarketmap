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
import com.example.YUmarket.model.map.MapMarketModel
import com.example.YUmarket.screen.MainState
import com.example.YUmarket.screen.MainViewModel
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.screen.map.MapProductInfo.MapProductInfoActivity
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.ModelRecyclerAdapter
import com.example.YUmarket.widget.adapter.listener.map.MapItemListAdapterListener
import com.example.YUmarket.widget.adapter.viewholder.ModelViewHolder
import com.example.YUmarket.widget.adapter.viewholder.map.MapViewPagerViewHolder
import com.google.android.gms.maps.model.Circle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

// https://github.com/foreknowledge/my-places
// https://navermaps.github.io/android-map-sdk/guide-ko/2-1.html

class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {
    //private val viewModel by viewModel<MapViewModel>()
    private val viewModel by sharedViewModel<MainViewModel>()
    private val resourcesProvider by inject<ResourcesProvider>()

    private val viewPagerAdapter by lazy {
        object : ModelRecyclerAdapter<MapItemModel, MainViewModel>(
            listOf(), viewModel, resourcesProvider,
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
        const val DISTANCE = 300

        fun newInstance() = MapFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

    }

    private lateinit var map: NaverMap
    private lateinit var layout: View

    private var filterCategoryOptions = mutableListOf<CheckBox>()
    private var filterCategoryChecked = mutableListOf<Boolean>()

    //private var map: NaverMap? = null

    /**
     * 지도에서 사용할 목적지 마커
     */


    private lateinit var locationSource1: FusedLocationSource
    private lateinit var geocoder: Geocoder
    private var infoWindow: InfoWindow? = null

    private lateinit var locationManager: LocationManager
    private val locationListener: LocationListener by lazy {
        LocationListener { location ->
            viewModel.curLocation = location
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

    private var isFragmentInitialized: Boolean = false
    private var isListenerAdded : Boolean = false

    /*
        부부식당 : 35.84276976076099, 128.75035319046196
        오빠야찜닭 : 35.8431743483104, 128.74862612604866
        커피플레이스 영남대점 : 35.84115830558572, 128.7540212700628

        서버에서 식당 정보를 받아와서 배열에 담고, repeat 안에서 배열을 돌면서 마커에 이름과 좌표를 넣는다.
    */


    override fun getViewBinding(): FragmentMapBinding =
        FragmentMapBinding.inflate(layoutInflater)

    override fun observeData() = with(viewModel) {
        data.observe(viewLifecycleOwner) {
            when (it) {
                is MapState.Uninitialized -> {}

                is MapState.Loading -> {
                    // TODO: 2022.04.10 handle loading
                }

                // 마커 정보를 다 가져오면 지도에 출력
                is MapState.Success -> {
                    
                }//onSuccess(it)

                is MapState.Error -> {
                    // TODO: 2022.04.10 handle error
                }
            }
        }

        // MainViewModel에서 value 수정할때마다 호출
        viewModel.locationData.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.Uninitialized -> {

                }

                is MainState.Loading -> {

                }

                is MainState.Success -> {
                    //val destLoc = viewModel.getDestinationLocation()
                    //updateLocationOverlay(destLoc)
                    updateLocation(it.mapSearchInfoEntity.locationLatLng)
                    removeAllMarkers()
                }

                is MainState.Error -> {

                }
            }
        }

        viewModel.shopData.observe(viewLifecycleOwner) {
            when(it) {
                is ShopApiState.Uninitialized -> {
                    viewModel.getApiShopList()
                }

                is ShopApiState.Loading -> {
                }

                is ShopApiState.Success -> {
                }

                is ShopApiState.Error -> {

                }
            }
        }
    }

    // TODO 지우기
    /*
    private fun onSuccess(state: MapState.Success) {

        if(!viewModel.getMarkers().isNullOrEmpty())
            removeAllMarkers()

        //val markets = state.markets

        //viewModel.setMarkers(markets.mapIndexed(::createMarketMarkerOnMap))

        for (marker in viewModel.getMarkers()!!) {

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
                // binding.viewPager2.adapter = viewPagerAdapter
                binding.viewPager2.visibility = View.VISIBLE
                binding.fbtnCloseViewPager.visibility = View.VISIBLE
                true
            }
        }
    }
    */

    private fun updateLocationOverlay(destLoc:LocationLatLngEntity){
        map.locationOverlay.position = LatLng(destLoc.latitude, destLoc.longitude)
        map.locationOverlay.isVisible = true
        map.locationOverlay.circleRadius =
            (DISTANCE / map.projection.metersPerPixel).toInt()
    }

    private fun setMarkerListener(markets: List<ShopInfoEntity>) {
        for (marker in viewModel.getMarkers()!!) {

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

    private fun initViewPager() = with(binding) {
        // TODO 식당 정보 삽입
        // 뷰페이저 어댑터에는 오로지 MapItemModel 배열만
        viewPager2.apply {
            adapter = viewPagerAdapter
            setPageTransformer(MapViewPagerViewHolder.ZoomOutTransformer)
        }
    }

    /**
     * 카테고리에 맞는 아이콘과 색깔을 마커에 적용
     * @param marker 적용할 마커
     * @param category 마켓의 카테고리
     */
    private fun setMarkerIconAndColor(marker: Marker, category: Int) = with(marker) {
        // TODO: 2022.04.10 현재는 마켓의 id로 구분, 나중에 카테고리로 구분할 것
        when (category) {
            0 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_m)
                iconTintColor = Color.parseColor("#46F5FF")
            }
            1 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_r)
                iconTintColor = Color.parseColor("#FFCB41")
            }
            2 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_s)
                iconTintColor = Color.parseColor("#886AFF")
            }
            3 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_e)
                iconTintColor = Color.parseColor("#04B404")
            }
            4 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_f)
                iconTintColor = Color.parseColor("#8A0886")
            }
            5 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_f)
                iconTintColor = Color.parseColor("#0B2F3A")
            }
        }
    }

    override fun initState() {
        // TODO 왜 있는지 모름
        if (!isFragmentInitialized) {
            locationSource1 = FusedLocationSource(this@MapFragment, LOCATION_PERMISSION_REQUEST_CODE)
            //isFragmentInitialized = true
        }

        super.initState()
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


        //with(dialogBinding) {
/*
            filterCategoryOptions.addAll(
                arrayOf(
                    foodBeverage, service, fashionAccessories,
                    supermarket, fashionClothes, etc
                )
            )

 */

        // 체크박스 누를때마다 돌면서 전부 true인지 확인하고, 전부 true이면 all의 체크 true
        //setFilterButtonsListener()

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

            //chkVisit.isChecked = filterCategoryOptions[7]
            //switchShowOnSale.isChecked = filterCategoryOptions[8]

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
                Toast.makeText(context, "적어도 하나 이상 카테고리를 선택하도록 하자", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0 until filterCategoryOptions.size)
                filterCategoryChecked[i] = filterCategoryOptions[i].isChecked

            updateMarker()

            dialog.dismiss()
            (layout.parent as ViewGroup).removeView(layout)
        }

        layout.findViewById<Button>(R.id.btn_filter_reset).setOnClickListener {
            // 어떤 하나의 체크박스만 체크하고 적용 -> 초기화 터치 -> x -> 필터

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

//                searchKeywords.clear()
        }

        builder.setView(layout)
        builder.create()
        //builder.setView(root).create()
        //}
    }

    private fun getCategoryNum(category: String): Int =
        when(category) {
            "FOOD_BEVERAGE" -> 0
            "SERVICE" -> 1
            "ACCESSORY" -> 2
            "MART" -> 3
            "FASHION" -> 4
            else -> 5
        }


    private fun setFilterButtonsListener() = with(dialogBinding) {
        for (checkBox in filterCategoryOptions) {
            filterCategoryChecked.add(true)

            checkBox.setOnClickListener { // checkOnListener안한 이유는 직접 터치하지 않고 체크박스의 체크를 설정할때 불필요하게 호출됨
                for (_checkBox in filterCategoryOptions)
                    if (!_checkBox.isChecked) {
                        all.isChecked = false // 이떄 all 리스너 동작 -> all은 클릭 리스너로 바꿈
                        return@setOnClickListener
                    }

                all.isChecked = true
            }
        }
    }

    private fun removeAllMarkers() {
        viewModel.getMarkers()!!.forEach { marker ->
            marker.map = null
        }
        binding.viewPager2.visibility = View.GONE
        binding.fbtnCloseViewPager.visibility = View.GONE
        infoWindow?.close()
        binding.btnCloseMarkers.visibility = View.GONE
    }

    private fun calDist(lat1:Double, lon1:Double, lat2:Double, lon2:Double) : Long{
        val EARTH_R = 6371000.0
        val rad = Math.PI / 180
        val radLat1 = rad * lat1
        val radLat2 = rad * lat2
        val radDist = rad * (lon1 - lon2)

        var distance = Math.sin(radLat1) * Math.sin(radLat2)
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist)
        val ret = EARTH_R * Math.acos(distance)

        return Math.round(ret)
    }

    override fun initViews() = with(binding) {
        initMap()
        initDialog()
        initViewPager()
        var m = viewModel.getMap()
        //m?.cameraPosition =
        //    CameraPosition(LatLng(viewModel.destLocation.latitude, viewModel.destLocation.longitude), 15.0)
/*
        mapView.getMapAsync(this@MapFragment)
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.mapFragment) as com.naver.maps.map.MapFragment?
            ?: com.naver.maps.map.MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.mapFragment, it).commit()
                Toast.makeText(this@MainActivity, "홈", Toast.LENGTH_LONG).show()
                //fm.beginTransaction().hide(it).commit()
            }


 */
/*
        var temp = arrayListOf<Marker>()
        var i = 0
        val markets = viewModel.getShopEntityList()

        repeat(markets.size) {
            if (filterCategoryChecked[markets[i].id.toInt()]) {
                temp += Marker().apply {
                    position = LatLng(
                        markets[i].location.latitude,
                        markets[i].location.longitude
                    )
                    icon = MarkerIcons.BLACK
                    tag = markets[i].name
                    zIndex = i
                }
            }
            i++
        }

        viewModel.setMarkers(temp)

        //searchAround()

        setMarkerListener(markets)

 */

        mapView.getMapAsync(this@MapFragment)


        binding.btnCloseMarkers.setOnClickListener {
            removeAllMarkers()
        }

        btnCurLocation.setOnClickListener {
            try {
                viewModel.getMap()?.cameraPosition =
                    CameraPosition(
                        LatLng(
                            viewModel.curLocation.latitude,
                            viewModel.curLocation.longitude
                        ), 15.0
                    )
            } catch (ex: Exception) {
                Toast.makeText(context, "초기화 중", Toast.LENGTH_SHORT).show()
            }
        }

        btnDestLocation.setOnClickListener {
            try {
                viewModel.getMap()?.cameraPosition =
                    CameraPosition(
                        LatLng(
                            viewModel.destLocation.latitude,
                            viewModel.destLocation.longitude
                        ), 15.0
                    )
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
            // int nRadius : 검색 반경값. 0~33까지 설정 가능. 1은 300m를 나타내며 33의 경우는 9900m를 의미



/*
            var searchKeyword = StringBuilder()

            // 버튼 누를때마다 체크박스 확인해서 check된 것만 string에 추가하고 마지막에 ; 추가하기

            if (chkFoodBeverage.isChecked) {
                searchKeyword.append(resources.getString(R.string.POI_food_beverage))
            }

           오버레이 객체는 아무 스레드에서나 생성할 수 있습니다.
           그러나 오버레이의 속성은 스레드 안전성이 보장되지 않으므로 여러 스레드에서 동시에 접근해서는 안됩니다.
           특히 지도에 추가된 오버레이의 속성은 메인 스레드에서만 접근해야 하며, 그렇지 않으면 CalledFromWrongThreadException이 발생합니다.
           단, 오버레이가 지도에 추가되지 않았다면 다른 스레드에서 오버레이의 속성에 접근해도 예외가 발생하지 않습니다.

           따라서 대량의 오버레이를 다룰 경우 객체를 생성하고 초기 옵션을 지정하는 작업은 백그라운드 스레드에서 수행하고
            지도에 추가하는 작업만을 메인 스레드에서 수행하면 메인 스레드를 효율적으로 사용할 수 있습니다.
            다음은 1000개의 마커를 백그라운드 스레드에서 생성하고 속성을 지정한 후 메인 스레드에서 지도에 추가하는 예제입니다.
        */

            updateMarker()
        }
    }

    private fun updateMarker(){
        deleteMarkers()

        // 가게 돌면서 가게 id에 해당하는게 true라면
        var temp = arrayListOf<Marker>()
        var i = 0
        var markets : List<ShopInfoEntity> = mutableListOf()

        viewModel.shopData.observe(viewLifecycleOwner) {
            when(it) {
                is ShopApiState.Uninitialized -> {
                }
                is ShopApiState.Loading -> {
                }
                is ShopApiState.Success -> {
                    markets = viewModel.getShopEntityList()!!
                }
                is ShopApiState.Error -> {

                }
            }
        }


        val destLoc = viewModel.getDestinationLocation()
        updateLocationOverlay(destLoc)

        if(!isListenerAdded) {
            map.addOnCameraChangeListener { i, b ->
                val destLoc = viewModel.getDestinationLocation()
                updateLocationOverlay(destLoc)
            }
            isListenerAdded = true
        }


/*
        val circle = CircleOverlay()
        circle.center = LatLng(destLoc.latitude, destLoc.longitude)
        circle.radius = DISTANCE.toDouble()

        circle.outlineWidth = 10
        circle.outlineColor = Color.parseColor("#AC97FE")
        circle.zIndex = 100

        circle.map = map

 */



        markets?.let {
            repeat(markets.size) {

                val dist = calDist(destLoc.latitude, destLoc.longitude, markets[i].latitude, markets[i].longitude)

                if (filterCategoryChecked[getCategoryNum(markets[i].category)] && dist < DISTANCE ) {
                    temp += Marker().apply {
                        position = LatLng(
                            markets[i].latitude,
                            markets[i].longitude
                        )
                        icon = MarkerIcons.BLACK
                        tag = markets[i].shop_name
                        zIndex = i
                    }

                    Log.d(TAG, "initViews: " + markets[i].shop_name + " : " + calDist(destLoc.latitude, destLoc.longitude, markets[i].latitude, markets[i].longitude))
                }
                i++
            }

            viewModel.setMarkers(temp)

            searchAround()

            setMarkerListener(markets)

            if(temp.size > 0)
                binding.btnCloseMarkers.visibility = View.VISIBLE
        }
    }

    private fun searchAround() {
        Log.d("TAG", "searchAround: ")
        // TODO 지도에 있는 마커 다 없애기
        deleteMarkers()

        binding.viewPager2.visibility = View.GONE
        binding.fbtnCloseViewPager.visibility = View.GONE

        showMarkersOnMap()
    }

    /**
     * 네이버 지도상 마커를 모두 없애는 method
     */
    private fun deleteMarkers() {
        for (marker in viewModel.getMarkers()!!) {
            marker.map = null
        }
    }

    /**
     * 네이버 지도상에 마커를 표시
     */
    private fun showMarkersOnMap() {
        var i = 0
        for (marker in viewModel.getMarkers()!!) {
            marker.map = viewModel.getMap()

            setMarkerIconAndColor(marker, getCategoryNum(viewModel.getShopEntityList()?.get(marker.zIndex)!!.category))
        }
    }

    private fun createMarketMarkerOnMap(zIndex: Int, market: MapMarketModel) = Marker(
        market.location
    ).apply {
        icon = MarkerIcons.BLACK
        tag = market.name
        this.zIndex = zIndex
        map = viewModel.getMap()
    }

    @SuppressLint("MissingPermission")
    private fun initMap() = with(binding) {

        // viewModel.getMap()?.cameraPosition =
        //     CameraPosition(LatLng(viewModel.destLocation.latitude, viewModel.destLocation.longitude), 15.0)

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

        Toast.makeText(context, "맵 초기화 완료", Toast.LENGTH_LONG).show()

        this.map = map.apply {
            locationSource = locationSource1
            locationTrackingMode = LocationTrackingMode.NoFollow
            uiSettings.isLocationButtonEnabled = true
            uiSettings.isScaleBarEnabled = true
            uiSettings.isCompassEnabled = true
        }
        viewModel.setMap(this.map)

//        observeData()

        /* TODO
            geocoder.getFromLocationName()
         */

        try {
            viewModel.updateLocation(
                LocationLatLngEntity(
                    viewModel.getDestinationLocation().latitude,
                    viewModel.getDestinationLocation().longitude
                )
            )
            Toast.makeText(
                context, viewModel.getCurrentLocation().latitude.toString() +
                        viewModel.getCurrentLocation().longitude, Toast.LENGTH_SHORT
            ).show()

            //map.cameraPosition =
            //    CameraPosition(LatLng(viewModel.destLocation.latitude, viewModel.destLocation.longitude), 15.0)

        } catch (ex: Exception) {
            Toast.makeText(context, "위치 초기화 중", Toast.LENGTH_SHORT).show()
        }

        //searchAround()
    }
}