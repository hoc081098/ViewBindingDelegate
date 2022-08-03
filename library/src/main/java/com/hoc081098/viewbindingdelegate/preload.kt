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

package com.hoc081098.viewbindingdelegate

import androidx.annotation.MainThread
import androidx.viewbinding.ViewBinding
import com.hoc081098.viewbindingdelegate.internal.CacheContainer
import com.hoc081098.viewbindingdelegate.internal.PreloadMethods
import kotlin.reflect.KClass

@MainThread
public fun preloadBindMethods(vararg classes: KClass<out ViewBinding>): Unit =
  PreloadMethods.run {
    preload(
      tag = "[preloadBindMethods]",
      kClasses = classes,
      cache = CacheContainer.provideBindMethodCache()
    )
  }

@MainThread
public fun preloadInflateMethods(vararg classes: KClass<out ViewBinding>): Unit =
  PreloadMethods.run {
    preload(
      tag = "[preloadInflateMethods]",
      kClasses = classes,
      cache = CacheContainer.provideInflateMethodCache()
    )
  }
