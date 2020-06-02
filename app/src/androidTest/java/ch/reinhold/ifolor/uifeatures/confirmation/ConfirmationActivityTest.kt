package ch.reinhold.ifolor.uifeatures.confirmation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import ch.reinhold.ifolor.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ConfirmationActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(
        ConfirmationActivity::class.java,
        false,
        false
    )

    private val context by lazy {
        InstrumentationRegistry.getInstrumentation().targetContext
    }

    private val activity get() = activityRule.activity

    private val registrationConfirmed by lazy { activity.getString(R.string.registrationConfirmed) }

    @Test
    fun launchesActivity() {
        activityRule.launchActivity(null)
    }

    @Test
    fun displaysConfirmationTitle() {
        activityRule.launchActivity(null)
        onView(withId(R.id.confirmation)).check(matches(withText(registrationConfirmed)))
    }
}
