package ch.reinhold.ifolor.uifeatures.registration

import android.content.Context
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.data.db.IfolorDao
import ch.reinhold.ifolor.domain.validators.Validator
import ch.reinhold.ifolor.test.TestCoroutineDispatcherRule
import ch.reinhold.ifolor.uicore.actions.navigation.GoToConfirmationAction
import ch.reinhold.ifolor.uicore.actions.ShowDatePickerAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

@ExperimentalCoroutinesApi
class RegistrationViewModelTest {

    @Rule
    @JvmField
    val rule = TestCoroutineDispatcherRule()

    private val localDate = LocalDate.of(2001, 12, 30)
        .atStartOfDay(ZoneOffset.UTC)

    private val context: Context = mock {
        on(mock.getString(any())).thenAnswer { it.getArgument<Int>(0).toString() }
    }

    private val nameValidator: Validator<String> = mock()
    private val emailValidator: Validator<String> = mock()
    private val birthdayValidator: Validator<Long> = mock()
    private val userDao: IfolorDao = mock()

    private val testDispatcher = TestCoroutineDispatcher()

    private val underTest = RegistrationViewModel(
        context,
        testDispatcher,
        testDispatcher,
        userDao,
        nameValidator,
        emailValidator,
        birthdayValidator
    )

    @Test
    fun emitsGoToConfirmationActionGivenRegisterButtonClicked() = runBlockingTest {
        underTest.actions.observeForever { }

        underTest.getOnRegisterClick().onClick(mock())

        assertThat(underTest.actions.value).isInstanceOf(GoToConfirmationAction::class.java)
    }

    @Test
    fun emitsShowDatePickerActionWithNoDateGivenBirthdayFieldClicked() = runBlockingTest {
        underTest.actions.observeForever { }

        underTest.onClickDate.onClick(mock())

        val tested = underTest.actions.value as? ShowDatePickerAction
        assertThat(tested!!.initialDate).isNull()
    }

    @Test
    fun emitsShowDatePickerActionWithDateGivenBirthdayFieldWithDateClicked() = runBlockingTest {
        val date = localDate.toInstant().toEpochMilli()
        val formattedText = "$date"
        underTest.setBirthday(date, formattedText)
        underTest.actions.observeForever { }

        underTest.onClickDate.onClick(mock())

        val tested = underTest.actions.value as? ShowDatePickerAction
        assertThat(tested!!.initialDate).isEqualTo(date)
    }

    @Test
    fun setsBirthdayDateAndFormattedText() = runBlockingTest {
        whenever(birthdayValidator.isValid(anyOrNull())).thenReturn(true)
        val date = localDate.toInstant().toEpochMilli()
        val formattedText = "$date"
        underTest.setBirthday(date, formattedText)

        assertThat(underTest.dateError.get()).isNull()
        assertThat(underTest.formattedDate.get()).isEqualTo(formattedText)
    }

    @Test
    fun setsBirthdayAndFormattedTextWithErrorGivenInvalidDate() = runBlockingTest {
        whenever(birthdayValidator.isValid(anyOrNull())).thenReturn(false)
        val date = localDate.toInstant().toEpochMilli()
        val formattedText = "$date"
        underTest.setBirthday(date, formattedText)

        assertThat(underTest.dateError.get()).isEqualTo(R.string.invalid_birthday.toString())
        assertThat(underTest.formattedDate.get()).isEqualTo(formattedText)
    }

    @Test
    fun validatesRegistrationButtonGivenAllFieldsAreValid() = runBlockingTest {
        underTest.actions.observeForever { }

        whenever(nameValidator.isValid(anyOrNull())).thenReturn(true)
        whenever(emailValidator.isValid(anyOrNull())).thenReturn(true)
        whenever(birthdayValidator.isValid(anyOrNull())).thenReturn(true)

        val date = localDate.toInstant().toEpochMilli()
        val formattedText = "$date"
        underTest.setBirthday(date, formattedText)

        assertThat(underTest.actions.value).isNull()
        assertThat(underTest.dateError.get()).isNull()
        assertThat(underTest.formattedDate.get()).isEqualTo(formattedText)
        assertThat(underTest.isButtonEnabled.get()).isEqualTo(true)
    }

    @Test
    fun validatesNameWithErrorAndButtonDisabledGivenFocusLostAndNameIsEmpty() = runBlockingTest {
        val name = ""
        underTest.actions.observeForever { }

        whenever(nameValidator.isValid(anyOrNull()))
            .thenAnswer { !it.getArgument<String>(0).isNullOrBlank() }

        underTest.name.set(name)
        underTest.getOnNameFocus().onFocusChange(mock(), false)

        assertThat(underTest.actions.value).isNull()
        assertThat(underTest.name.get()).isEqualTo(name)
        assertThat(underTest.nameError.get()).isEqualTo(R.string.invalid_name.toString())
        assertThat(underTest.isButtonEnabled.get()).isEqualTo(false)
    }

    @Test
    fun validatesEmailWithErrorAndButtonDisabledGivenFocusLostAndNameIsEmpty() = runBlockingTest {
        val email = "abc"
        underTest.actions.observeForever { }

        whenever(emailValidator.isValid(anyOrNull())).thenReturn(false)

        underTest.email.set(email)
        underTest.getOnEmailFocus().onFocusChange(mock(), false)

        assertThat(underTest.actions.value).isNull()
        assertThat(underTest.email.get()).isEqualTo(email)
        assertThat(underTest.emailError.get()).isEqualTo(R.string.invalid_email.toString())
        assertThat(underTest.isButtonEnabled.get()).isEqualTo(false)
    }

    @Test
    fun validatesNameAndButtonDisabledGivenFocusLostAndNameIsNotEmpty() = runBlockingTest {
        val name = "ABC"
        underTest.actions.observeForever { }

        whenever(nameValidator.isValid(anyOrNull()))
            .thenAnswer { !it.getArgument<String>(0).isNullOrBlank() }

        underTest.name.set(name)
        underTest.getOnNameFocus().onFocusChange(mock(), false)

        assertThat(underTest.actions.value).isNull()
        assertThat(underTest.name.get()).isEqualTo(name)
        assertThat(underTest.nameError.get()).isNull()
        assertThat(underTest.isButtonEnabled.get()).isEqualTo(false)
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
