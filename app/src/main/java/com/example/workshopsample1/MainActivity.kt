package com.example.workshopsample1

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.workshopsample1.activity.ContainerActivity
import com.example.workshopsample1.activity.SecondActivity
import com.example.workshopsample1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	
	//ViewBinding
	private lateinit var binding : ActivityMainBinding
	
	//Javaでのstatic変数・メソッドを記載する方法
	//→ kotlinには基本的にはstaticという概念はない(companionというものが互換としてある）
	companion object {
		//constをつけるとコンパイルで定数になるので実行時に高速に
		const val KEY_RESULT_TEXT = "keyResult"
	}
	
	//ここがこのActivityの始点
	//ライフサイクルのイベントは必ずオーバーライドになる
	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		//遷移ボタン処理
		binding.transitBtn.setOnClickListener {
			transitSecondActivity()
		}
		
		binding.transitScreenBtn.setOnClickListener {
			transitFragmentScreen()
		}
	}
	
	//別画面で値を渡したいときにはBundleを経由して渡す
	private fun transitSecondActivity() {
		//上の入力値を次画面へ渡すため、テキストをViewからStringで取得する
		val inputText = binding.inputText.text.toString()
		
		//kotlinでのapply(スコープ関数）では
		Intent(applicationContext, SecondActivity::class.java).apply {
			//intent
			putExtra(SecondActivity.KEY_INPUT_TEXT, inputText)
		}.run {
			//Intentによる遷移を開始する
			startActivityResultLauncher.launch(this)
		}
	}
	
	//画面からResultを受け取りたい場合はregisterForActivityResultを使う
	private val startActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->		//RESULT_OK（RESULT_CANCEL)はデフォルトで用意されているが、チェック用のResultコードはカスタマイズ可能（同じになればなんでもOK）
		//別々にすることでAから返ってきた値、Bから返ってきた値など分類したりするので基本的にチェック処理を行うと良い
		if (result.resultCode == RESULT_OK) {
			//返り値を取り出す
			//Kotlinでは ?.letでifのnullチェックと同じ働きが可能(スコープ関数）
			result.data?.let {
				val resultText = it.getStringExtra(KEY_RESULT_TEXT)
				
				resultText?.let {
					binding.resultValue.apply {
						text = resultText
						setTypeface(Typeface.DEFAULT_BOLD)
						setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))
					}
				}
			}
		}
	}
	
	
	private fun transitFragmentScreen(){
		Intent(applicationContext, ContainerActivity::class.java).apply {
			startActivity(this)
		}
	}
}
