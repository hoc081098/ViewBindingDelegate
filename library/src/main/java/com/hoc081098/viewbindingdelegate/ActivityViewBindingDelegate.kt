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
import androidx.viewbinding.ViewBinding
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Used to implement [ViewBinding] property delegate for [Activity].
 *
 * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from [Activity]'s contentView, eg: `T::bind` static method can be used.
 * @param viewBindingClazz if viewBindingBind is not provided, Kotlin Reflection will be used to get `T::bind` static method.
 */
public class ActivityViewBindingDelegate<T : ViewBinding> private constructor(
  viewBindingBind: ((View) -> T)? = null,
  viewBindingClazz: Class<T>? = null
) : ReadOnlyProperty<Activity, T> {

  private var binding: T? = null
  private val bind: (View) -> T

  init {
    ensureMainThread()
    require(viewBindingBind != null || viewBindingClazz != null) {
      "Both viewBindingBind and viewBindingClazz are null. Please provide at least one."
    }

    bind = viewBindingBind ?: run {
      val method by lazy(NONE) { viewBindingClazz!!.getMethod("bind", View::class.java) }

      @Suppress("UNCHECKED_CAST")
      fun(view: View): T = method.invoke(null, view) as T
    }
  }

  override fun getValue(thisRef: Activity, property: KProperty<*>): T {
    ensureMainThread()

    return binding
      ?: bind(thisRef.findViewById<ViewGroup>(android.R.id.content).getChildAt(0))
        .also { binding = it }
  }

  public companion object Factory {
    /**
     * Create [ActivityViewBindingDelegate] from [viewBindingBind] lambda function.
     *
     * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from [Activity]'s contentView, eg: `T::bind` static method can be used.
     */
    public fun <T : ViewBinding> from(viewBindingBind: (View) -> T): ActivityViewBindingDelegate<T> =
      ActivityViewBindingDelegate(viewBindingBind = viewBindingBind)

    /**
     * Create [ActivityViewBindingDelegate] from [ViewBinding] class.
     *
     * @param viewBindingClazz Kotlin Reflection will be used to get `T::bind` static method from this class.
     */
    public fun <T : ViewBinding> from(viewBindingClazz: Class<T>): ActivityViewBindingDelegate<T> =
      ActivityViewBindingDelegate(viewBindingClazz = viewBindingClazz)
  }
}
