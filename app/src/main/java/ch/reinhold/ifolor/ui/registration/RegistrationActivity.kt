package ch.reinhold.ifolor.ui.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.core.extensions.toFormattedCalendarTime
import ch.reinhold.ifolor.core.logger.obtainLogger
import ch.reinhold.ifolor.databinding.ActivityRegistrationBinding
import ch.reinhold.ifolor.domain.validators.DateRangeValidator
import ch.reinhold.ifolor.domain.validators.EmailFieldValidator
import ch.reinhold.ifolor.domain.validators.RequiredFieldValidator
import ch.reinhold.ifolor.ui.actions.GoToConfirmationAction
import ch.reinhold.ifolor.ui.actions.ShowDatePickerAction
import ch.reinhold.ifolor.ui.actions.ViewModelAction
import ch.reinhold.ifolor.ui.extensions.datepicker.buildDatePicker
import org.koin.core.KoinComponent
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

class RegistrationActivity : AppCompatActivity(), KoinComponent {

    private val logger = obtainLogger("RegistrationActivity")

    private val minDate = LocalDate.of(1900, 1, 1)
        .atStartOfDay(ZoneOffset.UTC)

    private val maxDate = LocalDate.of(2019, 12, 31)
        .atStartOfDay(ZoneOffset.UTC)

    private val viewModel = RegistrationViewModel(
        context = this,
        nameValidator = RequiredFieldValidator(),
        emailValidator = EmailFieldValidator(),
        birthdayValidator = DateRangeValidator(minDate, maxDate)
    )

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
            is GoToConfirmationAction -> goToConfirmationScreen()
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

    private fun goToConfirmationScreen() {
        // TODO: Implement navigation action to confirmation screen.
        logger.debug("Register clicked. Going to confirmation screen")
    }
}
