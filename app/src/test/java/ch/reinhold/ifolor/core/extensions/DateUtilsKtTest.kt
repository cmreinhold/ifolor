package ch.reinhold.ifolor.core.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import java.util.*

class DateUtilsKtTest {

    private val localDate = LocalDate.of(2001, 12, 30)
        .atStartOfDay(ZoneOffset.UTC)

    private val localDateInMillis = localDate.toInstant().toEpochMilli()

    @Test
    fun formatsDateForUk() {
        val underTest = localDateInMillis
        val expected = "30/12/2001"
        val tested = underTest.toFormattedCalendarTime(locale = Locale.UK)
        assertThat(tested).isEqualTo(expected)
    }
}
