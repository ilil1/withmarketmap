package com.example.YUmarket.screen.map.MapLocationSetting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity
import com.example.YUmarket.databinding.ActivityMapLocationSettingBinding
import com.example.YUmarket.screen.base.BaseActivity
import com.skt.Tmap.TMapGpsManager
import com.skt.Tmap.TMapView
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel


// 핀을 끌어서 옮길때마다 위치 정보 받아서 주소 보여줌
class MapLocationSettingActivity : BaseActivity<ActivityMapLocationSettingBinding>(){
    var isCurAddressNull = true

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private lateinit var cur: Location

    private lateinit var tMapView: TMapView
    private lateinit var tMapGPS: TMapGpsManager
    private val viewModel by viewModel<MapLocationSettingViewModel>()

    override fun getViewBinding() = ActivityMapLocationSettingBinding.inflate(layoutInflater)

    companion object {
        const val CAMERA_ZOOM_LEVEL = 15f
        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MapLocationSettingActivity::class.java).apply {
                putExtra(MY_LOCATION_KEY, mapSearchInfoEntity)
            }
    }

    override fun initViews() {
        initMap()

        binding.btnSetCurLocation.setOnClickListener {
            // 처음 들어와서 바로 설정버튼 누르면 에러

            // 이전 위치와 다른 경우에만 finish(). 같으면 아직 viewModel 갱신이 안된 상태이기 때문에 ㄱㄷ이라고 토스트
            // 먼저 지도 위치를 드래그 해서 이동시키는 즉시 isCurAddressNull = true

            // 로딩 이미지를 출력해서 지도 이동해서 갱신중일때 터치 막기

            if (!isCurAddressNull) {
                val entity = viewModel.getMapSearchInfo()

                val intent = Intent()
                intent.putExtra("result", entity)

                setResult(Activity.RESULT_OK, intent)
                Toast.makeText(this@MapLocationSettingActivity, "설정완료!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(
                    this@MapLocationSettingActivity,
                    "위치를 드래그해서 갱신해주세요!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.fbtnBack.setOnClickListener {
            // 위치 정보 없음 상태에서 누르면 x

            //setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.btnSetCur.setOnClickListener {

            val prefb = this.getSharedPreferences("locationData", MODE_PRIVATE)
            val col_val: Collection<*> = prefb.all.values
            val it_val = col_val.iterator()
            val value = it_val.next() as String
            val jsonObject = JSONObject(value)

            var lat = jsonObject.getString("LATITUDE") as String
            var lon = jsonObject.getString("LONGITUDE") as String


/*
            if(cur == null){
                Toast.makeText(this, "아직 현재위치를 가져오지 못했습니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

 */


            tMapView.setLocationPoint(lon.toDouble(), lat.toDouble())
            tMapView.setCenterPoint(lon.toDouble(), lat.toDouble())

            isCurAddressNull = true

            viewModel.getReverseGeoInformation(
                LocationLatLngEntity(
                    lat.toDouble(),
                    lon.toDouble()
                )
            )
        }
    }

    private fun initMap() = with(binding) {
        tMapView = TMapView(this@MapLocationSettingActivity).apply {
            setSKTMapApiKey("l7xx47edb2787b5040fc8e004c19e85c0053")
            setOnDisableScrollWithZoomLevelListener { _, tMapPoint ->
                isCurAddressNull = true

                viewModel.getReverseGeoInformation(
                    LocationLatLngEntity(
                        tMapPoint.latitude,
                        tMapPoint.longitude
                    )
                )
            }
        }

        tMapGPS = TMapGpsManager(applicationContext);

        // Initial Setting
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10F);
        tMapGPS.setProvider(tMapGPS.provider);
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);

        tMapGPS.OpenGps();

        TMap.addView(tMapView)
        val entity = intent.getParcelableExtra<MapSearchInfoEntity>(MY_LOCATION_KEY)

        // 화면을 이동하면서 화면 중앙에 해당하는 실제 좌표 위치를 tMapPoint에 계속 저장함
        // 뒤로가기를 하면 그냥 뷰모델에 반영없이 뒤로가기 하는거고
        // 이 위치로 주소설정을 하면 뷰모델에 반영

        tvCurAddress.text = entity?.fullAddress ?: "정보없음"

        tMapView.setLocationPoint(entity?.locationLatLng!!.longitude, entity?.locationLatLng.latitude)
        tMapView.setCenterPoint(entity?.locationLatLng!!.longitude, entity?.locationLatLng.latitude)
    }

    override fun observeData() = with(viewModel) {
        mapLocationSettingStateLiveData.observe(this@MapLocationSettingActivity) {
            when (it) {
                is MapLocationSettingState.Uninitialized -> {

                }

                is MapLocationSettingState.Loading -> {

                }

                is MapLocationSettingState.Success -> {
                    binding.tvCurAddress.text = it.mapSearchInfoEntity.fullAddress
                    isCurAddressNull = false
                }

                is MapLocationSettingState.Error -> {

                }
            }
        }
    }

}
