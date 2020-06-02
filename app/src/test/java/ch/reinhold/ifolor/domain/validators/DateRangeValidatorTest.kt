package ch.reinhold.ifolor.domain.validators

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

@Suppress("UsePropertyAccessSyntax")
class DateRangeValidatorTest {

    private val minDate = LocalDate.of(1900, 1, 1)
        .atStartOfDay(ZoneOffset.UTC)

    private val maxDate = LocalDate.of(2019, 12, 31)
        .atStartOfDay(ZoneOffset.UTC)

    private val dateWithinRange = LocalDate.of(2001, 12, 31)
        .atStartOfDay(ZoneOffset.UTC)

    private val dateBeforeRange = minDate.minusSeconds(1)
    private val dateAfterRange = maxDate.plusSeconds(1)

    private val minDateMillis = minDate.toInstant().toEpochMilli()
    private val maxDateMillis = maxDate.toInstant().toEpochMilli()

    private val dateWithinRangeInMillis = dateWithinRange.toInstant().toEpochMilli()
    private val dateBeforeRangeInMillis = dateBeforeRange.toInstant().toEpochMilli()
    private val dateAfterRangeInMillis = dateAfterRange.toInstant().toEpochMilli()

    @Test
    fun validatesTrueGivenDateWithinRange() {
        val underTest = DateRangeValidator(minDate, maxDate)
        val tested = underTest.isValid(dateWithinRangeInMillis)
        assertThat(tested).isTrue()
    }

    @Test
    fun validatesTrueGivenLongDateWithinRangeInMillis() {
        val underTest = DateRangeValidator(minDateMillis, maxDateMillis)
        val tested = underTest.isValid(dateWithinRangeInMillis)
        assertThat(tested).isTrue()
    }


    @Test
    fun validatesFalseGivenDateBeforeRange() {
        val underTest = DateRangeValidator(minDateMillis, maxDateMillis)
        val tested = underTest.isValid(dateBeforeRangeInMillis)
        assertThat(tested).isFalse()
    }

    @Test
    fun validatesFalseGivenDateAfterRange() {
        val underTest = DateRangeValidator(minDateMillis, maxDateMillis)
        val tested = underTest.isValid(dateAfterRangeInMillis)
        assertThat(tested).isFalse()
    }

    @Test
    fun validatesFalseGivenEmptyDate() {
        val date: Long? = null
        val underTest = DateRangeValidator(minDateMillis, maxDateMillis)
        val tested = underTest.isValid(date)
        assertThat(tested).isFalse()
    }

}
