package com.example.workshopsample1.data

sealed interface CityCode {
	
	fun getCode(): String
	
	data object Tokyo : CityCode{
		private const val CODE = "130010"
		override fun getCode(): String = CODE
		
	}
	
	data object Osaka : CityCode{
		private const val CODE = "270000"
		override fun getCode(): String = CODE
	}
}