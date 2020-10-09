package com.androchef.qrcodescanner.db

import com.androchef.qrcodescanner.db.entities.QrResult

/**
 * Developed by Happy on 5/7/19
 */
interface DbHelperI {

    fun insertQRResult(result: String): Int

    fun getQRResult(id: Int): QrResult

    fun addToFavourite(id: Int): Int

    fun removeFromFavourite(id: Int): Int

    fun deleteQrResult(id: Int): Int

    fun getAllQRScannedResult(): List<QrResult>

    fun getAllFavouriteQRScannedResult(): List<QrResult>

    fun deleteAllQRScannedResult()

    fun deleteAllFavouriteQRScannedResult()
}