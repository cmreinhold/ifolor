package ch.reinhold.ifolor.uicore.binding

import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import ch.reinhold.ifolor.core.logger.obtainLogger
import java.util.concurrent.atomic.AtomicInteger

object EditTextDataBindingAdapter {

    private val logger = obtainLogger("EditTextDataBindingAdapter")

    @JvmStatic
    @BindingAdapter("onFocus")
    fun onFocusChanged(editText: EditText, focusChangeListener: View.OnFocusChangeListener?) {
        val times = AtomicInteger()
        editText.onFocusChangeListener = focusChangeListener
        editText.doOnTextChanged { text, _, _, count ->
            if (times.incrementAndGet() > 1 || text != null) {
                logger.debug("OnTextChanged -> txt: $text, times: $times, count: $count")
                focusChangeListener?.onFocusChange(editText, false)
            }
        }
    }
}
