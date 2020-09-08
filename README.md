# ViewBindingDelegate

## Author: [Petrus Nguyễn Thái Học](https://github.com/hoc081098)

- Simplify usage of Android View Binding with Kotlin Property Delegates and solve behavior of Fragment’s ViewLifecycleOwner
- Simple one-liner ViewBinding in Fragments and Activities with Kotlin

![Android CI](https://github.com/hoc081098/ViewBindingDelegate/workflows/Android%20CI/badge.svg)
[![Jitpack](https://jitpack.io/v/hoc081098/ViewBindingDelegate.svg)](https://jitpack.io/#hoc081098/ViewBindingDelegate)
[![GitHub](https://img.shields.io/github/license/hoc081098/ViewBindingDelegate?color=4EB1BA)](https://opensource.org/licenses/MIT)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

## Medium article

Read [ViewBinding Delegate — one line](https://medium.com/@hoc081098/viewbinding-delegate-one-line-4d0cdcbf53ba) to get details about implementation.

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

## Activity (with reflection)
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

## Activity (without reflection): Pass `::bind` method reference
```kotlin
class SecondActivity : AppCompatActivity(R.layout.activity_second) {
  private val binding by viewBinding(ActivitySecondBinding::bind)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.root
  }
}
```

## Fragment (with reflection)
```kotlin
class FirstFragment : Fragment(R.layout.fragment_first) {
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

## Fragment (without reflection): Pass `::bind` method reference
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

## Includes `<merge/>` tag layout: Create 2 `ViewBinding` property

```kotlin
class ThirdFragment : Fragment(R.layout.fragment_third) {
  private val includeBinding by viewBinding<FragmentThirdIncludeBinding>()
  private val binding by viewBinding<FragmentThirdBinding>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    includeBinding.textViewThirdInclude.text = "Working..."
    binding.buttonThird.setOnClickListener {
      Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.buttonThird.setOnClickListener(null)
  }
}
```


# Note

## Acitivty
  Must `setContentView` before access `ViewBinding` property. This can be done easily with `constructor`:
  ```java
  public AppCompatActivity(@LayoutRes int contentLayoutId) { ... }
  ```
  ```kotlin
  class MainActivity : AppCompatActivity(R.layout.activity_main) { ... }
  ```

## Fragment
  `Fragment`'s `View` must be not null before access `ViewBinding` property. This can be done easily with `constructor`:
  ```java
  public Fragment(@LayoutRes int contentLayoutId) { ... }
  ```
  ```kotlin
  class FirstFragment : Fragment(R.layout.fragment_first) { ... }
  ```
