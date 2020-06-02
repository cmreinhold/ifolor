package ch.reinhold.ifolor.ui.registration

import android.content.Context
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.data.db.IfolorDao
import ch.reinhold.ifolor.data.db.entities.UserEntity
import ch.reinhold.ifolor.domain.validators.Validator
import ch.reinhold.ifolor.ui.actions.GoToConfirmationAction
import ch.reinhold.ifolor.ui.actions.ShowDatePickerAction
import ch.reinhold.ifolor.ui.actions.ViewModelAction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * [ViewModel] that observes events from the ui via databinding on the layout and updates its
 * ui accordingly, handling editText validations and button clicks from the registration screen.
 *
 * For navigation actions or popup dialogs it uses the actions [MutableLiveData] object to
 * communicate with the [RegistrationActivity].
 */
class RegistrationViewModel(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher,
    private val uiDispatcher: CoroutineDispatcher,
    private val ifolorDao: IfolorDao,
    private val nameValidator: Validator<String>,
    private val emailValidator: Validator<String>,
    private val birthdayValidator: Validator<Long>
) : ViewModel() {

    val actions = MutableLiveData<ViewModelAction>()

    private val isValidName get() = nameValidator.isValid(name.get())
    private val isValidEmail get() = emailValidator.isValid(email.get())
    private val isValidBirthday get() = birthdayValidator.isValid(date.get())

    private val nameErrorMessage by lazy { context.getString(R.string.invalid_name) }
    private val emailErrorMessage by lazy { context.getString(R.string.invalid_email) }
    private val birthdayErrorMessage by lazy { context.getString(R.string.invalid_birthday) }

    private fun emit(action: ViewModelAction) = actions.postValue(action)

    private fun emitAsync(action: ViewModelAction) = viewModelScope.launch {
        emit(action)
    }

    val name = ObservableField<String>()
    val nameError = ObservableField<String>()
    private val onFocusName = View.OnFocusChangeListener { _, focused ->
        if (!focused) {
            validateName()
            validateButton()
        }
    }

    fun getOnNameFocus() = onFocusName

    val email = ObservableField<String>()
    val emailError = ObservableField<String>()
    private val onFocusEmail = View.OnFocusChangeListener { _, focused ->
        if (!focused) {
            validateEmail()
            validateButton()
        }
    }

    fun getOnEmailFocus() = onFocusEmail

    val formattedDate = ObservableField<String>()
    val dateError = ObservableField<String>()
    private val date = ObservableField<Long>()
    val onClickDate = View.OnClickListener { _ ->
        emit(ShowDatePickerAction(date.get()))
    }

    fun setBirthday(selectedDate: Long, selectedDateFormatted: String) {
        date.set(selectedDate)
        formattedDate.set(selectedDateFormatted)

        validateBirthday()
        validateButton()
    }

    val isButtonEnabled = ObservableBoolean()
    private val onRegisterButtonClick = View.OnClickListener {
        handleRegistration()
    }

    fun getOnRegisterClick() = onRegisterButtonClick

    private fun validateName() {
        if (isValidName) nameError.set(null)
        else nameError.set(nameErrorMessage)
    }

    private fun validateEmail() {
        if (isValidEmail) emailError.set(null)
        else emailError.set(emailErrorMessage)
    }

    private fun validateBirthday() {
        if (isValidBirthday) dateError.set(null)
        else dateError.set(birthdayErrorMessage)
    }

    private fun validateButton() {
        isButtonEnabled.set(isValidName && isValidEmail && isValidBirthday)
    }

    private fun handleRegistration() = viewModelScope.launch(ioDispatcher) {
        val user = UserEntity(
            name = name.get()!!,
            email = email.get()!!,
            birthday = date.get()!!
        )
        ifolorDao.insertUser(user)

        withContext(uiDispatcher) {
            emit(GoToConfirmationAction())
        }
    }
}
