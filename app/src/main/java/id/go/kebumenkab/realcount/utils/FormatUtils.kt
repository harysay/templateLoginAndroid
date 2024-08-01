package id.go.kebumenkab.realcount.utils

import android.annotation.SuppressLint
import android.text.Spanned
import androidx.core.text.HtmlCompat
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun dateFormatter(date: String): String {
    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val newDateFormat: Date = dateFormat.parse(date) as Date
    dateFormat = SimpleDateFormat("dd MMM yyyy")
    val newDate = dateFormat.format(newDateFormat)

    return newDate.format(date)
}

@SuppressLint("SimpleDateFormat")
fun dateFormatter2(date: String): String {
    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val newDateFormat: Date = dateFormat.parse(date) as Date
    dateFormat = SimpleDateFormat("dd MMM")
    val newDate = dateFormat.format(newDateFormat)

    return newDate.format(date)
}

@SuppressLint("SimpleDateFormat")
fun dateFormatter3(date: String): String {
    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val newDateFormat: Date = dateFormat.parse(date) as Date
    dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val newDate = dateFormat.format(newDateFormat)

    return newDate.format(date)
}

fun numberFormatter(number: Int): String {
    val customSymbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = '.'
    }

    val customFormat = DecimalFormat("#,###", customSymbols)
    return customFormat.format(number)
}

fun fromHtml(htmlText: String?): Spanned? {
    return htmlText?.let {
        HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}