package ch.reinhold.ifolor

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import ch.reinhold.ifolor.ui.registration.RegistrationActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
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

}
