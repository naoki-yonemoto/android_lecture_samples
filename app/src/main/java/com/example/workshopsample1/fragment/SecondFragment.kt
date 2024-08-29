package com.example.workshopsample1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.workshopsample1.R
import com.example.workshopsample1.databinding.FragmentScreenSecondBinding

class SecondFragment : Fragment() {
	
	private var _binding : FragmentScreenSecondBinding? = null
	private val binding get() = _binding!!
	
	companion object {
		private const val KEY_INPUT_TEXT = "keyInput"
		
		fun newInstance(inputText : String) : SecondFragment {
			//			val fragment = SecondFragment()
			//			val bundle = Bundle()
			//			bundle.putString(KEY_INPUT_TEXT, inputText)
			//			fragment.arguments = bundle
			//			return fragment
			
			//スコープ関数を使うとこんな感じ
			return SecondFragment().apply {
				arguments = Bundle().apply {
					putString(KEY_INPUT_TEXT, inputText)
				}
			}
		}
	}
	
	
	override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
		_binding = FragmentScreenSecondBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		//セットされたargument（データ）はここに入っている
		arguments?.let { args ->
			//取り出す
			args.getString(KEY_INPUT_TEXT)?.let {
				binding.setValue.text = it
				binding.setValue.setTextColor(ResourcesCompat.getColor(resources, R.color.orange, null))
			}
		}
		
		binding.finishBtn.setOnClickListener {
			backToFirstFragment()
		}
	}
	
	private fun backToFirstFragment() {
		val returnText = binding.resultText.text.toString()
		
		setFragmentResult(
			FirstFragment.KEY_RESULT,
			bundleOf(FirstFragment.KEY_RETURN_TEXT to returnText)
		)
		
		parentFragmentManager.popBackStack()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
}