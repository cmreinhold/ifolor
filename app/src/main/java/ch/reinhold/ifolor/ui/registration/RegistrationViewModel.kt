package ch.reinhold.ifolor.ui.registration

import android.content.Context
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.domain.validators.Validator
import ch.reinhold.ifolor.ui.actions.GoToConfirmationAction
import ch.reinhold.ifolor.ui.actions.ViewModelAction
import kotlinx.coroutines.launch

/**
 * [ViewModel] that observes events from the ui via databinding on the layout and updates its
 * ui accordingly, handling editText validations and button clicks from the registration screen.
 *
 * For navigation actions or popup dialogs it uses the actions [MutableLiveData] object to
 * communicate with the [RegistrationActivity].
 */
class RegistrationViewModel(
    context: Context,
    private val nameValidator: Validator<String>
) : ViewModel() {

    val actions = MutableLiveData<ViewModelAction>()

    private val isValidName get() = nameValidator.isValid(name.get())

    private val nameErrorMessage by lazy {  context.getString(R.string.invalid_name) }

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

    val isButtonEnabled = ObservableBoolean()
    private val onRegisterButtonClick = View.OnClickListener {
        emitAsync(GoToConfirmationAction())
    }

    fun getOnRegisterClick() = onRegisterButtonClick

    private fun validateName() {
        if (isValidName) nameError.set(null)
        else nameError.set(nameErrorMessage)
    }

    private fun validateButton() {
        isButtonEnabled.set(isValidName)
    }
}
