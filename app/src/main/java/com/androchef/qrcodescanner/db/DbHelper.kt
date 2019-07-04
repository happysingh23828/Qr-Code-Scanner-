package com.androchef.qrcodescanner.db

import com.androchef.qrcodescanner.db.database.QrResultDataBase
import com.androchef.qrcodescanner.db.entities.QrResult

/**
 * Developed by Happy on 5/7/19
 */
class DbHelper(var qrResultDataBase: QrResultDataBase) : DbHelperI {

    override fun insertQRResult(qrResult: QrResult) {
        qrResultDataBase.getQrDao().insertQrResult(qrResult)
    }

    override fun addToFavourite(id: Int): Int {
        return qrResultDataBase.getQrDao().addToFavourite(id)
    }

    override fun removeFromFavourite(id: Int): Int {
        return qrResultDataBase.getQrDao().removeFromFavourite(id)
    }

    override fun deleteQrResult(id: Int): Int {
        return qrResultDataBase.getQrDao().deleteQrResult(id)
    }

    override fun getAllQRScannedResult(): List<QrResult> {
        return qrResultDataBase.getQrDao().getAllScannedResult()
    }

    override fun getAllFavouriteQRScannedResult(): List<QrResult> {
        return qrResultDataBase.getQrDao().getAllFavouriteResult()
    }
}
