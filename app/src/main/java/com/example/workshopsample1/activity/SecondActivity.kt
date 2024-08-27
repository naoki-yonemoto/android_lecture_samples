package com.example.workshopsample1.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.workshopsample1.MainActivity
import com.example.workshopsample1.R
import com.example.workshopsample1.databinding.ActivitySecondBinding

class SecondActivity: AppCompatActivity() {
	
	private lateinit var binding : ActivitySecondBinding
	
	companion object{
		const val KEY_INPUT_TEXT = "keyInput"
	}
	
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivitySecondBinding.inflate(layoutInflater).apply {
			setContentView(this.root)
		}
		
		val beforeInputText = intent.getStringExtra(KEY_INPUT_TEXT)
		beforeInputText?.let {
			binding.setValue.text = beforeInputText
			binding.setValue.setTextColor(ResourcesCompat.getColor(resources, R.color.orange, null))
		}
		
		binding.finishBtn.setOnClickListener {
			backToMainActivity()
		}
	}
	
	
	private fun backToMainActivity(){
		val inputText = binding.resultValue.text.toString()
		
		intent.apply {
			putExtra(MainActivity.KEY_RESULT_TEXT, inputText)
			
		}
		setResult(RESULT_OK, intent)
		
		//Activityを終了する(画面を閉じる）
		finish()
	}

}