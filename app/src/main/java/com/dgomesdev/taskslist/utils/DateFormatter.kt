package com.dgomesdev.taskslist.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.getDate(
    pattern: String = "dd/MM/yyyy",
    date: Date = Date(this)): String {
    val formatter = SimpleDateFormat(pattern, Locale.FRANCE).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }
    return formatter.format(date)
}

fun String.toDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern("dd/MM/yyyy"))