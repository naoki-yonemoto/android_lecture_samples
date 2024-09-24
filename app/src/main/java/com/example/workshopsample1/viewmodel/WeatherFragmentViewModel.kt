package com.example.workshopsample1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workshopsample1.data.WeatherResponse
import com.example.workshopsample1.repository.WeatherApiRepository
import com.example.workshopsample1.utils.ApiResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherFragmentViewModel
@Inject constructor(
	private val repository : WeatherApiRepository
) : ViewModel() {
	
	//(Shared)Flowを使ってFragmentから値を監視させる(observerパターン)
	//値の変更が検知されたらFragmentで処理をできるようになる
	//値の変更が激しい、値を変更する処理が色々なところからコールされる場合はこちらのほうが使いやすい（処理の集約ができる）
	private val _weatherInformation = MutableSharedFlow<ApiResultOf<WeatherResponse>>()
	val weatherInformation : SharedFlow<ApiResultOf<WeatherResponse>> = _weatherInformation.asSharedFlow()

	//天気情報の取得
	suspend fun getWeatherInformation(cityCode: String) {
		_weatherInformation.emit(repository.getWeatherInformation(cityCode))
	}
	
	//returnでそのまま返す場合
	suspend fun getWeatherInformation2(cityCode: String) : ApiResultOf<WeatherResponse> {
		return repository.getWeatherInformation(cityCode)
	}
	
	//viewModelの拡張ライブラリがCoroutineScopeを持っていくれているのでViewModelでもコール可能
	fun getWeatherInformation3(cityCode: String) {
		viewModelScope.launch {
			_weatherInformation.emit(repository.getWeatherInformation(cityCode))
		}
	}
}