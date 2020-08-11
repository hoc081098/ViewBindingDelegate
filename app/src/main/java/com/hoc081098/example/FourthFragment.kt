package com.hoc081098.example

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.hoc081098.example.databinding.FragmentFourthBinding
import com.hoc081098.viewbindingdelegate.viewBinding
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

class FourthVM : ViewModel() {
  private val _liveData = MutableLiveData<String>().apply { value = "0s" }

  private val start = SystemClock.uptimeMillis()
  private val timer = timer(period = 1_000) {
    val s = TimeUnit.MILLISECONDS.toSeconds(SystemClock.uptimeMillis() - start)
    _liveData.postValue("${s}s")
  }

  override fun onCleared() {
    super.onCleared()
    timer.cancel()
  }

  val liveData get() = _liveData as LiveData<String>
}

class FourthFragment : Fragment(R.layout.fragment_fourth) {
  private val vm by viewModels<FourthVM>()
  private val binding by viewBinding<FragmentFourthBinding>()

  private var cachedView: View? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ) = cachedView ?: super.onCreateView(inflater, container, savedInstanceState)!!
    .also { cachedView = it }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Log.d(TAG, "FourthFragment::onViewCreated ${binding.root}")

    vm.liveData.observe(viewLifecycleOwner) {
      binding.textView.text = it
      Log.d(TAG, "FourthFragment:: $it")
    }
    binding.button2.setOnClickListener {
      findNavController().navigate(R.id.actionFourthFragmentToFirstFragment)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.button2.setOnClickListener(null)

    Log.d(TAG, "FourthFragment::onDestroyView ${binding.root}")
  }

  private companion object {
    const val TAG = "FourthFragment"
  }
}
