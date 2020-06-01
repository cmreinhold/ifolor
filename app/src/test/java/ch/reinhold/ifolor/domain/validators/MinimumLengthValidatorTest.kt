package ch.reinhold.ifolor.domain.validators

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@Suppress("UsePropertyAccessSyntax")
class MinimumLengthValidatorTest {

    private val minValidLength = 5

    private val underTest = MinimumLengthValidator(minValidLength)

    @Test
    fun validatesTrueGivenTextLengthIsGreaterThanMinimum() {
        val text = "123456"
        val result = underTest.isValid(text)
        assertThat(result).isTrue()
    }

    @Test
    fun validatesTrueGivenTextLengthIsTheSameAsMinimum() {
        val text = "12345"
        val result = underTest.isValid(text)
        assertThat(result).isTrue()
    }

    @Test
    fun validatesFalseGivenTextLengthIsLessMinimum() {
        val text = "1234"
        val result = underTest.isValid(text)
        assertThat(result).isFalse()
    }

    @Test
    fun validatesFalseGivenTextLengthIsZero() {
        val text = ""
        val result = underTest.isValid(text)
        assertThat(result).isFalse()
    }

    @Test
    fun validatesFalseGivenTextLengthIsNull() {
        val text = null
        val result = underTest.isValid(text)
        assertThat(result).isFalse()
    }

}
