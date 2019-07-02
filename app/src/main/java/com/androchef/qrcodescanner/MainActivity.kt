package com.androchef.qrcodescanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androchef.qrcodescanner.ui.favourite_history.FavouriteHistoryFragment
import com.androchef.qrcodescanner.ui.qrscanner.QRScannerFragment
import com.androchef.qrcodescanner.ui.scanner_history.ScannedHistoryFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onClicks()
    }

    private fun onClicks() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.qrScanMenuId -> {
                    startQRScannerFragment()
                }
                R.id.scannedResultMenuId -> {
                    startScannedHistoryFragment()
                }
                R.id.favouriteScannedMenuId -> {
                    startFavouriteHistory()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun startQRScannerFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,QRScannerFragment()).commit()
    }

    private fun startScannedHistoryFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ScannedHistoryFragment()).commit()
    }
    private fun startFavouriteHistory(){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,FavouriteHistoryFragment()).commit()
    }
}
