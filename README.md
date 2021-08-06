# ViewBindingDelegate

## Author: [Petrus Nguyễn Thái Học](https://github.com/hoc081098)

- Simplify usage of Android View Binding with Kotlin Property Delegates and solve behavior of Fragment’s ViewLifecycleOwner
- Simple one-liner ViewBinding in Fragments and Activities with Kotlin

![Month](https://jitpack.io/v/hoc081098/ViewBindingDelegate/month.svg)
![Week](https://jitpack.io/v/hoc081098/ViewBindingDelegate/week.svg)
![Android Tests](https://github.com/hoc081098/ViewBindingDelegate/workflows/Android%20Tests/badge.svg)
![Android CI](https://github.com/hoc081098/ViewBindingDelegate/workflows/Android%20CI/badge.svg)
![Build APK](https://github.com/hoc081098/ViewBindingDelegate/workflows/Build%20debug%20APK/badge.svg)
[![Jitpack](https://jitpack.io/v/hoc081098/ViewBindingDelegate.svg)](https://jitpack.io/#hoc081098/ViewBindingDelegate)
[![GitHub](https://img.shields.io/github/license/hoc081098/ViewBindingDelegate?color=4EB1BA)](https://opensource.org/licenses/MIT)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

## Medium article

Read [ViewBinding Delegate — one line](https://hoc081098.medium.com/viewbinding-delegate-one-line-4d0cdcbf53ba) to get details about implementation.

# Getting Started

## 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

- Kotlin
```kotlin
allprojects {
  repositories {
    ...
    maven(url = "https://jitpack.io")
  }
}
```

- Groovy
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

## 2. Add the dependency

- Kotlin
```kotlin
dependencies {
  implementation("com.github.hoc081098:ViewBindingDelegate:1.1.0")
}
```

- Groovy
```gradle
dependencies {
  implementation 'com.github.hoc081098:ViewBindingDelegate:1.1.0'
}
```

# Usage

```kotlin
import com.hoc081098.viewbindingdelegate.*
```

### 1. Activity (with reflection). [See example](https://github.com/hoc081098/ViewBindingDelegate/blob/master/app/src/main/java/com/hoc081098/example/MainActivity.kt)

<details>
  <summary>Click to expand</summary>

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

</details>

### 2. Activity (without reflection): Pass `::bind` method reference. [See example](https://github.com/hoc081098/ViewBindingDelegate/blob/master/app/src/main/java/com/hoc081098/example/SecondActivity.kt)

<details>

  <summary>Click to expand</summary>

```kotlin
class SecondActivity : AppCompatActivity(R.layout.activity_second) {
  private val binding by viewBinding(ActivitySecondBinding::bind)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.root
  }
}
```

</details>

### 3. Fragment (with reflection). [See example](https://github.com/hoc081098/ViewBindingDelegate/blob/master/app/src/main/java/com/hoc081098/example/ReflectionFragment.kt)

<details>

  <summary>Click to expand</summary>

```kotlin
class FirstFragment : Fragment(R.layout.fragment_first) {
  private val binding by viewBinding<FragmentFirstBinding> {
    button.setOnClickListener(null)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.button.setOnClickListener {
      findNavController().navigate(R.id.actionFirstFragmentToSecondFragment)
    }
  }
}
```

</details>

### 4. Fragment (without reflection): Pass `::bind` method reference. [See example](https://github.com/hoc081098/ViewBindingDelegate/blob/master/app/src/main/java/com/hoc081098/example/NotReflectionFragment.kt)

<details>

  <summary>Click to expand</summary>

```kotlin
class SecondFragment : Fragment(R.layout.fragment_second) {
  private val binding by viewBinding(FragmentSecondBinding::bind)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.root
  }
}
```

</details>

### 5. Includes `<merge/>` tag layout: Create 2 `ViewBinding` property. [See example](https://github.com/hoc081098/ViewBindingDelegate/blob/master/app/src/main/java/com/hoc081098/example/Reflection2Fragment.kt)

<details>

  <summary>Click to expand</summary>


```kotlin
class ThirdFragment : Fragment(R.layout.fragment_third) {
  private val includeBinding by viewBinding<FragmentThirdIncludeBinding>()
  private val binding by viewBinding<FragmentThirdBinding> { buttonThird.setOnClickListener(null) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    includeBinding.textViewThirdInclude.text = "Working..."
    binding.buttonThird.setOnClickListener {
      Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }
  }
}
```

</details>

### 6. The `Dialog` of `DialogFragment`[See example](https://github.com/hoc081098/ViewBindingDelegate/blob/master/app/src/main/java/com/hoc081098/example/DemoDialogFragment.kt)

Extends `DefaultViewBindingDialogFragment` or implements `ViewBindingDialogFragment`. 
  
<details>

  <summary>Click to expand</summary>

```kotlin
class DemoDialogFragment : DefaultViewBindingDialogFragment() {
  private val viewBinding by dialogFragmentViewBinding(R.id.root, DialogFragmentDemoBinding::bind)
  private val viewBinding2 by dialogFragmentViewBinding<DialogFragmentDemoBinding>(R.id.root)

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return AlertDialog.Builder(requireContext())
      .setTitle("Demo dialog")
      .setNegativeButton("Cancel") { _, _ -> }
      .setPositiveButton("OK") { _, _ -> }
      .setView(R.layout.dialog_fragment_demo)
      .create()
  }

  override fun onResume() {
    super.onResume()

    viewBinding.textInputLayout
    viewBinding2.textInputLayout
  }
}
```

</details>

# Note

### 1. Activity
  Must `setContentView` before access `ViewBinding` property. This can be done easily with `constructor`:
  ```java
  public AppCompatActivity(@LayoutRes int contentLayoutId) { ... }
  ```
  ```kotlin
  class MainActivity : AppCompatActivity(R.layout.activity_main) { ... }
  ```

### 2. Fragment
  `Fragment`'s `View` must be not null before access `ViewBinding` property. This can be done easily with `constructor`:
  ```java
  public Fragment(@LayoutRes int contentLayoutId) { ... }
  ```
  ```kotlin
  class FirstFragment : Fragment(R.layout.fragment_first) { ... }
  ```
  
### 3. Proguard
If there is any problem with `Proguard`, add below to your `app/proguard-rules.pro`:
```
# ViewBindingDelegate uses Reflection.
-keepclassmembers class ** implements androidx.viewbinding.ViewBinding {
    public static ** bind(android.view.View);

    public static ** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);

    public static ** inflate(android.view.LayoutInflater, android.view.ViewGroup);
}
```

### 4. Throws `IllegalStateException`: "Attempt to get view binding when fragment view is destroyed" when accessing delegate property in `onDestroyView`

Since version `1.0.0-alpha03 - Feb 16, 2021`, we cannot access ViewBinding delegate property in `onDestroyView` (this causes many problems). Recommended way is passing a lambda to `onDestroyView: (T.() -> Unit)? = null` parameter of extension functions, eg.

```diff
- private val binding by viewBinding<FragmentFirstBinding>()

+ private val binding by viewBinding<FragmentFirstBinding> { /*this: FragmentFirstBinding*/
+   button.setOnClickListener(null)
+   recyclerView.adapter = null
+ }
 
  override fun onDestroyView() {
    super.onDestroyView()
-   binding.button.setOnClickListener(null)
-   binding.recyclerView.adapter = null
  }
```

### 5. Min SDK version

Since version `1.2.0`, `minSdkVersion` has been changed to `14`.

# License

    MIT License

    Copyright (c) 2020-2021 Petrus Nguyễn Thái Học
