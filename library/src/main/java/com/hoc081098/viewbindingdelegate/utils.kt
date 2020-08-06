package com.hoc081098.viewbindingdelegate

import android.os.Handler
import android.os.Looper
import android.util.Log

internal object MainHandler {
  private val handler = Handler(Looper.getMainLooper())

  fun post(action: () -> Unit): Boolean = handler.post(action)
}

@PublishedApi
internal fun ensureMainThread() = check(Looper.getMainLooper() == Looper.myLooper()) {
  "Expected to be called on the main thread but was " + Thread.currentThread().name
}

private const val debug = true

internal inline fun log(crossinline message: () -> String) {
  if (debug) {
    Log.d("ViewBinding", message())
  }
}