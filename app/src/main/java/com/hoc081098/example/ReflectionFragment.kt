package com.hoc081098.example

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hoc081098.example.databinding.FragmentFirstBinding
import com.hoc081098.viewbindingdelegate.viewBinding
import java.util.concurrent.TimeUnit
import kotlin.math.pow

class ReflectionFragment : Fragment(R.layout.fragment_first), Runnable {
  private val binding by viewBinding<FragmentFirstBinding>()

  private val start = SystemClock.uptimeMillis()
  private val handler = Handler(Looper.getMainLooper())

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.button.setOnClickListener {
      findNavController().navigate(R.id.actionFirstFragmentToSecondFragment)
    }

    handler.post(this)
  }

  override fun run() {
    binding.textView.text =
      "${TimeUnit.MILLISECONDS.toSeconds(SystemClock.uptimeMillis() - start)} s"
    handler.postDelayed(this, 1_000)
  }

  override fun onDestroyView() {
    super.onDestroyView()

    repeat(5_000) { println(it.toDouble().pow(2)) }

    handler.removeCallbacks(this)
    binding.button.setOnClickListener(null)

    Log.d("FragmentViewBinding", "onDestroyView done")
  }
}
