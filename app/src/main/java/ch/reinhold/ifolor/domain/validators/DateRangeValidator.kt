package ch.reinhold.ifolor.domain.validators

import org.threeten.bp.ZonedDateTime

/**
 * Compares a given time in milliseconds against specific min and max date boundaries to see if
 * the date is within the range of min and max dates.
 */
class DateRangeValidator constructor(
    private val minRangeMillis: Long,
    private val maxRangeMillis: Long
) : Validator<Long> {

    constructor(minRange: ZonedDateTime, maxRange: ZonedDateTime) : this(
        minRange.toInstant().toEpochMilli(),
        maxRange.toInstant().toEpochMilli()
    )

    override fun isValid(item: Long?) =
        if (item == null) false
        else (minRangeMillis..maxRangeMillis).contains(item)

}
