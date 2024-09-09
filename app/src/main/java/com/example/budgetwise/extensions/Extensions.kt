package com.example.budgetwise.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatDateTimeFromTimestamp(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault())
    val date = Date(this)
    return dateFormat.format(date).uppercase(Locale.getDefault())
}

fun Long.formatDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = Date(this)
    return dateFormat.format(date).uppercase(Locale.getDefault())
}