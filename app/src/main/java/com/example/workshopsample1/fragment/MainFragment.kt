package com.example.workshopsample1.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.workshopsample1.databinding.FragmentMainBinding
import com.example.workshopsample1.push.LocalNotificationManager.actionLocalNotification
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainFragment: Fragment() {
	
	private var _binding : FragmentMainBinding? = null
	private val binding get() = _binding!!
	
	override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
		_binding = FragmentMainBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		
		binding.cameraButton.setOnClickListener {
			startQRCodeActivity()
		}
		
		binding.pushButton.setOnClickListener {
			startNotificationLauncher()
		}
		
		binding.pushActionButton.setOnClickListener {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
				&& ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
				permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
			} else {
				actionLocalNotification(requireContext())
			}
		}
		
		binding.gpsFineButton.setOnClickListener {
			startGPSFinePermissionLauncher()
		}
		
		binding.gpsCloserButton.setOnClickListener {
			startGPSCloserPermissionLauncher()
		}
		
		binding.multiPermissionButton.setOnClickListener {
			startMultiPermissionLauncher()
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	private fun startQRCodeActivity(){
		permissionCameraLauncher.launch(Manifest.permission.CAMERA)
	}
	
	//PUSHの権限はAndroid13↑から
	private fun startNotificationLauncher(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
			&& ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
		} else {
			Toast.makeText(requireContext(), "権限は許可されています", Toast.LENGTH_SHORT).show()
		}
	}
	
	//位置情報の権限はバージョンによって結構差がある
	// android.permission.ACCESS_COARSE_LOCATION　（おおよその位置）
	// android.permission.ACCESS_FINE_LOCATION　（正確な位置）
	// android.permission.ACCESS_BACKGROUND_LOCATION （バックグラウンドでの取得を許可する）
	// ※バックグラウンド取得はかなり特殊で上2種と同時にリクエストしても無視される。
	// 正確な位置を許可した上でユーザーに直接OSの設定画面で許可してもらうしかない
	// 実装上になぜBG取得が必要な理由をアプリ内で説明しないとリジェクトされたりする
	private fun startGPSFinePermissionLauncher(){
		//おおよその位置か正確な位置かの2択にしたい場合はFINEでリクエストする（どちらかはユーザーが選ぶ）
		permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
	}
	
	private fun startGPSCloserPermissionLauncher(){
		//おおよその位置か正確な位置かの2択にしたい場合はFINEでリクエストする（どちらかはユーザーが選ぶ）
		permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
	}
	
	//複数の権限を一度に取得する
	//アプリに必須機能が複数ある場合などはこれを使ったり、
	//複数の権限が許可されていないと使えない機能があったりする場合は使ったりする
	private fun startMultiPermissionLauncher(){
		val requestList = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
			arrayOf(
				Manifest.permission.CAMERA,
				Manifest.permission.POST_NOTIFICATIONS,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION
			)
		}else {
			arrayOf(
				Manifest.permission.CAMERA,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION
			)
		}
		multiPermissionLauncher.launch(requestList)
	}
	
	private val permissionCameraLauncher =
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			//返し値がtrueなら許可された
			//許可状態ならtrueが返却される
			if(isGranted) {
				Toast.makeText(requireContext(), "権限の許可をしました", Toast.LENGTH_SHORT)
					.show()
				
				//QRコード読み取り画面開始
				val options = ScanOptions().apply {
					setOrientationLocked(true)
					setBeepEnabled(false)
					setBarcodeImageEnabled(true)
				}
				barcodeLauncher.launch(options)
			} else {
				Toast.makeText(requireContext(), "権限の拒否をしました", Toast.LENGTH_LONG)
					.show()
			}
		}
	
	private val permissionLauncher =
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			//返し値がtrueなら許可された
			//許可状態ならtrueが返却される
			if(isGranted) {
				Toast.makeText(requireContext(), "権限の許可をしました", Toast.LENGTH_SHORT)
					.show()
			} else {
				Toast.makeText(requireContext(), "権限の拒否をしました", Toast.LENGTH_LONG)
					.show()
			}
		}
	
	private val multiPermissionLauncher =
		registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ grantedList ->
			//Map<String, Boolean>で入っている
			// <権限：許諾フラグ>
			if(grantedList.containsValue(false)){
				val rejectList = mutableListOf<String>()
				grantedList.forEach { entry ->
					if(!entry.value) {
						rejectList.add(getPermissionName(entry.key).text)
					}
				}
				val items = rejectList.toString()
				Toast.makeText(requireContext(), "${items}の権限の拒否をしました", Toast.LENGTH_LONG)
					.show()
			} else {
				Toast.makeText(requireContext(), "全ての権限の許可をしました", Toast.LENGTH_SHORT)
					.show()
			}
		}
	
	val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
		if (result.contents != null) {
			Toast.makeText(requireContext(), "QRコードの読み取りに成功しました", Toast.LENGTH_SHORT)
				.show()
		} else {
			Toast.makeText(requireContext(), "QRコードの読み取りに失敗しました", Toast.LENGTH_SHORT)
				.show()
		}
	}
	
	sealed class AppPermission(
		open val value:String,
		open val text:String){
		
		data class LocationCoarse(
			override val value: String = Manifest.permission.ACCESS_COARSE_LOCATION,
			override val text : String = "おおよそな位置情報"
		):AppPermission(value, text)
		
		data class LocationFine(
			override val value: String = Manifest.permission.ACCESS_FINE_LOCATION,
			override val text : String = "正確な位置情報"
		):AppPermission(value, text)
		
		data class Camera(
			override val value: String = Manifest.permission.CAMERA,
			override val text : String = "カメラ"
		):AppPermission(value, text)
		
		@RequiresApi(Build.VERSION_CODES.TIRAMISU)
		data class PushNotification(
			override val value: String = Manifest.permission.POST_NOTIFICATIONS,
			override val text : String = "カメラ"
		):AppPermission(value, text)
		
		data object Other :AppPermission("", "")
		
	}
	
	private fun getPermissionName(value:String) : AppPermission {
		 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
			return when(value){
				AppPermission.LocationCoarse().value -> AppPermission.LocationCoarse()
				AppPermission.LocationFine().value -> AppPermission.LocationFine()
				AppPermission.Camera().value -> AppPermission.Camera()
				AppPermission.PushNotification().value -> AppPermission.PushNotification()
				else -> AppPermission.Other
			}
		} else {
			return when(value){
				AppPermission.LocationCoarse().value -> AppPermission.LocationCoarse()
				AppPermission.LocationFine().value -> AppPermission.LocationFine()
				AppPermission.Camera().value -> AppPermission.Camera()
				else -> AppPermission.Other
			}
		}
	}
}