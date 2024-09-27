package com.example.workshopsample1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.example.workshopsample1.R
import com.example.workshopsample1.data.*
import com.example.workshopsample1.databinding.FragmentMainBinding
import com.example.workshopsample1.databinding.ListItemWetherForecastBinding
import com.example.workshopsample1.utils.ApiResultOf
import com.example.workshopsample1.viewmodel.WeatherFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class WeatherFragment : Fragment() {
	
	private var _binding : FragmentMainBinding? = null
	private val binding get() = _binding!!
	private val viewModel : WeatherFragmentViewModel by viewModels()
	
	private lateinit var weatherListAdapter : WeatherListAdapter
	
	override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
		_binding = FragmentMainBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		setLoading()
		
		//suspend funをコールするにはCoroutineScope内からコールする
		lifecycleScope.launch {
			//suspend Functionをコールする
			viewModel.getWeatherInformation(CityCode.Tokyo)
		}
		
		initWeatherInformation()
		reloadWeatherInfo()
	}
	
	//値をそのままFragmentまで返すケース
	private suspend fun callApi(){
		val result = viewModel.getWeatherInformation2(CityCode.Tokyo)
	}
	
	//LoadingViewをセットする
	//APIコール中はLoadingアイコンとか出す(必要ならば）
	private fun setLoading() {
		lifecycleScope.launch {
			viewModel.loading.collect {
				binding.loading.visibility = if (it) View.VISIBLE else View.GONE
			}
		}
	}
	
	private fun initWeatherInformation() {
		lifecycleScope.launch {
			viewModel.weatherInformation.collect { result ->
				viewModel.setLoading(false)
				
				when (result) {
					is ApiResultOf.Success -> {
						val response = result.value
						setOverView(response)
						initWeatherList(response.forecasts)
					}
					
					is ApiResultOf.Failure -> {
						//Error Handling
					}
				}
			}
		}
	}
	
	private fun setOverView(response : WeatherResponse) {
		binding.publicationDateTime.text = getString(R.string.format_publication_dateTime, response.publicTimeFormatted)
		binding.locationPoint.text = response.title
		binding.publicationHeader.text = response.description.descriptionBodyText
	}
	
	//Adapter&Listに使うデータをセットする
	private fun initWeatherList(listData : List<WeatherForecasts>) {
		binding.forecastList.apply {
			this.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
			weatherListAdapter = WeatherListAdapter()
			this.adapter = weatherListAdapter
			weatherListAdapter.submitList(listData)
		}
	}
	
	private fun reloadWeatherInfo(){
		binding.reload.setOnClickListener {
			lifecycleScope.launch(Dispatchers.IO) {
				when(viewModel.weatherPoint){
					is CityCode.Tokyo -> {
						viewModel.getWeatherInformation(CityCode.Osaka)
					}
					
					is CityCode.Osaka -> {
						viewModel.getWeatherInformation(CityCode.Tokyo)
					}
				}
			}
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	
	companion object {
		private val TAG = WeatherFragment::class.java.simpleName
		private const val TOKYO_CODE : String = "130010"
		private const val OSAKA_CODE : String = "270000"
	}
	
	private inner class WeatherListAdapter : ListAdapter<WeatherForecasts, WeatherListAdapter.WeatherListViewHolder>(DIFF_UTIL_ITEM_CALLBACK) {
		
		private inner class WeatherListViewHolder(val itemBinding : ListItemWetherForecastBinding) : RecyclerView.ViewHolder(itemBinding.root) {
			
			fun bind(itemData : WeatherForecasts) {
				
				setWeatherIconImage(itemData.iconImage)
				
				//曜日
				itemBinding.dateLabel.text = getString(R.string.format_date_label, itemData.dateLabel)
				
				//天候(文字）
				itemBinding.locationWeatherText.text = itemData.weatherLabel
				
				//最低気温(特定の時間がすぎると気温が取得できない）
				val minvalue = itemData.temperature.min.celsius ?: "--"
				itemBinding.minTemp.text = getString(R.string.format_min_temperature, minvalue)
				
				//最高気温
				val maxvalue = itemData.temperature.max.celsius ?: "--"
				itemBinding.maxTemp.text = getString(R.string.format_max_temperature, maxvalue)
				
				
				setChanceOfRainPercent(itemData.chanceOfRain)
			}
			
			//天気アイコンの設定（CoilでURLから読み込む)
			//SVGなのでCoilの拡張ライブラリも追加
			private fun setWeatherIconImage(iconImage : WeatherIconImage) {
				itemBinding.locationWeatherIcon.load(iconImage.url){
					decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
				}
			}
			
			//降水確率
			private fun setChanceOfRainPercent(rainPer : ChanceOfRain) {
				itemBinding.lateNightPer.text = rainPer.lateNight
				itemBinding.morningPer.text = rainPer.morning
				itemBinding.afternoonPer.text = rainPer.afternoon
				itemBinding.nightPer.text = rainPer.night
			}
		}
		
		override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : WeatherListViewHolder {
			val itemBinding = ListItemWetherForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
			return WeatherListViewHolder(itemBinding)
		}
		
		override fun onBindViewHolder(holder : WeatherListViewHolder, position : Int) {
			holder.bind(getItem(position))
		}
	}
	
	private val DIFF_UTIL_ITEM_CALLBACK = object : DiffUtil.ItemCallback<WeatherForecasts>() {
		override fun areItemsTheSame(oldItem : WeatherForecasts, newItem : WeatherForecasts) : Boolean {
			return oldItem == newItem
		}
		
		override fun areContentsTheSame(oldItem : WeatherForecasts, newItem : WeatherForecasts) : Boolean {
			//本当はユニークなIDとかのが良い
			return oldItem.date == newItem.date
		}
		
	}
}