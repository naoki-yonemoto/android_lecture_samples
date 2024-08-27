package com.example.workshopsample1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.workshopsample1.R
import com.example.workshopsample1.databinding.FragmentScreenMainBinding

class FirstFragment: Fragment() {
	
	private var _binding :FragmentScreenMainBinding? = null
	private val binding get() = _binding!!
	
	companion object {
		const val KEY_RESULT = "keyResult"
		const val KEY_RETURN_TEXT = "keyReturnText"
	}
	
	override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
		_binding = FragmentScreenMainBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		setFragmentResultListener(KEY_RESULT){ _, bundle ->
			val returnText = bundle.getString(KEY_RETURN_TEXT)
			returnText?.let {
				transitResultAction(it)
			}
		}
		
		binding.transitBtn.setOnClickListener {
			transitSecondFragment()
		}
	}
	
	private fun transitSecondFragment(){
		val inputText = binding.inputText.text.toString()
		
		val fragment = SecondFragment.newInstance(inputText)
		
		//fragmentのスタック管理はFragmentManagerが行っている
		parentFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, fragment) //遷移の仕方にはAddとReplaceがある
			.addToBackStack(null) //戻るときにスタック機能を使うか
			.commit()
	}
	
	private fun transitResultAction(returnText: String){
		binding.returnText.text = returnText
		binding.returnText.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}