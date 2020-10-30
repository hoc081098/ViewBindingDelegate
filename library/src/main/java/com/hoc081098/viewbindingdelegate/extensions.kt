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
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

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
