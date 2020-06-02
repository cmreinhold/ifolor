package ch.reinhold.ifolor.domain.validators

class EmailFieldValidator : Validator<String> {

    private val emailPattern2 = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    ).toPattern()

    override fun isValid(item: String?) = when {
        item == null -> false
        item.contains('@') -> emailPattern2.matcher(item).matches()
        item.contains("@") -> emailPattern2.matcher(item).matches()
        else -> false
    }

}
