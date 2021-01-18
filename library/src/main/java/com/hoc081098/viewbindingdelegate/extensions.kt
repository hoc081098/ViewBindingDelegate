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
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hoc081098.viewbindingdelegate.impl.ActivityViewBindingDelegate
import com.hoc081098.viewbindingdelegate.impl.DialogFragmentViewBindingDelegate
import com.hoc081098.viewbindingdelegate.impl.FragmentViewBindingDelegate

/**
 * Create [ViewBinding] property delegate for this [Fragment].
 *
 * @param bind a lambda function that creates a [ViewBinding] instance from [Fragment]'s root view, eg: `T::bind` static method can be used.
 */
@MainThread
public fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): FragmentViewBindingDelegate<T> =
  FragmentViewBindingDelegate.from(
    fragment = this,
    viewBindingBind = bind
  )

/**
 * Create [ViewBinding] property delegate for this [Fragment].
 */
@MainThread
public inline fun <reified T : ViewBinding> Fragment.viewBinding(): FragmentViewBindingDelegate<T> =
  FragmentViewBindingDelegate.from(
    fragment = this,
    viewBindingClazz = T::class.java
  )

/**
 * Create [ViewBinding] property delegate for this [Activity].
 * @param bind a lambda function that creates a [ViewBinding] instance from [Activity]'s contentView, eg: `T::bind` static method can be used.
 */
@Suppress("unused")
@MainThread
public fun <T : ViewBinding> Activity.viewBinding(bind: (View) -> T): ActivityViewBindingDelegate<T> =
  ActivityViewBindingDelegate.from(viewBindingBind = bind)

/**
 * Create [ViewBinding] property delegate for this [Activity].
 */
@Suppress("unused")
@MainThread
public inline fun <reified T : ViewBinding> Activity.viewBinding(): ActivityViewBindingDelegate<T> =
  ActivityViewBindingDelegate.from(viewBindingClazz = T::class.java)

/**
 * Create [ViewBinding] property delegate for the [Dialog] of this [DialogFragment].
 *
 * @param bind a lambda function that creates a [ViewBinding] instance from root view of the [Dialog] of this [DialogFragment],
 *        eg: `T::bind` static method can be used.
 */
@MainThread
public fun <DF, T : ViewBinding> DF.dialogFragmentViewBinding(
  @IdRes rootId: Int,
  bind: (View) -> T
): DialogFragmentViewBindingDelegate<T, DF> where DF : DialogFragment, DF : ViewBindingDialogFragment {
  return DialogFragmentViewBindingDelegate.from(
    fragment = this,
    viewBindingBind = bind,
    rootId = rootId,
  )
}

/**
 * Create [ViewBinding] property delegate for the [Dialog] of this [DialogFragment].
 */
@MainThread
public inline fun <DF, reified T : ViewBinding> DF.dialogFragmentViewBinding(
  @IdRes rootId: Int
): DialogFragmentViewBindingDelegate<T, DF> where DF : DialogFragment, DF : ViewBindingDialogFragment {
  return DialogFragmentViewBindingDelegate.from(
    fragment = this,
    viewBindingClazz = T::class.java,
    rootId = rootId,
  )
}

/**
 * Create [ViewBinding] property delegate for the [Dialog] of this [DefaultViewBindingDialogFragment].
 */
@MainThread
public inline fun <reified T : ViewBinding> DefaultViewBindingDialogFragment.dialogFragmentViewBinding(
  @IdRes rootId: Int
): DialogFragmentViewBindingDelegate<T, DefaultViewBindingDialogFragment> =
  dialogFragmentViewBinding<DefaultViewBindingDialogFragment, T>(rootId)

public inline infix fun <reified T : ViewBinding> ViewGroup.inflateViewBinding(attachToParent: Boolean): T =
  LayoutInflater.from(context).inflateViewBinding(this, attachToParent)

public inline fun <reified T : ViewBinding> LayoutInflater.inflateViewBinding(
  parent: ViewGroup? = null,
  attachToParent: Boolean = parent != null
): T {
  val method = GetInflateMethod(T::class.java)
  return if (method.parameterTypes.size == 3) {
    method.invoke(
      null,
      this,
      parent,
      attachToParent,
    ) as T
  } else {
    check(attachToParent) { "attachToRoot is always true for ${T::class.java.simpleName}.inflate" }

    method.invoke(
      null,
      this,
      checkNotNull(parent) { "parent must be not null for ${T::class.java.simpleName}.inflate" },
    ) as T
  }

}

public inline fun <reified T : ViewBinding> Context.inflateViewBinding(
  parent: ViewGroup? = null,
  attachToParent: Boolean = parent != null
): T = LayoutInflater.from(this).inflateViewBinding(parent, attachToParent)
