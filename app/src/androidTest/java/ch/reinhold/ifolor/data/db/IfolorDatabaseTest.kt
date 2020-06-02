package ch.reinhold.ifolor.data.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.reinhold.ifolor.data.db.entities.UserEntity
import ch.reinhold.ifolor.tests.rules.TestRoomDatabaseRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class IfolorDatabaseTest {

    @Rule
    @JvmField
    val rule = TestRoomDatabaseRule(IfolorDatabase::class)

    private val database get() = rule.database

    private val localDate = LocalDate.of(1981, 4, 1)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()

    private val user = UserEntity(
        name = "username",
        email = "email@x.y",
        birthday = localDate.toEpochMilli()
    )

    @Test
    fun insertsAndGetsUser() {
        val underTest = database.dao()
        val tested = underTest.getUser(user.email)
        assertThat(tested).isNull()

        rule.testScope.launch { underTest.insertUser(user) }

        val testedStoredUser = underTest.getUser(user.email)!!
        with(testedStoredUser) {
            assertThat(email).isEqualTo(user.email)
            assertThat(name).isEqualTo(user.name)
            assertThat(birthday).isEqualTo(user.birthday)
        }
    }

}
