package ch.reinhold.ifolor.domain.validators

interface Validator<T : Any> {
    fun isValid(item: T?): Boolean
}
