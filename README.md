# ViewBindingDelegate
- Simplify usage of Android View Binding with Kotlin Property Delegates and solve behavior of Fragment’s ViewLifecycleOwner
- Simple one-liner ViewBinding in Fragments and Activities with Kotlin

[![](https://jitpack.io/v/hoc081098/ViewBindingDelegate.svg)](https://jitpack.io/#hoc081098/ViewBindingDelegate)

# Getting Started

## 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

## 2. Add the dependency

```gradle
dependencies {
  implementation 'com.github.hoc081098:ViewBindingDelegate:<latest_version>'
}
```

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
