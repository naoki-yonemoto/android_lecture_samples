package com.example.workshopsample1

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.workshopsample1.databinding.ActivityMainBinding
import com.example.workshopsample1.fragment.MainFragment

class MainActivity : AppCompatActivity() {
	
	private lateinit var binding : ActivityMainBinding
	
	//ここがこのActivityの始点
	//ライフサイクルのイベントは必ずオーバーライドになる
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater).apply {
			setContentView(this.root)
		}
		
		val toolbar = binding.toolbar.apply {
			title = "サンプルアプリ#4"
		}
		
		setSupportActionBar(toolbar)
		supportActionBar?.apply {
			setDisplayHomeAsUpEnabled(true)
		}
		
		
		val fragment = MainFragment()
		val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
		transaction.add(R.id.container, fragment)
		transaction.commit()
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home) {
			//戻るを押したら前画面を終了（前画面がないならアプリを終了）
			finish()
		}
		return super.onOptionsItemSelected(item)
	}
}
