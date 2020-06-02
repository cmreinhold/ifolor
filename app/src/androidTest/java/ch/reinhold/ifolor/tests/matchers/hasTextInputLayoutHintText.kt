package ch.reinhold.ifolor.tests.matchers

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasTextInputError(expectedError: String): Matcher<View> = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {}

    override fun matchesSafely(item: View?): Boolean = (item as? TextInputLayout)
        ?.error?.toString()?.run {
            expectedError == this
        } ?: false
}
