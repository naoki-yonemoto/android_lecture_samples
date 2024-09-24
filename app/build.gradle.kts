plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.jetbrains.kotlin.android)
	id("com.google.devtools.ksp")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "com.example.workshopsample1"
	compileSdk = 34
	
	defaultConfig {
		applicationId = "com.example.workshopsample1"
		minSdk = 30
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"
		
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}
	
	buildTypes {
		debug {
			buildConfigField("String", "BASE_URL", "${property("debug.api_url_base")}")
			
		}
		
		release {
			buildConfigField("String", "BASE_URL", "${property("release.api_url_base")}")
			
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	
	kotlinOptions {
		jvmTarget = "17"
	}
	
	buildFeatures {
		viewBinding = true
		buildConfig = true
	}
	
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	
	//version catalog（別ファイルに記載する方法）を利用して記載する
	implementation (libs.androidx.appcompat) //バージョンが下の下位互換をサポートするためのライブラリ
	
	implementation ("androidx.activity:activity-ktx:1.9.0") // Activityを便利に扱えるライブラリ
	implementation ("androidx.fragment:fragment-ktx:1.8.1") // Fragmentを便利に扱えるライブラリ
	implementation("androidx.constraintlayout:constraintlayout:2.1.4") // ConstraintLayoutを使うためのライブラリ
	implementation("androidx.recyclerview:recyclerview:1.3.2")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
	
	//kotlin coroutine
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
	implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
	
	
	//http connection
	implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
	implementation("com.squareup.okhttp3:okhttp")
	implementation("com.squareup.okhttp3:logging-interceptor")
	
	//Routing
	implementation("com.squareup.retrofit2:retrofit:2.11.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
	
	//Json Parser
	implementation("com.squareup.moshi:moshi:1.15.1")
	implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
	ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")
	
	//DI
	implementation("com.google.dagger:hilt-android:2.51.1")
	ksp("com.google.dagger:hilt-android-compiler:2.51.1")
	
	//coil
	implementation("io.coil-kt:coil:2.7.0")
	implementation("io.coil-kt:coil-svg:2.7.0")
	
	
	//↓は外部からバージョンの記載を受け継いでいるだけ
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

}