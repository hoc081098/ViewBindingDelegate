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

import androidx.annotation.MainThread
import androidx.collection.SimpleArrayMap
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import kotlin.LazyThreadSafetyMode.NONE

@MainThread
internal sealed interface MethodCache {
  fun <T : ViewBinding> getOrPut(clazz: Class<T>): Method

  fun putAll(pairs: List<Pair<Class<out ViewBinding>, Method>>)

  fun <T : ViewBinding> Class<T>.findMethod(): Method
}

@MainThread
private abstract class AbstractMethodCache : MethodCache {
  private val cache = SimpleArrayMap<Class<out ViewBinding>, Method>()

  init {
    ensureMainThread()
  }

  override fun <T : ViewBinding> getOrPut(clazz: Class<T>) =
    cache[clazz] ?: clazz.findMethod().also { cache.put(clazz, it) }

  override fun putAll(pairs: List<Pair<Class<out ViewBinding>, Method>>) =
    pairs.forEach { (k, v) -> cache.put(k, v) }
}

private class BindMethodCache : AbstractMethodCache() {
  override fun <T : ViewBinding> Class<T>.findMethod() =
    measureTimeMillis("[findBindMethod]") { findBindMethod() }
}

private class InflateMethodCache : AbstractMethodCache() {
  override fun <T : ViewBinding> Class<T>.findMethod() =
    measureTimeMillis("[findInflateMethod]") { findInflateMethod() }
}

@MainThread
internal object CacheContainer {
  private val bindMethodCache by lazy(NONE) {
    ensureMainThread()
    log { "[CacheContainer] bindMethodCache created" }

    BindMethodCache()
  }
  private val inflateMethodCache by lazy(NONE) {
    ensureMainThread()
    log { "[CacheContainer] inflateMethodCache created" }

    InflateMethodCache()
  }

  @MainThread
  internal fun provideBindMethodCache(): MethodCache = bindMethodCache

  @MainThread
  internal fun provideInflateMethodCache(): MethodCache = inflateMethodCache
}
