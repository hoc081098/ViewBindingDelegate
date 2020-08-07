package com.hoc081098.viewbindingdelegate

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<T : ViewBinding>(
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
      ?: bind((thisRef.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0))
        .also { binding = it }
  }
}
