package com.androchef.qrcodescanner.utils

import android.util.Patterns
import android.webkit.URLUtil
import java.net.MalformedURLException


object ContentCheckUtil {

    fun isWebUrl(urlString: String?): Boolean {
        return try {
            URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
        } catch (ignored: MalformedURLException) {
            false
        }
    }
}