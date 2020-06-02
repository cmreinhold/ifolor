package ch.reinhold.ifolor.uifeatures.registration

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.tests.matchers.hasTextInputError
import com.google.android.material.datepicker.MaterialDatePickerTestUtils
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegistrationActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(
        RegistrationActivity::class.java,
        false,
        false
    )

    private val activity get() = activityRule.activity

    private val registerLabel by lazy { activity.getString(R.string.registerAction) }
    private val invalidName by lazy { activity.getString(R.string.invalid_name) }
    private val invalidEmail by lazy { activity.getString(R.string.invalid_email) }
    private val invalidBirthday by lazy { activity.getString(R.string.invalid_birthday) }

    private val maxDate = LocalDate.of(2019, 12, 31)
        .atStartOfDay(ZoneOffset.UTC)

    private val now = ZonedDateTime.now(ZoneOffset.UTC)

    private val dateWithinRange = maxDate.minusMonths(1)
    private val dateAfterRange = now.minusMonths(1)

    @Test
    fun activityLaunches() {
        activityRule.launchActivity(null)
    }

    @Test
    fun initialStateShowsLabelsWithNoTextFilledAndButtonDisabled() {
        activityRule.launchActivity(null)

        onView(withId(R.id.registerButton)).apply {
            check(matches(withText(registerLabel)))
            check(matches(not(isEnabled())))
        }
    }

    @Test
    fun validatesWithErrorAfterNameFieldLosesFocus() {
        val dummyName = ""
        activityRule.launchActivity(null)

        // Fills name field with empty value
        onView(withId(R.id.usernameTextField))
            .perform(click(), replaceText(dummyName), closeSoftKeyboard())

        // To lose focus
        onView(withId(R.id.registerButton)).perform(click())

        // Checks enabled button state and clicks it
        onView(withId(R.id.registerButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.username)).check(matches(hasTextInputError(invalidName)))
    }

    @Test
    fun validatesWithErrorAfterEmailFieldLosesFocus() {
        val dummyEmail = ""
        activityRule.launchActivity(null)

        // Fills name field with empty value
        onView(withId(R.id.emailTextField))
            .perform(click(), replaceText(dummyEmail), closeSoftKeyboard())

        // To lose focus
        onView(withId(R.id.registerButton)).perform(click())

        // Checks enabled button state and clicks it
        onView(withId(R.id.registerButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.email)).check(matches(hasTextInputError(invalidEmail)))
    }

    @Test
    fun validatesWithoutErrorAfterCalendarSetBefore2019() {
        activityRule.launchActivity(null)

        selectValidCalendarDate()

        // Checks date is filled
        onView(withId(R.id.birthdayTextField)).check(matches(not(withText(""))))
        onView(withId(R.id.birthday)).check(matches(not(hasTextInputError(invalidBirthday))))
    }

    @Test
    fun validatesWithErrorAfterCalendarSetAfter2019() {
        activityRule.launchActivity(null)

        selectInvalidCalendarDate()

        // Checks date is filled
        onView(withId(R.id.birthdayTextField)).check(matches(not(withText(""))))
        onView(withId(R.id.birthday)).check(matches(hasTextInputError(invalidBirthday)))
    }

    fun validatesNothingWhenCancellingDatePicker() {
        activityRule.launchActivity(null)

        openCalendarPicker()
        dismissCalendarPicker()

        // Checks date is filled
        onView(withId(R.id.birthdayTextField)).check(matches(withText("")))
        onView(withId(R.id.birthday)).check(matches(not(hasTextInputError(invalidBirthday))))
    }

    @Test
    fun registerButtonGetsEnabledGivenAllFieldsAreValid() {
        val dummyName = "Christian Reinhold"
        val dummyEmail = "christian.reinhold@ifolor.com"

        activityRule.launchActivity(null)

        // Fills name, email and birthday fields
        onView(withId(R.id.usernameTextField))
            .perform(click(), replaceText(dummyName))

        onView(withId(R.id.emailTextField))
            .perform(click(), replaceText(dummyEmail), closeSoftKeyboard())

        selectValidCalendarDate()

        // Checks enabled button state and clicks it
        onView(withId(R.id.registerButton)).apply {
            check(matches(isEnabled()))
            perform(click())
        }
    }

    private fun selectValidCalendarDate() {
        openCalendarPicker()
        navigateToCalendarMonth(dateWithinRange)
        selectCalendarDay(dateWithinRange)
    }

    private fun selectInvalidCalendarDate() {
        openCalendarPicker()
        navigateToCalendarMonth(dateAfterRange)
        selectCalendarDay(dateAfterRange)
    }

    private fun selectCalendarDay(dateTime: ZonedDateTime, dayOfMonth: Int = 1) {
        val dateInMillis = dateTime.toInstant().toEpochMilli()
        MaterialDatePickerTestUtils.clickDay(dateInMillis, dayOfMonth)
        MaterialDatePickerTestUtils.clickOk()
    }

    private fun navigateToCalendarMonth(targetDateTime: ZonedDateTime) {
        val monthsToYear = 12 * (now.year - targetDateTime.year)
        val monthsInYear = now.monthValue - targetDateTime.monthValue
        val monthsCount = monthsToYear + monthsInYear
        Log.d("Click", "Clicking $monthsCount times on previous month")

        repeat(monthsCount) {
            MaterialDatePickerTestUtils.clickPrev()
        }
    }

    private fun dismissCalendarPicker() {
        MaterialDatePickerTestUtils.clickCancel()
    }

    private fun openCalendarPicker() {
        onView(withId(R.id.birthdayTextField)).perform(click())
    }
}
