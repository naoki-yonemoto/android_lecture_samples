package com.example.workshopsample1.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response OverView
 */
@JsonClass(generateAdapter = true)
data class WeatherResponse(
	val publicTimeFormatted : String,
	val title : String,
	val description : WeatherDescription,
	val forecasts: List<WeatherForecasts>,
	val location : WeatherLocation
)


//---------------------------------------------------------//
//予報概要
data class WeatherDescription(
	@Json(name = "bodyText") val descriptionBodyText : String
)

//予報内容
data class WeatherForecasts(
	val date : String,
	val dateLabel : String,
	@Json(name = "telop") val weatherLabel : String,
	@Json(name = "detail") val forecastDetail : WeatherForecastDetail,
	val temperature : WeatherTemperature,
	val chanceOfRain: ChanceOfRain,
	@Json(name = "image") val iconImage: WeatherIconImage
)

//予報の概要
data class WeatherForecastDetail(
	val weather : String?,
	val wind : String?,
	val wave : String?
)

//気温MAX-MIN
data class WeatherTemperature(
	val min : TemperatureCelsius,
	val max : TemperatureCelsius
)

//気温（摂氏）
data class TemperatureCelsius(
	val celsius : String?
)

//降水確率
data class ChanceOfRain(
	//0時から6時
	@Json(name = "T00_06") val lateNight : String,
	@Json(name = "T06_12") val morning : String,
	@Json(name = "T12_18") val afternoon : String,
	@Json(name = "T18_24") val night : String
)

data class WeatherIconImage(
	val title : String,
	val url : String,
	val width : Int,
	val height : Int,
)

//地理情報
data class WeatherLocation(
	val area : String,
	val prefecture : String,
	val district : String,
	val city : String
)