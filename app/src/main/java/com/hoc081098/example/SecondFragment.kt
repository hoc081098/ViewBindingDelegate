package com.hoc081098.example

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hoc081098.example.databinding.FragmentSecondBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class SecondFragment : Fragment(R.layout.fragment_second) {
  private val binding by viewBinding(FragmentSecondBinding::bind)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.buttonToThird.setOnClickListener {
      findNavController().navigate(R.id.actionSecondFragmentToThirdFragment)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.buttonToThird.setOnClickListener(null)
  }
}
