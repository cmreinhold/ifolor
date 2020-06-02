package ch.reinhold.ifolor.ui.extensions.datepicker

import androidx.annotation.StringRes
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker

fun buildDatePicker(
    initialDate: Long? = null,
    @StringRes titleResId: Int? = null,
    builder: MaterialDatePicker<Long>.() -> Unit
): MaterialDatePicker<Long> {
    val constraintsBuilder = CalendarConstraints.Builder()
    initialDate?.let { constraintsBuilder.setOpenAt(it) }

    val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
    initialDate?.let { datePickerBuilder.setSelection(it) }

    titleResId?.let { datePickerBuilder.setTitleText(titleResId) }
    datePickerBuilder.setCalendarConstraints(constraintsBuilder.build())

    val datePicker = datePickerBuilder.build()
    builder(datePicker)
    return datePicker
}
