package com.app.bookshelf.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.getFormattedDate(): String {
    try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val date = originalFormat.parse(this)
        date?.let {
            val formattedDate: String = targetFormat.format(date)
            return formattedDate
        } ?: return ""

    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }

}