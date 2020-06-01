package com.google.android.material.datepicker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual.equalTo

object MaterialDatePickerTestUtils {

    fun clickDay(timeInMillis: Long, day: Int) {
        clickDay(Month.create(timeInMillis), day)
    }

    fun clickOk() {
        onView(withTagValue(equalTo(MaterialDatePicker.CONFIRM_BUTTON_TAG)))
            .perform(click())
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    }

    fun clickCancel() {
        onView(withTagValue(equalTo(MaterialDatePicker.CANCEL_BUTTON_TAG)))
            .perform(click())
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    }

    fun clickPrev() {
        onView(withTagValue(equalTo(MaterialCalendar.NAVIGATION_PREV_TAG)))
            .perform(click())
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    }

    private fun clickDay(month: Month, day: Int) {
        onView(
            Matchers.allOf(
                isDescendantOfA(
                    withTagValue(
                        equalTo(
                            MaterialCalendar.MONTHS_VIEW_GROUP_TAG
                        )
                    )
                ),
                withTagValue(equalTo(month)),
                withText(day.toString())
            )
        ).perform(click())
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    }

}
