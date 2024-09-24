package com.example.workshopsample1.di

import com.example.workshopsample1.BuildConfig
import com.example.workshopsample1.api.WeatherApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * API通信を行うためのModule群
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModules {
	
	@Provides
	@Singleton
	fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService =
		retrofit.create(WeatherApiService::class.java)
	
	
	@Provides
	@Singleton
	fun provideOkHttpClient():OkHttpClient = OkHttpClient.Builder()
		.connectTimeout(60, TimeUnit.SECONDS)
		.readTimeout(60, TimeUnit.SECONDS)
		.writeTimeout(60, TimeUnit.SECONDS)
		.addInterceptor(HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		})
		.build()
	
	@Provides
	@Singleton
	fun provideMoshi() : Moshi =
		Moshi.Builder()
			.add(KotlinJsonAdapterFactory())
			.build()
	
	
	@Provides
	@Singleton
	fun provideRetrofit(): Retrofit =
		Retrofit.Builder()
			.baseUrl(BuildConfig.BASE_URL)
			.client(provideOkHttpClient())
			.addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
			.build()
}