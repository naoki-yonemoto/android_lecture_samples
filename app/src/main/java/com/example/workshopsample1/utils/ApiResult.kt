package com.example.workshopsample1.utils

import retrofit2.Response


//Kotlin: sealed class / sealed interface
//Enumクラスの便利版のようなもの、DataClassと固定なObjectを保持できる
//Enumのような区分などを分類する＆後から追加で情報を持たせたいような処理する場合には便利
sealed class ApiResultOf<out T>{
	data class Success<out R>(val value : R) : ApiResultOf<R>()
	data class Failure(val errorType : ErrorType) : ApiResultOf<Nothing>()
}

//エラーをハンドリングしやすいように内部のResultクラスに置き換える
fun <T : Any> safeApiCall(response: Response<T>): ApiResultOf<T> {
	return try {
		return if(response.isSuccessful){
			ApiResultOf.Success(response.body()!!)
		} else {
			// 実際のプロジェクトではもっと細かく丁寧にハンドリングしたりする（必要ならエラーメッセージを見たり...
			// ここらへんは設計に合わせた実装をする
			ApiResultOf.Failure(ErrorType.ClientError())
		}
	} catch (e: Exception) {
		ApiResultOf.Failure(ErrorType.ClientError())
	}
}

sealed interface ErrorType{
	val errorMessage: String
	
	data class ClientError(
		override val errorMessage : String = "400系Error"
	) : ErrorType

	data class ServerError(
		override val errorMessage : String = "500系Error"
	) : ErrorType
}
