package ch.reinhold.ifolor.uifeatures.registration

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.core.extensions.toFormattedCalendarTime
import ch.reinhold.ifolor.databinding.ActivityRegistrationBinding
import ch.reinhold.ifolor.domain.validators.DateRangeValidator
import ch.reinhold.ifolor.domain.validators.EmailFieldValidator
import ch.reinhold.ifolor.domain.validators.RequiredFieldValidator
import ch.reinhold.ifolor.uicore.actions.navigation.GoToConfirmationAction
import ch.reinhold.ifolor.uicore.actions.ShowDatePickerAction
import ch.reinhold.ifolor.uicore.actions.ViewModelAction
import ch.reinhold.ifolor.uicore.extensions.datepicker.buildDatePicker
import ch.reinhold.ifolor.uifeatures.confirmation.ConfirmationActivity
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import org.koin.core.get
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

class RegistrationActivity : AppCompatActivity(), KoinComponent {

    private val minDate = LocalDate.of(1900, 1, 1)
        .atStartOfDay(ZoneOffset.UTC)

    private val maxDate = LocalDate.of(2019, 12, 31)
        .atStartOfDay(ZoneOffset.UTC)

    private val viewModel by lazy {
        RegistrationViewModel(
            context = this,
            ioDispatcher = Dispatchers.IO,
            uiDispatcher = Dispatchers.Main,
            ifolorDao = get(),
            nameValidator = RequiredFieldValidator(),
            emailValidator = EmailFieldValidator(),
            birthdayValidator = DateRangeValidator(minDate, maxDate)
        )
    }

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        binding.model = viewModel

        viewModel.actions.observe(this, Observer {
            handleActions(it)
        })
    }

    private fun handleActions(action: ViewModelAction) {
        when (action) {
            is GoToConfirmationAction -> goToConfirmationScreen(action.email)
            is ShowDatePickerAction -> showDatePicker(action.initialDate)
        }
    }

    private fun showDatePicker(initialDate: Long?) {
        val datePicker = buildDatePicker(
            initialDate,
            R.string.prompt_birthday
        ) {
            addOnPositiveButtonClickListener { milliSeconds ->
                viewModel.setBirthday(
                    selectedDate = milliSeconds,
                    selectedDateFormatted = milliSeconds.toFormattedCalendarTime()
                )
            }
        }
        datePicker.show(supportFragmentManager, toString())
    }

    private fun goToConfirmationScreen(email: String) {
        val intent = ConfirmationActivity.makeIntent(this, email)
        startActivity(intent)
    }
}
