package com.hoc081098.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoc081098.example.databinding.ActivitySecondBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class SecondActivity : AppCompatActivity(R.layout.activity_second) {
  private val binding by viewBinding(ActivitySecondBinding::bind)
  private val demoAdapter = DemoAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.recyclerView.run {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(this@SecondActivity)
      adapter = demoAdapter
    }

    demoAdapter.submitList((0..1_000).map(Int::toString))
  }
}
