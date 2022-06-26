package com.example.YUmarket.screen.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.repository.map.MapRepository
import com.example.YUmarket.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MapViewModel : BaseViewModel()