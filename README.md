# Android 勉強会 #3

## Kotlin Coroutine
Kotlinで使える軽量スレッド
実行スレッドの切り替え、非同期処理、並列処理...etc
などサポートしており、これまでの非同期処理より直感的なコードで使える
Observe/Subscribeな使い方もあるので対応が幅広いのも特徴

AndroidでもCoroutineをサポートしており
Androidに合わせたCoroutineもある（後述）
https://developer.android.com/kotlin/coroutines?hl=ja


## ViewModel
https://developer.android.com/topic/libraries/architecture/viewmodel?hl=ja

M（Model） V（View） VM（ViewModel）パターンのVM

Androidは拡張ライブラリによって便利に扱えるようになっている（Android独特の動作と親和性を持つようになる）
implementation androidx.lifecycle:lifecycle-viewmodel


①一時的な値の保持として
Activity・Fragmentはライフサイクルによって保持している値が消失することがある。（Destoryされるとクラス内変数の値は破棄される）
ViewModelはライフサイクルの外にある存在なので、値を保持しておきたい、複数のクラスで共通で使いたい場合にはここに値を置いておく（こともある）


②アプリのアーキテクチャとして
https://developer.android.com/topic/architecture?hl=ja

Activity・Fragment（View）だけに処理を集めるとViewのクラスが大きなクラスになってしまう
Viewに関係のない処理を切り離すことによってFragmentの見通しを良くする
Viewに関係のない処理：APIコール・DBへの処理、一時的な値の保持、（Singletonの）Managerクラスなどへのアクセス...etc

CoroutineのFlowを利用することでObserverパターンも利用可能
※LiveDataというAndroid専用のObserverライブラリもあるが、今はCoroutineが主流

View　->　ViewModel　<-> （UseCase） -> Repository 
といった感じに各々の処理を分離することで関心の分離を行う


## APIコール＋Responseデータの反映
インターネットを経由してAPIを叩いてResponseデータをもらう
通信はすべてバックグラウンドで行い、UI更新はすべてメインスレッドで行う必要があるので注意が必要

一番利用ケースが多いRESTfulなAPI（Json）をもらい、Android内でパースして、それを画面（View）に反映する

1から全部自分でやるのは手間なのでライブラリを使って利用するのが一般的

### 事前設定
AndroidManifest.xmlに
<uses-permission android:name="android.permission.INTERNET" />
を記載する（しないとネットワーク通信ができない）

またAndroidは9から全面的にHTTP通信は許可されてないない（しようとするとクラッシュ）

HTTP通信したい場合は
application内にandroid:usesCleartextTraffic="true"を入れる
もしくはandroid:networkSecurityConfigで 特定のネットワーク内（URL）なら個別で設定することも可能
※セキュリティ的には基本的に許可しないほうがいい

### 基本的なライブラリ
・Coroutine
基本的にはKotlin標準だけでも間に合うがAndroidに向けたライブラリもあるので導入しておくと都合がいい
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
	implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")


・okhttp　https://square.github.io/okhttp/
一般的なHTTP通信をサポートするHTTPクライアント(Javaライブラリ)

・retrofit2 https://square.github.io/retrofit/
Android向けのREST通信をサポートするライブラリ（Javaライブラリ）

・moshi https://github.com/square/moshi
Jsonをパース補助するためのライブラリ（Java/Kotlinライブラリ）

※上記3つは同じ開発元なのでセットで使うことが多い（サポートも続いてる）

※代替ライブラリ
通信について
 → 最近ではKotlin純正なKotlin純正の軽量サーバサイドライブラリもある
 → サーバサイド（Spring）の代替にあたるものだが、Androidでも使える

Jsonパーサーについて
 →　こちらもKotlin純正のkotlin serializationがある
 →　純Kotlinで書く場合はこちらのサポートが手厚いので将来的にはこちらに乗り換えることになるかも？


### その他のライブラリ
・Dagger Hilt https://developer.android.com/training/dependency-injection/hilt-android?hl=ja
Android向けカスタマイズされたDIライブラリ
必須ではないが、RepositoryやManagerやUseCaseクラスなどはSingletonで問題ない
その類のものをSigletonで保持して、楽に使えるようにできるようになる。


・Coil https://coil-kt.github.io/coil/
Android向けの画像ダウンローダーライブラリ
URLから直接画像を取得して表示する場合に使う
ローディング中の画像をセットしたり、エラー時の画像、画像キャッシュなど設定できる
ComposeやCoroutineとの相性もいい



### APIをコールする
※上記のライブラリを導入した前提


①Okhttp/Retofit/Moshiの設定
ApiModules.kt参照

Okhttpでタイムアウトなどの基本的な設定、Logging
Moshiのインスタンスを作成（＋追加設定）
RetrofitでAPIのルート（ベース）URLの設定、HTTPクライアントの設定（okhttpを入れる）、Jsonのパーサーを設定する

通信にAuthTokenがいるようなサーバ設定ならここらへんでOkhttpに設定

②Responseで使うModelクラスを作成する
WeatherResponse.kt参照

Jsonのクラス構造・パラメータに対応したクラスを各種作成する
基本的にはDataClassで宣言
moshiを使えばパラメータ名とAndroidの変数名を別に設定することも可能


③APIコールのためのInterfaceクラスを作成する
WeatherApiService.kt参照

RetrofitはInterface使ってルーティングを行うので
ここでCURDプロトコル、残りのURLのPathを記載する（アノテーションで設定）
suspendな関数として使えば自動でCoroutine対応になっている

※POSTするときはそのままRequestなデータクラスを渡せば自動でパースしてくれる（基本的な設定はResponseクラスと同一）


④APIのResponse（Request）するためのRepositoryクラスを作成する
WeatherApiRepository.kt参照
③のInterfaceクラスを使って実際にAPIコールするメソッドを記載する
関心の分離のためResponse/Requestに集中するようなクラスが理想


※UseCaseクラスがあるならViewModel前にもう一枚噛ませる
⑤ViewModelにRepositoryクラスのメソッドコールを実装する
WeatherFragmentViewModel.kt参照

Fragment(View)で扱いやすいようにデータを取得、加工、分離をしたりする
suspendでコールする、viewModelScopeを使ってコール、Observerパターンでの実装などパターンは様々

⑥Viewへの反映
WeatherFragment.kt参照
⑤で対応した形式によってViewにデータを反映する

