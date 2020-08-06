package com.hoc081098.viewbindingdelegate_example

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hoc081098.viewbindingdelegate.viewBinding
import com.hoc081098.viewbindingdelegate_example.databinding.FragmentSecondBinding

class SecondFragment : Fragment(R.layout.fragment_second) {
  private val binding by viewBinding(FragmentSecondBinding::bind)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.root
  }
}