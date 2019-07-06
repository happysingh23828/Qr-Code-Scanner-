package com.androchef.qrcodescanner.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

/**
 * Developed by Happy on 6/7/19
 */


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun Calendar.toFormattedDisplay(): String {
    val simpleDateFormat = SimpleDateFormat("dd-mm-yyyy hh:mm a", Locale.US)
    return simpleDateFormat.format(this.time)
}