package com.hoc081098.viewbindingdelegate_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoc081098.viewbindingdelegate.viewBinding
import com.hoc081098.viewbindingdelegate_example.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity(R.layout.activity_second) {
  private val binding by viewBinding(ActivitySecondBinding::bind)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.root
  }
}