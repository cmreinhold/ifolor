package ch.reinhold.ifolor.ui.registration

import ch.reinhold.ifolor.test.TestCoroutineDispatcherRule
import ch.reinhold.ifolor.ui.actions.GoToConfirmationAction
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegistrationViewModelTest {

    @Rule
    @JvmField
    val rule = TestCoroutineDispatcherRule()

    private val underTest = RegistrationViewModel()

    @Test
    fun emitsGoToConfirmationActionGivenRegisterButtonClicked() = runBlockingTest {
        underTest.actions.observeForever { }

        underTest.getOnRegisterClick().onClick(mock())
        assertThat(underTest.actions.value).isInstanceOf(GoToConfirmationAction::class.java)
    }
}
