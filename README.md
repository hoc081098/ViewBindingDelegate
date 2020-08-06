# ViewBindingDelegate
- Simplify usage of Android View Binding with Kotlin Property Delegates and solve behavior of Fragment’s ViewLifecycleOwner
- Simple one-liner ViewBinding in Fragments and Activities with Kotlin

# Usage

```kotlin
import com.hoc081098.viewbindingdelegate.viewBinding
```

```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main) {
  private val viewBinding by viewBinding<ActivityMainBinding>()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    viewBinding.button.setOnClickListener {
      startActivity(Intent(this@MainActivity, SecondActivity::class.java))
    }
  }
}
```

```kotlin
class SecondActivity : AppCompatActivity(R.layout.activity_second) {
  private val binding by viewBinding(ActivitySecondBinding::bind)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.root
  }
}
```

```kotlin
class FirstFragment : Fragment(R.layout.fragment_first), Runnable {
  private val binding by viewBinding<FragmentFirstBinding>()
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.button.setOnClickListener {
      findNavController().navigate(R.id.actionFirstFragmentToSecondFragment)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.button.setOnClickListener(null)
  }
```

```kotlin
class SecondFragment : Fragment(R.layout.fragment_second) {
  private val binding by viewBinding(FragmentSecondBinding::bind)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.root
  }
}
```
