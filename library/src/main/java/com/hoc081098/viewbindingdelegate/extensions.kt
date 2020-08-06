package com.hoc081098.viewbindingdelegate

import android.app.Activity
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

@MainThread
fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): FragmentViewBindingDelegate<T> =
  FragmentViewBindingDelegate(
    fragment = this,
    viewBindingBind = bind
  )

@MainThread
inline fun <reified T : ViewBinding> Fragment.viewBinding(): FragmentViewBindingDelegate<T> =
  FragmentViewBindingDelegate(
    fragment = this,
    viewBindingClazz = T::class.java
  )

@Suppress("unused")
@MainThread
fun <T : ViewBinding> Activity.viewBinding(bind: (View) -> T): ActivityViewBindingDelegate<T> =
  ActivityViewBindingDelegate(viewBindingBind = bind)

@Suppress("unused")
@MainThread
inline fun <reified T : ViewBinding> Activity.viewBinding(): ActivityViewBindingDelegate<T> =
  ActivityViewBindingDelegate(viewBindingClazz = T::class.java)