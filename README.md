# Android 勉強会 #1

## Androidについて
Google社がオープンソース開発しているスマホ向けのOS
　（カスタマイズされたものがスマホ以外でもAndroidシステムが乗ってることがある）

開発言語はJava / Kotlin / XML +α

開発環境（IDE）にはAndroid　Studioを利用する
基本的には最新でOK（検索して公式サイトTOPからダウンロードできるもの）

## Androidのファイル構成（大枠）

・Root
　├ AndroidManifest.xml
　├ Java/Kotlin
　├ resファイル
　┗ .gradle

###　各構成の役割

#### AndroidManifest.xml
アプリケーション全体のルール基盤を記載しておくところ
→ アプリのアイコンや開始する前の動作
→ ”このアプリはGPSを使います”などの権限(パーミッション)をあらかじめ宣言（使うときにはコード内部で認可が別途必要）
→ その他特殊な動作（レシーバー、サービス）の宣言
→ ライブラリで指定された情報を予め記載して部分

※レシーバー：情報が外部から渡されたときの振る舞い
※サービス：画面表示に現れないが、アプリの動作として振る舞えるシステム

#### Java/Kotlin
アプリ全体の振る舞いを記載するところ（メインのコード）
基本的にはここを煮詰めていくのが仕事になる

#### resファイル
リソース（resource）ファイル
ここで言うリソースは予めアプリ内で持っておく画像、文字列、UI画面など静的なコンテンツ

#### .gradle
Androidのビルドを行ってアプリの形にするにはgradleを使ってビルドをする必要がある。
ここではビルドに必要な情報を記載する。
プロジェクトのgradleファイルとアプリのgradleファイルの2種類が存在している。
プロジェクトgradleは基本的には言語自体のバージョンやライブラリのリポジトリなどの基本的なもの、
アプリのgradleはAndroidに必要な要素を記載することが多い。
Androidオリジナルの要素としていくつかの構成要素がある（アプリケーションのID設定 ,システムバージョンや、コンパイルの仕方、暗号化の設定...etcなど）

→ システムバージョン
”Android 14”などのOS自体のバージョンを表すもの。
Googleが基本的に毎年リリースしてバージョンアップをしていっているもの。
ただし内部的にはTargetSDKバージョンとか言われ、専用のシステムコード番号がある。
(Android TargetSDKで検索すると一覧が出る)

ここだけ言語はKotlin or groovy (今はKotlinが主流)


#### Googleが公式リリースしているAndroid開発を手助けするライブラリ
AndroidXやJetPackなどと呼ばれる
https://developer.android.com/jetpack/androidx/versions?hl=ja

①コードを書きやすくしてくれる効果
ラムダで受け取れたり、コード自体を拡張して書きやすくしてくれる効果がある

②特定・新機能を使える/使いやすくするための＋αなライブラリ
代表的なものはConstraintlayout、RecyclerViewなどの開発の補助となるようなライブラリ

③OSバージョン差異を吸収して同じような振る舞いをしてくれるようなサポート用のライブラリ
AndroidはOSによって追加されるAPI・機能、非推奨になったAPIなどOS内でのコードに差分が発生する場合
同じようなコードでライブラリ内部でどのOSのコードを呼び出すか自動で切り分けてくれたりする。


## Androidのアプリでのレイアウト表示を交えた開発
レイアウト表示システムには
・Java/Kotlin + XML (Android View)
・JetPack Compose (kotlin) （2020年あたりから本格リリース）
があるが、基本的にはAndroid Viewの方でしばらくは説明する。
→ノウハウなどは圧倒的にこちらの方が上。まだまだこちらも使われている。
振る舞いとViewの役割がはっきり分かれていて基本的なシステムなため。


※Compose →　Fultter（Dart）やSwiftUI（iOS)と同じ概念の開発システム
コードでUIと振る舞いを一緒動かせるシステム。
慣れている人ならViewシステムより早く、より有機的にコーディングできる（おそらく...)

