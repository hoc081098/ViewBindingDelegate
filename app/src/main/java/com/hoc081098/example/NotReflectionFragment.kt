package com.hoc081098.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hoc081098.example.databinding.FragmentSecondBinding
import com.hoc081098.example.databinding.ItemRecyclerMergeBinding
import com.hoc081098.viewbindingdelegate.inflateViewBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class NotReflectionFragment : Fragment(R.layout.fragment_second) {
  private val binding by viewBinding(FragmentSecondBinding::bind)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.buttonToThird.setOnClickListener {
      findNavController().navigate(R.id.actionSecondFragmentToThirdFragment)
    }

    LayoutInflater.from(requireContext())
      .inflateViewBinding<ItemRecyclerMergeBinding>(requireView().rootView as ViewGroup)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.buttonToThird.setOnClickListener(null)
  }
}
