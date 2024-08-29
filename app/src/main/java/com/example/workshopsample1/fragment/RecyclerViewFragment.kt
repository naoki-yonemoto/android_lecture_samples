package com.example.workshopsample1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workshopsample1.R
import com.example.workshopsample1.databinding.FragmentRecyclerViewBinding
import com.example.workshopsample1.databinding.ListItemFruitListBinding
import com.example.workshopsample1.model.FruitData
import com.example.workshopsample1.model.sampleListData

class RecyclerViewFragment: Fragment() {
	
	private var _binding : FragmentRecyclerViewBinding? = null
	private val binding get() = _binding!!
	
	private lateinit var fruitItemAdapter : ListItemAdapter
	
	//テストデータ
//※1	private var sampleItems = mutableListOf<FruitData>()
	
	private var sampleItems2 = sampleListData.toMutableList()
	
	override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View {
		_binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
//※1		sampleItems.addAll(sampleListData)
//※1		fruitItemAdapter = ListItemAdapter(sampleItems)
		fruitItemAdapter = ListItemAdapter()
		
		binding.list.apply {
			//棚形式のリスト、縦方向へのスクロール、正順(上から順番にリストアイテムを展開する）の指定
			//GridLayoutManagerを使えば格子型（Amazonの商品陳列みたいな）のリストもできる
			layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
			//アイテム表示に必要なアダプターをセットする
			adapter = fruitItemAdapter
			//アイテムの間に区切り線を引く(これぐらいならViewファイルに直接入れてもいいけど)
			//もっとガッツリカスタマイズしたい場合（特定の場所にしか区切りを入れたくないなどはRecyclerView.ItemDecorationを継承したカスタムクラスを作る必要がある
			addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
		}
		
		binding.sortBtn.setOnClickListener {
			sortByPrice()
		}
	}
	
	//価格ソート
	private fun sortByPrice(){
		//kotlin Collectionの拡張関数
		//昇順ソート  降順はsortedByDescending
//※1		val sortList = sampleItems.sortedBy { it.price }
//※1		sampleItems.clear()
//※1		sampleItems.addAll(sortList) //ListはJavaと同じく参照渡し,innerClassを使うとここらへんが親の変数だけ参照すればいいので面倒がすくなくなったりする
		
		sampleItems2 = sampleItems2.sortedBy { it.price }.toMutableList()
		
		//notifyDataSetChanged()リスト全体更新するのであまり推奨されない
		//更新場所が明確の場合はより狭い範囲で更新をかけるのがベストだが...全体ソートなのでやむを得ない
		fruitItemAdapter.notifyDataSetChanged()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	//※1　Kotlin:クラス内クラスではinnerクラスにすると便利("親クラスの変数やメソッドが可視化できる!"）
//	private inner class ListItemAdapter(val listItems : List<FruitData>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
	
	
		private inner class ListItemAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
		//以下の3つのメソッド実装が強要される(onCreateViewHolder, getItemCount, onBindViewHolder)
		//ViewHolderを返す実装
		override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RecyclerView.ViewHolder {
			val inflater = LayoutInflater.from(parent.context)
			val itemBinding = ListItemFruitListBinding.inflate(inflater, parent, false)
			return ListItemViewHolder(itemBinding)
			
			//標準実装ではこんな感じ
		//　return ListItemViewHolder(inflater.inflate(R.layout.list_item_fruit_list, parent,  false))
		
		}
		
		//Viewに表示するアイテムの個数（リストの長さ）を返す実装
		override fun getItemCount() : Int = sampleItems2.size
//※1		override fun getItemCount() : Int = listItems.size
		
		
		//ViewHolderの中にあるアイテム（個別のアイテム）に対して何かしら操作する実装
		override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position : Int) {
			//positionが”List表示にいるViewの位置”（普通のリストと同じで0から始まる）
			//つまりデータのリストと一致しているのでこのView表示内部のデータはlist[position]と同一となる
			val data = sampleItems2[position]
//※1			val data = listItems[position]
			
			when(holder){
				is ListItemViewHolder -> {
					holder.onBind(data)
				}
			}
		}
		
		//アイテムに対するViewHolderにViewを渡しておく
		
		private inner class ListItemViewHolder(val itemBinding : ListItemFruitListBinding):RecyclerView.ViewHolder(itemBinding.root){
		//private inner class ListItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)
			//ここにメソッド作ってもonBindViewHolder内に書いてもどっちでもいい
			//たくさんViewHolderを作るなら内部メソッドの方がわかりやすいかも
			fun onBind(data :FruitData){
				itemBinding.id.text = String.format(requireContext().getString(R.string.item_id), data.id)
				itemBinding.name.text = String.format(requireContext().getString(R.string.item_name), data.name)
				itemBinding.price.text = String.format(requireContext().getString(R.string.item_price), data.price)
			}
		}
		
	}
}