## Android開発における注意点
### ActivityとFragment
画面表示のシステムにはActivityとFragmentというものがある。
Activityは基本の画面表示システム。
画面1つに対して1つしか用意できない。
FragmentはActivityの上に乗っかる画面表示システム。
1つのActivityの上にいくつものFragmentを乗せることができる。

※厳密にはもっと色々差があるが複雑なので大枠はこのような覚え方で良い
Activityは追加するときにはManifest.xmlに追加したり手間があったりする。Fragmentは特に制約がない。

### スレッド
Android（iOSも）は画面を描写するなどUIを操作する動作にはメインスレッド（UIスレッド）を利用しなければいけない。
また、通信を行う、DBから値を取得するなどの行為はサブスレッドで行わなれけばならない。
→　これを破るとアプリが強制終了（クラッシュ）する

基本的にはOSはメインスレッドを使っているが、通信するときなどは注意

### ライフサイクル
OSシステムが則ったタイミングで、適切な行動をしないといけない。（検索するとフローが出てくる）
onCreate, onResume...etc
→ （今は違うが）昔はハードの性能に限界があったため、メモリを有効に使えるようなOSシステムになっているため。
OSはライフサイクルを監視してメモリから開放するなどの行為を自動でやってくれるため、それに逸脱する行為はNG

ドキュメントURL：
https://developer.android.com/guide/components/activities/activity-lifecycle?hl=ja#kotlin




## View(表示する画面）の作り方（Android　Viewの場合）

各種ウェジェットを組み合わせてXML上で表現する。
よほど複雑な構造のものでなければ基本的にはAndroid（Google）が用意してくれたものを利用すればOK

以下は開発で使うメジャーなもの

###　枠組み系
ConstraintLayout
LinerLayout
FrameLayout
ScrollView
RecyclerView
ViewPager2

基本的にはレイアウトの大枠になる。
この中にアイテム系のものを入れたり、枠組み系をネストさせたりしてレイアウトを作っていく

### アイテム系
TextView
EditText
Button
Switch
ImageView

ユーザーに触ってもらうViewだったり、内容を伝えたりするものが多い

### その他
WebView
ProgressBar
Space
BottomNavigationView

用途がかなり特化している専用のView（プロジェクトでは使わないこともある）


ベースとなるアイテムを元に使い方を特化したもの、カスタマイズ派生したものがデフォルトでもたくさんある。
例：トグルできるToggleButton, 画像をボタンにできるImageButton, 択一選択させるRadioButton...etc


### ConstraintLayout
implementation("androidx.constraintlayout:constraintlayout:2.1.4")(数字はバージョン)
が必要
中身のレイアウトの位置を制約によって制御する

画面端から画面端まで
画面端から◯◯のViewのところまで
◯◯のViewの下から表示

など相対的にレイアウトを配置する。
画面に合わせたレスポンシブなUIになりやすいのが最大の利点

app:layout_constraintTop_toTopOf...みたいに特定の位置からといった感じで指定していく
基本的には2方向指定すれば制約が成立する。
つけなかったり/つけすぎるとレイアウトが崩れたりするので結構難しい。

### LinearLayout
棚のように中身のView並べていくようになる
横向き：vertical
縦向き：horizontal


### レイアウトにできる命令
android:の項目で基本的な制御ができる

#### id
システムが判別するためにつける名前みたいなもの
なくても表示してくれるが、コードから操作したいときには付ける必要がある。

#### layout_width/layout_height(必須)
Viewの横幅と縦幅を指定する。数値で指定する場合はDPという値を使う　100dpなど
システムがおまかせのサイズ指定もある
wrap_content：そのレイアウトが取れる最小幅
match_parent；そのレイアウトが取れる最大幅

#### PaddingとMargine
Padding:内側に余白を持つ
Margine：外側に余白を持つ






### Google公式のトレーニング
https://developer.android.com/courses/fundamentals-training/toc-v2?hl=ja

レッスン1〜レッスン4くらいまでが基本的なところ
バックグラウンド動作とかユーザーデータの保存はあまり使わないのでスルーしても良い(内容も古い)
→使うときに調べたほうが都合がいい