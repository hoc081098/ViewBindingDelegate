## 1.3.0 - Dec 24, 2021

-   Change `targetSdkVersion` and `compileSdkVersion` to 31.

-   Updated dependencies
    -   Kotlin to 1.6.10
    -   AGP to 7.0.4
    -   Gradle to 7.3.3

-   Others
    -   Update Github workflows.
    -   Tracking API compatibility.

## 1.2.0 - Aug 6, 2021

-   Change `minSdkVersion` to `14`.

## 1.1.0 - Aug 1, 2021

-   Updated dependencies:
    -   Kotlin to 1.5.21
    -   AGP to 7.0.0
    -   Gradle to 7.1.1

## 1.0.0 - May 7, 2021

-   This is our first stable release.

-   Updated dependencies:
    -   Kotlin to 1.5.0.
    -   AGP to 4.2.

## 1.0.0-beta02 - Apr 20, 2021

-   Downgrade Gradle to 6.8.3 (JitPack.io cannot build with Gradle 7.0)

## 1.0.0-beta01 - Apr 19, 2021

-   Update docs.

-   Updated dependencies:
    -   Kotlin to 1.4.32.
    -   Gradle to 7.0.
    -   AGP to 4.1.3.

## 1.0.0-alpha03 - Feb 16, 2021
-   **Breaking change**: Now, cannot access ViewBinding delegate property in `onDestroyView` (this causes many problems).
    Recommended way is passing a lambda to `onDestroyView: (T.() -> Unit)? = null` parameter of extension functions, eg.

    ```kotlin
    private val binding by viewBinding<FragmentFirstBinding>() { /*this: FragmentFirstBinding*/
      button.setOnClickListener(null)
      recyclerView.adapter = null
    }
    ```
-   New feature: add inflating extensions
    -   `ViewGroup.inflateViewBinding(attachToParent: Boolean)`.
    -   `LayoutInflater.inflateViewBinding(parent: ViewGroup? = null, attachToParent: Boolean = parent != null)`.
    -   `Context.inflateViewBinding(parent: ViewGroup? = null, attachToParent: Boolean = parent != null)`.
-   Updated dependencies:
    -   Gradle to 6.8.2.
    -   Android Gradle plugin to 4.1.2.

## 1.0.0-alpha02 - Jan 1, 2021
-   New features:
    -   Add `dialogFragmentViewBinding`, `DialogFragmentViewBindingDelegate`: supports for the `Dialog` of `DialogFragment`.
-   Updated dependencies:
    -   Gradle to 6.7.1.
    -   Android Gradle plugin to 4.1.1.
    -   Android build tool to 30.0.3.
    -   Kotlin to 1.4.21.

## 1.0.0-alpha01 - Sep 10, 2020

*   Hotfix

## 1.0.0-alpha - Sep 10, 2020

*   Update to Kotlin 1.4.0, enable explicit API mode.
*   Cache `java.lang.reflect.Method`, update `consumer-rules.pro`.
*   Update README.md, documentation, example.

## 0.0.2 - Aug 7, 2020

*   Update README.md.
*   Update example.

## 0.0.1 - Aug 7, 2020

*   Publish.
