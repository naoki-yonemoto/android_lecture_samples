package com.example.workshopsample1.model

//Data構造を作るに特化する場合に使いやすいオプション
//コンストラクタ宣言で値を代入しておくのが必須
data class FruitData(
	val id: Int,
	val name : String,
	val price: Int,
)