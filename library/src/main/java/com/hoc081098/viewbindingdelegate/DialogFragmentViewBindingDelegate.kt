/*
 * MIT License
 *
 * Copyright (c) 2020 Petrus Nguyễn Thái Học
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

package com.hoc081098.viewbindingdelegate

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import java.security.spec.PKCS8EncodedKeySpec
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public class DialogFragmentViewBindingDelegate<T : ViewBinding> private constructor(
  private val fragment: LifecycleDialogFragment,
  @IdRes private val rootId: Int,
  viewBindingBind: ((View) -> T)? = null,
  viewBindingClazz: Class<T>? = null
) :
  ReadOnlyProperty<DialogFragment, T> {
  private var binding: T? = null
  private val bind = viewBindingBind ?: { view: View ->
    @Suppress("UNCHECKED_CAST")
    GetBindMethod(viewBindingClazz!!)(null, view) as T
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
      ?: bind(thisRef.requireDialog().findViewById(rootId)!!)
        .also { binding = it }
  }

  private inner class FragmentLifecycleObserver : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
      fragment.onDestroyViewLiveData.observe(fragment) {
        it ?: return@observe
        log { "$fragment::onDestroyView" }

        MainHandler.post {
          binding = null
          log { "MainHandler.post { binding = null }" }
        }
      }

      log { "$fragment::onCreate" }
    }

    override fun onDestroy(owner: LifecycleOwner) {
      fragment.lifecycle.removeObserver(this)
      binding = null

      log { "$fragment::onDestroy" }
    }
  }

  public companion object Factory {
    /**
     * Create [DialogFragmentViewBindingDelegate] from [viewBindingBind] lambda function.
     *
     * @param fragment the [DialogFragment] which owns this delegated property.
     * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from Dialog root view in [DialogFragment], eg: `T::bind` static method can be used.
     */
    public fun <T : ViewBinding> from(
      fragment: LifecycleDialogFragment,
      @IdRes rootId: Int,
      viewBindingBind: (View) -> T
    ): DialogFragmentViewBindingDelegate<T> = DialogFragmentViewBindingDelegate(
      fragment = fragment,
      viewBindingBind = viewBindingBind,
      rootId = rootId,
    )

    /**
     * Create [DialogFragmentViewBindingDelegate] from [ViewBinding] class.
     *
     * @param fragment the [DialogFragment] which owns this delegated property.
     * @param viewBindingClazz Kotlin Reflection will be used to get `T::bind` static method from this class.
     */
    public fun <T : ViewBinding> from(
      fragment: LifecycleDialogFragment,
      @IdRes rootId: Int,
      viewBindingClazz: Class<T>
    ): DialogFragmentViewBindingDelegate<T> = DialogFragmentViewBindingDelegate(
      fragment = fragment,
      viewBindingClazz = viewBindingClazz,
      rootId = rootId,
    )
  }
}