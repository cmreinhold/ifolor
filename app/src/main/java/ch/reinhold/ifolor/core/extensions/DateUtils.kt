package ch.reinhold.ifolor.core.extensions

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

const val UTC = "UTC"
private const val DEFAULT_DATE_PATTERN = "dd/MM/YYYY"

fun Long.toFormattedCalendarTime(
    pattern: String = DEFAULT_DATE_PATTERN,
    locale: Locale = Locale.getDefault(),
    zoneId: ZoneId = ZoneOffset.UTC
): String {
    val instant = Instant.ofEpochMilli(this)
    val dateTime = LocalDateTime.ofInstant(instant, zoneId)

    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(dateTime)
}
