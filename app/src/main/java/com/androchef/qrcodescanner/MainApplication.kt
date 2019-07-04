package com.androchef.qrcodescanner

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Developed by Happy on 2/7/19
 */
class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}