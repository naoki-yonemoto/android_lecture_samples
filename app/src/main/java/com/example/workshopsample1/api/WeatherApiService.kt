package com.example.workshopsample1.api

import com.example.workshopsample1.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Interface
 */
interface WeatherApiService {
	
	
	//coroutineでコールするようなメソッドはsuspend funにする
	//suspend funは同じsuspend funかcoroutine内でしかコールできない
	/**
	 * 天気情報の取得
	 */
	@GET("api/forecast/")
	suspend fun getWeatherInformation(
		@Query("city") city : String
	) : Response<WeatherResponse>
	
	
}