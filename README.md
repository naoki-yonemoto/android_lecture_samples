# Android 勉強会 #2

## ViewBinding(DataBinding)
XMLからレイアウトを構成する際、Androidが自動で生成してくれたBindingクラスからViewへのリンクを読み取る方法

何が便利（メリット）か?
①
findViewByIdだと「別のレイアウトファイル」のレイアウトIDまで設定できてしまう。
→ 間違えて設定した場合などはコンパイルは通るのに、実行時にエラー(ぬるぽ）が出てアプリが落ちてしまう。
→ Bindingを使えばこの問題は解決する

②
キャッシュが効く
→ findViewByIdをその都度呼ぶとコストがかかる
→ 変数に置いておけば解決できるが、あまりクラス内変数を置くのも結構微妙
→ 自動でキャッシュを効かせて、変数が少なく済むのでスッキリする


ViewBindingとDataBindingの違い
DataBindingを使ってViewを作るとKotlinコードの変数をUIに自動的に追従・反映してくれる。
→ こっちのほうがほぼ上位互換じゃんと思われるが、勝手に追従したりするので結構考えて作り込まないといけない点もある
→ またタイミング的にうまく反映しなかったりすると、デバッグ追従が結構たいへんだったり。



##　Fragment
Activityに似たViewのシステム（UI表示を担当する）
Activityの上に乗っかる画面のイメージ

何が便利？
→ 柔軟性に富む（1つのActivityに対して複数のFragmentを持つことができる）　ページ風のレイアウトにするときに効果を発揮しやすい
→ 画面遷移の管理が楽になる
→ そもそもActivityは作るのがちょっとめんどくさい。

SingleActivity系の開発だと
全体で持っておきたいならActivity
レイアウト単位で持っておきたいならFragment
（若干語弊があるが...)
みたいな区切り

ログイン画面など複雑な仕事をしない（単体で完結しがちな部分）は影響を切り離すため、Activityだけで作ったり...
プロジェクトによって扱い方は千差万別


### Androidの分かりにくい部分の概念
intent:OSシステムに対して何かを行うときに使うことが多い
intentに色々書き込んでOSに渡すとOSがよしなにした動きをやってくれる。

context:アプリが所持している情報などを持っている。システム関連から情報を引っ張ってくるときは大抵出てくる。
→内部リソースを引っ張ってきたり、特定のレイアウトを
また、UIのシステムに対してこれを渡すことも多い。
Viewに紐づいているのでActivity/Fragmentのライフサイクルに従う

applicationContext:
contextの一種、アプリ全体を司っているContext。なので必ず存在する（シングルトン）
Viewに紐づくことはないが、扱い方を間違えるとメモリリークする原因に

Bundle:OSに対してこちらから情報を渡すときに使う入れ物(のようなもの)
ここから値を取り出したりする


概念が難しい＆正直慣れなので今はあまり考えなくてもいい


## 画面遷移
表示している画面（Activity or Fragment)を切り替えること
スマホは（基本的には）1画面しかないので切り替えて情報を表示する
データも渡せるし、戻るときにデータを返すこともできる

### Activity
基本形（遷移だけ）
startActivity(intent, [ClassName]::class.java)

【データ情報を渡す】
・送る側
渡すIntentにputExtraで値をセットする、データを受け取るために必要なKEYもセットする
基本的にはプリミティブしか渡せないが、特定の処理をすれば、データ用のクラス自体を渡すことも可能（シリアライズ、デシリアライズ）

・受け取る側
送るときと同じKEYでintetからデータを抜き出す
間違ってたりデータがなかったりするとnullになるのでNullチェックはしておいたほうが無難（特に入ってる前提で受ける場合には）


【情報を返すパターン】
・送る側の画面
渡すときと同じようにIntentに情報を詰める
setResult(intent)をすれば前画面で下記で準備してあるResultAPIが発火する
こちらもKEYがいるが、RESULTチェックのためのKEY(リクエストコード/リザルトコード）とデータを取り出すためのKEYをセットしておく必要がある

・受け取る画面
遷移する前に registerForActivityResult を使って遷移することで、あらかじめ返しがあること設定させる（コールバック設定）
（返ってきた値を処理するにはラムダ処理で記載すると楽）
KEY2つをつかってデータのチェックをすると安全な実装

※補足
startActivityやregisterForActivityResultには他にも色々使い方があるがそれは今後
昔はonActivityResultが使われていたが、今は非推奨なので使わない（かなり長く使われていたので古い参考コードだとかなり残ってたりする）

https://smile-jsp.hateblo.jp/entry/android/activity-result-api-startactivityforresult
https://zenn.dev/chiii/articles/8c21622eae794d


### Fragment
FragmentはFragmentManagerというものがFragmentの遷移階層を管理している。
基本的にはStack形式の管理でpopを命令すると前の画面に戻る

基本形(遷移だけ)
Activity → Fragment
val fragment = [ClassName]Fragment()
val fragmentTransaction = supportFragmentManager.beginTransaction()
fragmentTransaction.add(R.id.container, fragment)
fragmentTransaction.commit()


【情報を渡すパターン】
・送る側
フラグメントのインスタンスを作った後、argumentsという部分に、Bundle形式でデータをKEYと入れる

・受け取る側
argumentsからKEYを使って取り出す。（これもなければNullになる）

【情報を返すパターン】
・送る側の画面
setFragmentResultを使って送るときと同じようにBundleを使ってKEYとデータを入れる
Activityと同じでRESULTチェック用のKEY（リクエストコード/リザルトコード）も必要

・受け取る画面
ライフサイクルに予め用意しておいたsetFragmentResultListenerにコールバックが来る
bundleが流れてくるのでここからデータを取り出す。

※補足
Fragmentでの遷移・値渡しにはSafeArg、NavigationGraphなど便利なライブラリもあるので興味があるなら調べるとGOOD
こちらも昔はonActivityResultが使われていたが、今は非推奨なので使わない

https://tech.mokelab.com/android/Fragment/result-ja.html

### なぜコンストラクタ・Setterで値を渡さないのか
参考：
https://android-tech.jp/fragmnet-newinstance/

簡単に言えばライフサイクルに適合しない挙動になるから
OSのシステムに依存した領域にデータを置いておくことで適合させる
他にはプログラム的にはやっぱり変数はPrivateが基本なのでむやみやたらにセッターを作らない
（KotlinはGetterSetterは自動生成なのでprivateだけでOK)


また今後説明するがViewModelを利用して共通にデータを参照する部分を用意しておくなど
データ共有の仕方は様々