package com.hoc081098.example

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hoc081098.example.databinding.FragmentThirdBinding
import com.hoc081098.example.databinding.FragmentThirdIncludeBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class Reflection2Fragment : Fragment(R.layout.fragment_third) {
  private val includeBinding by viewBinding<FragmentThirdIncludeBinding>()
  private val binding by viewBinding<FragmentThirdBinding>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    includeBinding.textViewThirdInclude.text = "Working..."
    binding.buttonThird.setOnClickListener {
      findNavController().navigate(R.id.actionThirdFragmentToFourthFragment)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.buttonThird.setOnClickListener(null)
  }
}
