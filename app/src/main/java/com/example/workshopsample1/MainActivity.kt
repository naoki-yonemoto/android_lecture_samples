package com.example.workshopsample1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
	
	//ここがこのActivityの始点
	//ライフサイクルのイベントは必ずオーバーライドになる
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		
		//　ボタンの動作を設定する
		val colorChangeButton = findViewById<Button>(R.id.color_change_button)
		val textView = findViewById<TextView>(R.id.main_title_text)
		colorChangeButton.setOnClickListener {
			textView.setTextColor(getColor(R.color.red))
		}
		
		val textChangeButton = findViewById<Button>(R.id.text_change_button)
		onChangeTextWord(textChangeButton, textView, "WORLD, HELLO!")
	}
	
	//メソッド化
	//Androidは大抵onCreate,onResumeに処理がものすごい偏るので長くなりそうならメソッドに切り出していく
	private fun onChangeTextWord(button : Button, textView : TextView, text: String){
		button.setOnClickListener {
			textView.text = text
		}
	}
	
	override fun onResume() {
		super.onResume()
	}
	
	override fun onDestroy() {
		super.onDestroy()
	}
	
}
