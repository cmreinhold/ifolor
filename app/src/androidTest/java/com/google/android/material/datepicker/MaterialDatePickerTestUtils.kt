package com.google.android.material.datepicker

import android.util.Log
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual.equalTo
import org.threeten.bp.ZonedDateTime

object MaterialDatePickerTestUtils {

    fun openCalendarPicker(viewMatcher: Matcher<View>) {
        onView(viewMatcher).perform(click())
    }

    fun selectCalendarDate(viewMatcher: Matcher<View>, now: ZonedDateTime, date: ZonedDateTime) {
        openCalendarPicker(viewMatcher)
        navigateToCalendarMonth(now, date)
        selectCalendarDay(date)
        clickOk()
    }

    private fun selectCalendarDay(dateTime: ZonedDateTime, dayOfMonth: Int = 1) {
        val dateInMillis = dateTime.toInstant().toEpochMilli()
        clickDay(dateInMillis, dayOfMonth)
    }

    private fun navigateToCalendarMonth(now: ZonedDateTime, targetDateTime: ZonedDateTime) {
        val monthsToYear = 12 * (now.year - targetDateTime.year)
        val monthsInYear = now.monthValue - targetDateTime.monthValue
        val monthsCount = monthsToYear + monthsInYear
        Log.d("Click", "Clicking $monthsCount times on previous month")

        repeat(monthsCount) {
            clickPrev()
        }
    }

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
