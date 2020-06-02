package ch.reinhold.ifolor.domain.validators

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@Suppress("UsePropertyAccessSyntax")
class EmailFieldValidatorTest {

    private val underTest = EmailFieldValidator()

    @Test
    fun validatesTrueGivenValidLongerEmailAddress() {
        val text = "christian.reinhold+56@ifolor.com"
        val result = underTest.isValid(text)
        assertThat(result).isTrue()
    }

    @Test
    fun validatesTrueGivenValidDummyEmailAddress() {
        val dummyEmail = "christian.reinhold@ifolor.com"
        val result = underTest.isValid(dummyEmail)
        assertThat(result).isTrue()
    }

    @Test
    fun validatesTrueGivenValidLongEmailAddress() {
        val text = "ch.am+56@y.ch"
        val result = underTest.isValid(text)
        assertThat(result).isTrue()
    }

    @Test
    fun validatesTrueGivenValidEmailAddress() {
        val text = "x@y.z"
        val result = underTest.isValid(text)
        assertThat(result).isTrue()
    }

    @Test
    fun validatesFalseGivenNoAtSign() {
        val text = "cmr.xyz"
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
