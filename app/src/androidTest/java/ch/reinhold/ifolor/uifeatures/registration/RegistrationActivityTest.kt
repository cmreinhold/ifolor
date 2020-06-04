package ch.reinhold.ifolor.uifeatures.registration

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
    fun launchesActivity() {
        activityRule.launchActivity(null)
    }

    @Test
    fun showsInitialStateLabels_WithNoTextFilled_AndButtonDisabled() {
        activityRule.launchActivity(null)

        onView(withId(R.id.registerButton)).apply {
            check(matches(withText(registerLabel)))
            check(matches(not(isEnabled())))
        }
    }

    @Test
    fun validatesWithError_AfterNameFieldLosesFocus() {
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
    fun validatesNameProperly_AfterEachCharacterNameChange() {
        activityRule.launchActivity(null)

        onView(withId(R.id.username)).check(matches(not(hasTextInputError(invalidName))))

        onView(withId(R.id.usernameTextField)).perform(click(), replaceText("c"))
        onView(withId(R.id.username)).check(matches(not(hasTextInputError(invalidName))))

        onView(withId(R.id.usernameTextField)).perform(replaceText(""))
        onView(withId(R.id.username)).check(matches(hasTextInputError(invalidName)))

        onView(withId(R.id.usernameTextField)).perform(replaceText("Chris"))
        onView(withId(R.id.username)).check(matches(not(hasTextInputError(invalidName))))
    }

    @Test
    fun validatesWithError_AfterEmailFieldLosesFocus() {
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
    fun validatesWithoutError_AfterCalendarSetBefore2019() {
        activityRule.launchActivity(null)

        selectValidCalendarDate()

        // Checks date is filled
        onView(withId(R.id.birthdayTextField)).check(matches(not(withText(""))))
        onView(withId(R.id.birthday)).check(matches(not(hasTextInputError(invalidBirthday))))
    }

    @Test
    fun validatesWithError_AfterCalendarSetAfter2019() {
        activityRule.launchActivity(null)

        selectInvalidCalendarDate()

        // Checks date is filled
        onView(withId(R.id.birthdayTextField)).check(matches(not(withText(""))))
        onView(withId(R.id.birthday)).check(matches(hasTextInputError(invalidBirthday)))
    }

    fun validatesNothing_WhenCancellingDatePicker() {
        activityRule.launchActivity(null)

        openCalendarPicker()
        dismissCalendarPicker()

        // Checks date is filled
        onView(withId(R.id.birthdayTextField)).check(matches(withText("")))
        onView(withId(R.id.birthday)).check(matches(not(hasTextInputError(invalidBirthday))))
    }

    @Test
    fun enablesRegisterButtonGets_GivenAllFieldsValid() {
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

    private fun openCalendarPicker() {
        MaterialDatePickerTestUtils.openCalendarPicker(withId(R.id.birthdayTextField))
    }

    private fun dismissCalendarPicker() {
        MaterialDatePickerTestUtils.clickCancel()
    }

    private fun selectValidCalendarDate() {
        MaterialDatePickerTestUtils.selectCalendarDate(
            withId(R.id.birthdayTextField),
            now,
            dateWithinRange
        )
    }

    private fun selectInvalidCalendarDate() {
        MaterialDatePickerTestUtils.selectCalendarDate(
            withId(R.id.birthdayTextField),
            now,
            dateAfterRange
        )
    }
}
