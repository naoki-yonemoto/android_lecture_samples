package com.example.workshopsample1.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.workshopsample1.databinding.ActivityFragmentContainerBinding

class ContainerActivity:AppCompatActivity() {
	
	private lateinit var binding : ActivityFragmentContainerBinding
	
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityFragmentContainerBinding.inflate(layoutInflater).apply {
			setContentView(this.root)
		}
		
		
		//Fragmentを生成してセットする
		
	}
}