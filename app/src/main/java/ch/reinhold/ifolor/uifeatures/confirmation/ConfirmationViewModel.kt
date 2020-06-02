package ch.reinhold.ifolor.uifeatures.confirmation

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.reinhold.ifolor.core.extensions.toFormattedCalendarTime
import ch.reinhold.ifolor.data.db.IfolorDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmationViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val uiDispatcher: CoroutineDispatcher,
    private val ifolorDao: IfolorDao,
    private val userEmail: String
) : ViewModel() {

    val name = ObservableField<String>()
    val email = ObservableField<String>()
    val birthday = ObservableField<String>()

    fun loadUser() = viewModelScope.launch(ioDispatcher) {
        val user = ifolorDao.getUser(userEmail) ?: return@launch
        val formattedBirthday = user.birthday.toFormattedCalendarTime()
        withContext(uiDispatcher) {
            name.set(user.name)
            email.set(user.email)
            birthday.set(formattedBirthday)
        }
    }

}
