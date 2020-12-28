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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

public interface VBDialogFragment {
  public val viewLiveData: LiveData<Listeners>

  public class Listeners {
    private var isDisposed = false
    private val listeners = mutableSetOf<() -> Unit>()

    public fun add(listener: () -> Unit) {
      check(!isDisposed) { "Already disposed" }
      listeners.add(listener)
    }

    public operator fun invoke() {
      check(!isDisposed) { "Already disposed" }
      log { "Listeners::invoke ${listeners.size}" }
      listeners.forEach { it() }
    }

    public fun dispose() {
      check(!isDisposed) { "Already disposed" }
      log { "Listeners::dispose ${listeners.size}" }
      listeners.clear()
      isDisposed = true
    }
  }
}

public open class DefaultVBDialogFragment : DialogFragment(), VBDialogFragment {
  private lateinit var listeners: VBDialogFragment.Listeners
  private val viewMutableLiveData = MutableLiveData<VBDialogFragment.Listeners>()

  override val viewLiveData: LiveData<VBDialogFragment.Listeners> get() = viewMutableLiveData

  @CallSuper
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewMutableLiveData.value = VBDialogFragment.Listeners().also { listeners = it }
    return null
  }

  @CallSuper
  override fun onDestroyView() {
    super.onDestroyView()

    listeners()
    listeners.dispose()
  }
}