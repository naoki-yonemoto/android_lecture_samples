package com.example.workshopsample1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workshopsample1.data.CityCode
import com.example.workshopsample1.data.WeatherResponse
import com.example.workshopsample1.repository.WeatherApiRepository
import com.example.workshopsample1.utils.ApiResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherFragmentViewModel
@Inject constructor(
	private val repository : WeatherApiRepository
) : ViewModel() {
	
	private val _loading = MutableStateFlow(false)
	val loading = _loading.asStateFlow()
	
	//(Shared)Flowを使ってFragmentから値を監視させる(observerパターン)
	//値の変更が検知されたらFragmentで処理をできるようになる
	//値の変更が激しい、値を変更する処理が色々なところからコールされる場合はこちらのほうが使いやすい（処理の集約ができる）
	private val _weatherInformation = MutableSharedFlow<ApiResultOf<WeatherResponse>>()
	val weatherInformation : SharedFlow<ApiResultOf<WeatherResponse>> = _weatherInformation.asSharedFlow()
	
	var weatherPoint: CityCode = CityCode.Tokyo
	
	
	fun setLoading(value : Boolean){
		viewModelScope.launch {
			_loading.emit(value)
		}
	}
	
	//天気情報の取得
	suspend fun getWeatherInformation(cityCode: CityCode) {
		_loading.emit(true)
		weatherPoint = cityCode
		_weatherInformation.emit(repository.getWeatherInformation(cityCode.getCode()))
	}
	
	//returnでそのまま返す場合
	suspend fun getWeatherInformation2(cityCode: CityCode) : ApiResultOf<WeatherResponse> {
		return repository.getWeatherInformation(cityCode.getCode())
	}
	
	//viewModelの拡張ライブラリがCoroutineScopeを持っていくれているのでViewModelでもコール可能
	fun getWeatherInformation3(cityCode: String) {
		viewModelScope.launch(Dispatchers.IO) {
			_weatherInformation.emit(repository.getWeatherInformation(cityCode))
		}
	}
}