package ch.reinhold.ifolor

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import ch.reinhold.ifolor.ui.registration.RegistrationActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
        onView(withId(R.id.usernameTextField)).check(matches(hasErrorText(invalidName)))
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
        onView(withId(R.id.emailTextField)).check(matches(hasErrorText(invalidEmail)))
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

        // To lose focus
        onView(withId(R.id.registerButton)).perform(click())

        // Checks enabled button state and clicks it
        onView(withId(R.id.registerButton)).apply {
            check(matches(not(isEnabled())))
            perform(click())
        }
    }
}
