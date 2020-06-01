package ch.reinhold.ifolor.ui.binding

import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter

object EditTextDataBindingAdapter {
    @JvmStatic
    @BindingAdapter("onFocus")
    fun onFocusChanged(editText: EditText, focusChangeListener: View.OnFocusChangeListener?) {
        editText.onFocusChangeListener = focusChangeListener
        editText.doOnTextChanged { _, _, _, _ ->
            focusChangeListener?.onFocusChange(editText, false)
        }
    }
}
