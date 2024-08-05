package com.example.workshopsample1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
	
	//画面が転回すると保持していた値はリセットされる
	private var count = 0
	
	//ここがこのActivityの始点
	//ライフサイクルのイベントは必ずオーバーライドになる
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		
		setContentView(R.layout.activity_main)
		
		//　レイアウトの動作を設定する
		val textView = findViewById<TextView>(R.id.main_title_text)
		val colorChangeButton = findViewById<Button>(R.id.color_change_button)
		colorChangeButton.setOnClickListener {
			textView.setTextColor(getColor(R.color.red))
		}
		
		val textChangeButton = findViewById<Button>(R.id.text_change_button)
		onChangeTextWord(textChangeButton, textView, "WORLD, HELLO!")
		
		val visibleButton = findViewById<Button>(R.id.text_visible_button)
		visibleButton.setOnClickListener {
//			textView.visibility = View.INVISIBLE
			textView.visibility = View.GONE
		}
		
		reverseTextWord(textView)
		
		counterFunction()
	}
	
	//メソッド化
	//Androidは大抵onCreate,onResumeに処理がものすごい偏るので(レイアウトの処理と通信の処理などは大抵ここに集まる）
	//長くなりそうならメソッドに切り出していく
	private fun onChangeTextWord(button : Button, textView : TextView, text: String){
		button.setOnClickListener {
			textView.text = text
		}
	}
	
	private fun reverseTextWord(textView : TextView){
		val reverseButton = findViewById<Button>(R.id.reverse_button)
		reverseButton.setOnClickListener {
			textView.text = "Hello, World."
			textView.setTextColor(getColor(R.color.black))
			textView.visibility = View.VISIBLE
		}
	}
	
	private fun counterFunction(){
		val countText = findViewById<TextView>(R.id.countText)
		val plusButton = findViewById<Button>(R.id.plus_button)
		val minusButton = findViewById<Button>(R.id.minus_button)
		
		plusButton.setOnClickListener {
			countText.text = (count++).toString()
		}
		
		minusButton.setOnClickListener {
			countText.text = (count--).toString()
		}
	}
	
	override fun onResume() {
		super.onResume()
	}
	
	override fun onDestroy() {
		super.onDestroy()
	}
	
}
