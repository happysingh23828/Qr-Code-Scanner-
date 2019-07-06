package com.androchef.qrcodescanner.db

import com.androchef.qrcodescanner.db.database.QrResultDataBase
import com.androchef.qrcodescanner.db.entities.QrResult
import java.util.*

/**
 * Developed by Happy on 5/7/19
 */
class DbHelper(var qrResultDataBase: QrResultDataBase) : DbHelperI {

    override fun insertQRResult(result: String): Int {
        val time = Calendar.getInstance()
        val resultType = findResultType(result)
        val qrResult = QrResult(result = result, resultType = resultType, calendar = time, favourite = false)
        return qrResultDataBase.getQrDao().insertQrResult(qrResult).toInt()
    }

    override fun getQRResult(id: Int): QrResult {
        return qrResultDataBase.getQrDao().getQrResult(id)
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

    override fun deleteAllQRScannedResult() {
        qrResultDataBase.getQrDao().deleteAllScannedResult()
    }

    override fun deleteAllFavouriteQRScannedResult() {
        qrResultDataBase.getQrDao().deleteAllFavouriteResult()
    }

    /*
    * This feature will add in future
    * */
    private fun findResultType(result: String): String {
        return "TEXT"
    }



}
