package ch.reinhold.ifolor.uifeatures.confirmation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ch.reinhold.ifolor.R
import ch.reinhold.ifolor.databinding.ActivityConfirmationBinding
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import org.koin.core.get

class ConfirmationActivity : AppCompatActivity(), KoinComponent {

    private lateinit var binding: ActivityConfirmationBinding

    private val viewModel by lazy {
        ConfirmationViewModel(
            ioDispatcher = Dispatchers.IO,
            uiDispatcher = Dispatchers.Main,
            ifolorDao = get(),
            userEmail = userEmail
        )
    }

    private val userEmail get() = intent.getStringExtra(PARAM_EMAIL) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirmation)
        binding.model = viewModel

        viewModel.loadUser()
    }

    companion object {

        private const val PARAM_EMAIL = "param.email"

        fun makeIntent(context: Context, email: String) =
            Intent(context, ConfirmationActivity::class.java).apply {
                putExtra(PARAM_EMAIL, email)
            }
    }
}
