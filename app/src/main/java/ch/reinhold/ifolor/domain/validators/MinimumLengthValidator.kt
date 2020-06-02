package ch.reinhold.ifolor.domain.validators

open class MinimumLengthValidator(private val minimumLength: Int = 0) : Validator<String> {
    override fun isValid(item: String?): Boolean {
        val itemLength = item?.length ?: 0
        return itemLength >= minimumLength
    }
}
