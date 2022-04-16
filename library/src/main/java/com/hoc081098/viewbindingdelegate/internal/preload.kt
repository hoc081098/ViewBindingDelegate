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

package com.hoc081098.viewbindingdelegate.internal

import android.content.Context
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.reflect.KClass

internal object PreloadMethods {
  private val ioExecutor by lazy(NONE) {
    ensureMainThread()
    log { "[PreloadMethods] ioExecutor created" }

    ThreadPoolExecutor(
      0,
      1,
      30L,
      TimeUnit.SECONDS,
      LinkedBlockingQueue(),
      ThreadFactory {
        Thread(it).apply {
          name = "com_hoc081098_viewbinding_disk_io"
        }
      }
    )
  }

  @MainThread
  internal inline fun Context.preload(
    tag: String,
    classes: Array<out KClass<out ViewBinding>>,
    crossinline func: Class<out ViewBinding>.() -> Method,
    crossinline onSuccess: (List<Pair<Class<out ViewBinding>, Method>>) -> Unit
  ) {
    if (classes.isEmpty()) return

    val mainExecutor = ContextCompat.getMainExecutor(applicationContext)
    ioExecutor.execute {
      log { "$tag start... classes=${classes.map { it.simpleName }}" }

      val methods = measureTimeMillis(tag) {
        classes.map {
          val clazz = it.java
          clazz to clazz.func()
        }
      }

      mainExecutor.execute {
        onSuccess(methods)
        log { "$tag done" }
      }
    }
  }
}
