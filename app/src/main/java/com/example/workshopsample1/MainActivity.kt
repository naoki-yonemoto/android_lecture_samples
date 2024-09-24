package com.example.workshopsample1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.workshopsample1.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	
	private lateinit var binding : ActivityMainBinding
	
	//ここがこのActivityの始点
	//ライフサイクルのイベントは必ずオーバーライドになる
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater).apply {
			setContentView(this.root)
		}
		
	}
}
