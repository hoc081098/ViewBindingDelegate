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

package com.hoc081098.example

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.hoc081098.example.databinding.DialogFragmentDemoBinding
import com.hoc081098.viewbindingdelegate.DefaultVBDialogFragment
import com.hoc081098.viewbindingdelegate.dialogFragmentViewBinding

class DemoDialogFragment : DefaultVBDialogFragment() {
  private val viewBinding by dialogFragmentViewBinding(R.id.root, DialogFragmentDemoBinding::bind)
  private val viewBinding2 by dialogFragmentViewBinding<DialogFragmentDemoBinding, DefaultVBDialogFragment>(
    R.id.root
  )

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return AlertDialog.Builder(requireContext())
      .setTitle("Demo dialog")
      .setNegativeButton("Cancel") { _, _ -> }
      .setPositiveButton("OK") { _, _ -> }
      .setView(R.layout.dialog_fragment_demo)
      .create()
  }

  override fun onResume() {
    super.onResume()

    Log.d("###", viewBinding.toString())
    Log.d("###", viewBinding.textInputLayout.toString())

    Log.d("###", viewBinding2.toString())
    Log.d("###", viewBinding2.textInputLayout.toString())
  }

  override fun onDestroyView() {
    super.onDestroyView()

    Log.d("###", "onDestroyView$viewBinding")
    Log.d("###", "onDestroyView${viewBinding.textInputLayout}")

    Log.d("###", "onDestroyView$viewBinding2")
    Log.d("###", "onDestroyView${viewBinding2.textInputLayout}")
  }
}
