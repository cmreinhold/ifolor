package ch.reinhold.ifolor.ui.registration

import android.content.Context
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.domain.validators.Validator
import ch.reinhold.ifolor.test.TestCoroutineDispatcherRule
import ch.reinhold.ifolor.ui.actions.GoToConfirmationAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
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

    private val context: Context = mock {
        on(mock.getString(any())).thenAnswer { it.getArgument<Int>(0).toString() }
    }

    private val nameValidator: Validator<String> = mock()
    private val emailValidator: Validator<String> = mock()

    private val underTest = RegistrationViewModel(
        context,
        nameValidator,
        emailValidator
    )

    @Test
    fun emitsGoToConfirmationActionGivenRegisterButtonClicked() = runBlockingTest {
        underTest.actions.observeForever { }

        underTest.getOnRegisterClick().onClick(mock())

        assertThat(underTest.actions.value).isInstanceOf(GoToConfirmationAction::class.java)
    }

    @Test
    fun validatesNameWithErrorAndButtonDisabledGivenFocusLostAndNameIsEmpty() = runBlockingTest {
        val name = ""
        underTest.actions.observeForever { }

        whenever(nameValidator.isValid(any()))
            .thenAnswer { !it.getArgument<String>(0).isNullOrBlank() }

        underTest.name.set(name)
        underTest.getOnNameFocus().onFocusChange(mock(), false)

        assertThat(underTest.actions.value).isNull()
        assertThat(underTest.name.get()).isEqualTo(name)
        assertThat(underTest.nameError.get()).isEqualTo(R.string.invalid_name.toString())
        assertThat(underTest.isButtonEnabled.get()).isEqualTo(false)
    }

    @Test
    fun validatesNameAndButtonEnabledGivenFocusLostAndNameIsNotEmpty() = runBlockingTest {
        val name = "ABC"
        underTest.actions.observeForever { }

        whenever(nameValidator.isValid(any()))
            .thenAnswer { !it.getArgument<String>(0).isNullOrBlank() }

        underTest.name.set(name)
        underTest.getOnNameFocus().onFocusChange(mock(), false)

        assertThat(underTest.actions.value).isNull()
        assertThat(underTest.name.get()).isEqualTo(name)
        assertThat(underTest.nameError.get()).isNull()
        assertThat(underTest.isButtonEnabled.get()).isEqualTo(true)
    }

    @Test
    fun doesNothingGivenOnFocusChangeIsTrue() = runBlockingTest {
        underTest.actions.observeForever { }

        underTest.getOnNameFocus().onFocusChange(mock(), true)

        assertThat(underTest.actions.value).isNull()
        assertThat(underTest.name.get()).isNull()
        assertThat(underTest.nameError.get()).isNull()
        assertThat(underTest.isButtonEnabled.get()).isEqualTo(false)
    }
}
