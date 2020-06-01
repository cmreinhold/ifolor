package ch.reinhold.ifolor.ui.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.core.logger.obtainLogger
import ch.reinhold.ifolor.databinding.ActivityRegistrationBinding
import ch.reinhold.ifolor.ui.actions.GoToConfirmationAction
import ch.reinhold.ifolor.ui.actions.ViewModelAction
import org.koin.core.KoinComponent

class RegistrationActivity : AppCompatActivity(), KoinComponent {

    private val logger = obtainLogger("RegistrationActivity")

    private val viewModel = RegistrationViewModel()

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
        }
    }

    private fun goToConfirmationScreen() {
        // TODO: Implement navigation action to confirmation screen.
        logger.debug("Register clicked. Going to confirmation screen")
    }
}