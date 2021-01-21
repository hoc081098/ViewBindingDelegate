package com.hoc081098.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoc081098.example.databinding.ActivitySecondBinding
import com.hoc081098.example.databinding.ActivityThirdBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class ThirdActivity : AppCompatActivity(R.layout.activity_third) {
  private val binding by viewBinding(ActivityThirdBinding::bind)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding
  }
}
