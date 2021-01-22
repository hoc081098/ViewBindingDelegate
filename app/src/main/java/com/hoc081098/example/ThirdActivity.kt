package com.hoc081098.example

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoc081098.example.databinding.ActivitySecondBinding
import com.hoc081098.example.databinding.ActivityThirdBinding
import com.hoc081098.example.databinding.FragmentFirstBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class ThirdActivity : AppCompatActivity(R.layout.activity_third) {
  private val binding by viewBinding(ActivityThirdBinding::bind)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.buttonClick.setOnClickListener {
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        add<FragmentA>(R.id.container)
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
}

class FragmentB : Fragment(R.layout.fragment_second) {

}