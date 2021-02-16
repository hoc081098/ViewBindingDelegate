package com.hoc081098.example

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.hoc081098.example.databinding.ActivityThirdBinding
import com.hoc081098.example.databinding.FragmentFirstBinding
import com.hoc081098.example.databinding.FragmentSecondBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class ThirdActivity : AppCompatActivity(R.layout.activity_third) {
  private val binding by viewBinding(ActivityThirdBinding::bind)

  private lateinit var fragmentA: FragmentA

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (savedInstanceState === null) {
      fragmentA = FragmentA()

      supportFragmentManager.commit {
        setReorderingAllowed(true)
        add(R.id.container, fragmentA, "FragmentA")
      }
    } else {
      fragmentA = supportFragmentManager.findFragmentByTag("FragmentA") as FragmentA
    }

    binding.buttonClick.setOnClickListener {
      val found = supportFragmentManager.findFragmentByTag("FragmentB") as? FragmentB

      supportFragmentManager.commit {
        if (found != null) remove(found)
        add(R.id.container, FragmentB(), "FragmentB")
        addToBackStack("FragmentB")
      }
    }
  }
}

class FragmentA : Fragment(R.layout.fragment_first) {
  private val binding by viewBinding<FragmentFirstBinding>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Log.d("FragmentManager", "FragmentManager ${binding.root} $this")
  }

  override fun onResume() {
    super.onResume()
    binding
  }

  override fun onDestroyView() {
    super.onDestroyView()
    Toast.makeText(requireContext(), "A Destroy", Toast.LENGTH_SHORT).show()
  }
}

class FragmentB : Fragment(R.layout.fragment_second) {
  private val binding by viewBinding<FragmentSecondBinding>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().supportFragmentManager.popBackStack()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Log.d("FragmentManager", "FragmentManager ${binding.root} $this")
  }

  override fun onResume() {
    super.onResume()
    binding
  }

  override fun onDestroyView() {
    super.onDestroyView()
    Toast.makeText(requireContext(), "B Destroy", Toast.LENGTH_SHORT).show()
  }
}
