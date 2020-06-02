package ch.reinhold.ifolor.uifeatures.confirmation

import ch.reinhold.ifolor.data.db.IfolorDao
import ch.reinhold.ifolor.data.db.entities.UserEntity
import ch.reinhold.ifolor.test.TestCoroutineDispatcherRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

@ExperimentalCoroutinesApi
class ConfirmationViewModelTest {

    @Rule
    @JvmField
    val rule = TestCoroutineDispatcherRule()

    private val testDispatcher get() = rule.testDispatcher


    private val localDate = LocalDate.of(2001, 12, 1)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()

    private val formattedLocalDate = "01/12/2001"

    private val user = UserEntity("email", "name", localDate.toEpochMilli())

    private val ifolorDao: IfolorDao = mock {
        on(mock.getUser(any())).thenReturn(user)
    }

    private val underTest = ConfirmationViewModel(
        testDispatcher,
        testDispatcher,
        ifolorDao,
        user.email
    )

    @Test
    fun displaysUserInfo() {
        underTest.loadUser()
        assertThat(underTest.name.get()!!).isEqualTo(user.name)
        assertThat(underTest.email.get()!!).isEqualTo(user.email)
        assertThat(underTest.birthday.get()!!).isEqualTo(formattedLocalDate)
    }

}
