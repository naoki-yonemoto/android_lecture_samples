package com.example.workshopsample1.repository

import com.example.workshopsample1.api.WeatherApiService
import com.example.workshopsample1.data.WeatherResponse
import com.example.workshopsample1.utils.ApiResultOf
import com.example.workshopsample1.utils.ErrorType
import com.example.workshopsample1.utils.safeApiCall
import retrofit2.Response
import javax.inject.Inject

class WeatherApiRepository
@Inject constructor(
	private val apiService : WeatherApiService
) {
	
	suspend fun getWeatherInformation(cityCode: String) : ApiResultOf<WeatherResponse> {
		return safeApiCall(apiService.getWeatherInformation(cityCode))
	}
	
}

