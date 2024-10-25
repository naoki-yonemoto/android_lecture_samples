package com.example.workshopsample1.push

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.workshopsample1.R

object LocalNotificationManager {
	
	private const val NOTIFICATION_ID = 1
	
	@SuppressLint("MissingPermission")
	fun actionLocalNotification(context : Context){
		// ②通知の準備
		val channelId = "NOTIFICATION_LOCAL"
		val builder = NotificationCompat.Builder(context, channelId).apply {
			setSmallIcon(R.drawable.baseline_android_24)
			setContentTitle("通知タイトル")
			setContentText("通知内容")
			priority = NotificationCompat.PRIORITY_HIGH
		}

		// ③チャネルの設定
		// Android8（API26）以上の場合、チャネルに登録する
		val name = "通知名サンプル"
		val description = "通知説明文サンプル"
		val importance = NotificationManager.IMPORTANCE_HIGH
		val channel = NotificationChannel(channelId, name, importance).apply {
			this.description = description
		}
		// システムにチャネルを登録する
		val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		manager.createNotificationChannel(channel)
		
		// ④ローカルプッシュ通知を送信する
		with(NotificationManagerCompat.from(context)) {
			notify(NOTIFICATION_ID, builder.build())
		}
	}

}