package ch.reinhold.ifolor.ui.registration

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RegistrationViewModel : ViewModel() {

    val actions = MutableLiveData<ViewModelAction>()

    private fun emit(action: ViewModelAction) = actions.postValue(action)

    private fun emitAsync(action: ViewModelAction) = viewModelScope.launch {
        emit(action)
    }

    val isButtonEnabled = ObservableBoolean()
    private val onRegisterButtonClick = View.OnClickListener {
        emitAsync(GoToConfirmationAction())
    }

    fun getOnRegisterClick() = onRegisterButtonClick

}
