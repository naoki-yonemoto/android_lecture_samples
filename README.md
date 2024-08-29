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


## 不定長のList構造のUI
リスト構造は画面表示ではよくある基本的なレイアウトになる
リストを表現したい場合にはRecyclerViewというものを利用する

ListViewというものもあるが、昔のもので柔軟性が低く、単純に使いにくいので覚えなくていい（リストを作るときは基本全部RecyclerViewで作るぐらいでいい）

事前にRecyclerViewのライブラリを入れておく（標準では入ってない）
implementation("androidx.recyclerview:recyclerview:1.3.2")


### 個別のレイアウトを作る
普通の全体画面にRecyclerViewを置いたものに加えて
リストのアイテム1つに対応する個別のレイアウトを作る

基本的な構造：
https://qiita.com/naoi/items/f8a19d6278147e98bbc2
https://qiita.com/soutominamimura/items/47a48e4e6e1aff3d3396



### なんでこんなことしてるのか？
Listは最悪無限長にまで拡大するのでデータが大量にある場合メモリ消費が激増する
画面から出たアイテムレイアウト


### 補足
今回は自前でListのDataの内容が変更されたら更新する動作を入れたが、
自前でListのDataの内容を変更して、変更された位置を自分で計算してピンポイントで更新する...
というのが面倒くさい！！（複雑になるとかなり難しくなりがち）

そこで追加されたRecyclerView用の自動更新付きAdapterのListAdapterというものがある（ListViewのAdapterではないのでややこしい）
RcyclerView.Adapterを継承してGoogleが作ったアイテム更新を楽にするためのAdapterクラス

https://qiita.com/chohas/items/acbf3787cd80b5277af7
https://qiita.com/Ryosuke-Android/items/45f02b2fe270b3bd5068

簡単に言えば、Adapterに対してリストを丸ごと投げると変更差分だけの更新メソッドを使ってくれる（内部の更新もバックグラウンドでよしなに更新してくれる）
→コストが小さい

というかこっちのほうが簡単だから、最初からこれで実装したほうがいいかもしれない。