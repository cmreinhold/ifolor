package ch.reinhold.ifolor.uifeatures.registration

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.uifeatures.confirmation.ConfirmationActivity
import com.google.android.material.datepicker.MaterialDatePickerTestUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.ext.getFullName
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

@ExperimentalCoroutinesApi
@LargeTest
@RunWith(AndroidJUnit4::class)
class RegistrationIntentActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(
        RegistrationActivity::class.java,
        false,
        false
    )

    private val maxDate = LocalDate.of(2019, 12, 31)
        .atStartOfDay(ZoneOffset.UTC)

    private val now = ZonedDateTime.now(ZoneOffset.UTC)

    private val dateWithinRange = maxDate.minusMonths(1)

    @Test
    fun navigatesToConfirmationActivity_AfterRegisterButtonClicked_GivenAllFieldsValid() =
        runBlockingTest {
            val name = "Christian Reinhold"
            val email = "christian.reinhold@ifolor.com"

            intentsTestRule.launchActivity(null)

            // Fills name, email and birthday fields
            onView(withId(R.id.usernameTextField))
                .perform(click(), replaceText(name))

            onView(withId(R.id.emailTextField))
                .perform(
                    click(),
                    replaceText(email),
                    closeSoftKeyboard()
                )

            selectValidCalendarDate()

            // Checks enabled button state and clicks it
            onView(withId(R.id.registerButton)).apply {
                check(matches(isEnabled()))
                perform(click())
            }

            // Navigates to ConfirmationActivity
            intended(
                allOf(
                    hasComponent(ConfirmationActivity::class.getFullName()),
                    hasExtra(ConfirmationActivity.PARAM_EMAIL, email)
                )
            )
        }

    private fun selectValidCalendarDate() {
        MaterialDatePickerTestUtils.selectCalendarDate(
            withId(R.id.birthdayTextField),
            now,
            dateWithinRange
        )
    }
}
