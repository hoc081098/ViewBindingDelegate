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

@file:Suppress("NOTHING_TO_INLINE")

package com.hoc081098.viewbindingdelegate.internal

import android.os.Looper
import android.util.Log
import kotlin.system.measureNanoTime

@PublishedApi
internal fun ensureMainThread(): Unit = check(Looper.getMainLooper() == Looper.myLooper()) {
  "Expected to be called on the main thread but was " + Thread.currentThread().name
}

// TODO(release): set `DEBUG` to `false` when publishing.
private const val DEBUG = false

internal inline fun log(crossinline message: () -> String) {
  if (DEBUG) {
    Log.d("ViewBindingDelegate", message())
  }
}

internal inline fun <T> measureNanoTime(tag: String, crossinline block: () -> T): T =
  if (DEBUG) {
    val t: T
    val time = measureNanoTime { t = block() }
    log { "$tag taken time=$time ns ~ ${time / 1_000_000.0} ms" }
    t
  } else {
    block()
  }
