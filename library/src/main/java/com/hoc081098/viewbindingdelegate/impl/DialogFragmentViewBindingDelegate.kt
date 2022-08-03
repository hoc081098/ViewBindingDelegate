/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Petrus Nguyễn Thái Học
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hoc081098.viewbindingdelegate.impl

import android.app.Dialog
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.hoc081098.viewbindingdelegate.ViewBindingDialogFragment
import com.hoc081098.viewbindingdelegate.ViewBindingDialogFragment.OnDestroyViewListeners
import com.hoc081098.viewbindingdelegate.internal.CacheContainer
import com.hoc081098.viewbindingdelegate.internal.ensureMainThread
import com.hoc081098.viewbindingdelegate.internal.log
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Used to implement [ViewBinding] property delegate for the [Dialog] of [DialogFragment].
 *
 * @param fragment the [DialogFragment].
 * @param rootId the root id of custom view of the [Dialog].
 * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from root view of the [Dialog] eg: `T::bind` static method can be used.
 * @param viewBindingClazz if viewBindingBind is not provided, Kotlin Reflection will be used to get `T::bind` static method.
 */
public class DialogFragmentViewBindingDelegate<T : ViewBinding, DF> private constructor(
  private val fragment: DF,
  @IdRes private val rootId: Int,
  viewBindingBind: ((View) -> T)? = null,
  viewBindingClazz: Class<T>? = null,
  private var onDestroyView: (T.() -> Unit)?
) : ReadOnlyProperty<DialogFragment, T> where DF : DialogFragment, DF : ViewBindingDialogFragment {

  private var binding: T? = null
  private val bind = viewBindingBind ?: { view: View ->
    @Suppress("UNCHECKED_CAST")
    CacheContainer
      .provideBindMethodCache()
      .getOrPut(viewBindingClazz!!)(null, view) as T
  }

  init {
    ensureMainThread()
    require(viewBindingBind != null || viewBindingClazz != null) {
      "Both viewBindingBind and viewBindingClazz are null. Please provide at least one."
    }

    fragment.lifecycle.addObserver(FragmentLifecycleObserver())
  }

  override fun getValue(thisRef: DialogFragment, property: KProperty<*>): T {
    return binding
      ?: thisRef.requireDialog().let { dialog ->
        bind(
          checkNotNull(dialog.findViewById(rootId)) {
            "ID $rootId does not reference a View inside this Dialog $dialog"
          }
        ).also { binding = it }
      }
  }

  private inner class FragmentLifecycleObserver : DefaultLifecycleObserver {
    val observer = Observer<OnDestroyViewListeners?> { listeners: OnDestroyViewListeners? ->
      listeners ?: return@Observer

      var onDestroyViewActual = onDestroyView
      listeners += {
        binding?.let { onDestroyViewActual?.invoke(it) }
        onDestroyViewActual = null
        binding = null

        log { "[DialogFragmentViewBindingDelegate] onDestroyView::$fragment" }
      }
    }

    override fun onCreate(owner: LifecycleOwner) {
      fragment.onDestroyViewLiveData.observeForever(observer)

      log { "[DialogFragmentViewBindingDelegate] onCreate::$fragment" }
    }

    override fun onDestroy(owner: LifecycleOwner) {
      fragment.lifecycle.removeObserver(this)
      fragment.onDestroyViewLiveData.removeObserver(observer)

      binding = null
      onDestroyView = null

      log { "[DialogFragmentViewBindingDelegate] onDestroy::$fragment" }
    }
  }

  public companion object Factory {
    /**
     * Create [DialogFragmentViewBindingDelegate] from [viewBindingBind] lambda function.
     *
     * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from root view of the [Dialog] eg: `T::bind` static method can be used.
     */
    @JvmStatic
    public fun <T : ViewBinding, DF> from(
      fragment: DF,
      @IdRes rootId: Int,
      viewBindingBind: (View) -> T,
      onDestroyView: (T.() -> Unit)?
    ): DialogFragmentViewBindingDelegate<T, DF> where DF : DialogFragment, DF : ViewBindingDialogFragment =
      DialogFragmentViewBindingDelegate(
        fragment = fragment,
        viewBindingBind = viewBindingBind,
        rootId = rootId,
        onDestroyView = onDestroyView
      )

    /**
     * Create [DialogFragmentViewBindingDelegate] from [viewBindingClazz] class.
     *
     * @param viewBindingClazz Kotlin Reflection will be used to get `T::bind` static method from this class.
     */
    @JvmStatic
    public fun <T : ViewBinding, DF> from(
      fragment: DF,
      @IdRes rootId: Int,
      viewBindingClazz: Class<T>,
      onDestroyView: (T.() -> Unit)?
    ): DialogFragmentViewBindingDelegate<T, DF> where DF : DialogFragment, DF : ViewBindingDialogFragment =
      DialogFragmentViewBindingDelegate(
        fragment = fragment,
        viewBindingClazz = viewBindingClazz,
        rootId = rootId,
        onDestroyView = onDestroyView
      )
  }
}
