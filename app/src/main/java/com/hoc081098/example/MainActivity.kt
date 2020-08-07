package com.hoc081098.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.hoc081098.example.databinding.ActivityMainBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
  private lateinit var appBarConfiguration: AppBarConfiguration
  private val viewBinding by viewBinding<ActivityMainBinding>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController
    appBarConfiguration = AppBarConfiguration(navController.graph)
    setupActionBarWithNavController(
      navController,
      appBarConfiguration
    )

    viewBinding.button.setOnClickListener {
      startActivity(Intent(this@MainActivity, SecondActivity::class.java))
    }
  }

  override fun onSupportNavigateUp(): Boolean =
    findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}
