package ch.reinhold.ifolor.domain.validators

import org.assertj.core.api.Assertions
import org.junit.Test

class RequiredFieldValidatorTest {

    private val underTest = RequiredFieldValidator()

    @Test
    fun validatesTrueGivenTextLengthIsGreaterThanMinimum() {
        val text = "123456"
        val result = underTest.isValid(text)
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun validatesTrueGivenTextLengthIsOne() {
        val text = "1"
        val result = underTest.isValid(text)
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun validatesFalseGivenTextLengthIsZero() {
        val text = ""
        val result = underTest.isValid(text)
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun validatesFalseGivenTextLengthIsNull() {
        val text = null
        val result = underTest.isValid(text)
        Assertions.assertThat(result).isFalse()
    }
}
