package com.androchef.qrcodescanner.db.dao

import androidx.room.*
import com.androchef.qrcodescanner.db.entities.QrResult

/**
 * Developed by Happy on 4/7/19
 */
@Dao
interface QrResultDao {

    @Query("SELECT * FROM QrResult ORDER BY time DESC")
    fun getAllScannedResult(): List<QrResult>

    @Query("SELECT * FROM QrResult WHERE favourite = 1 ORDER BY time DESC")
    fun getAllFavouriteResult(): List<QrResult>

    @Query("DELETE FROM QrResult")
    fun deleteAllScannedResult()

    @Query("DELETE FROM QrResult WHERE favourite = 1")
    fun deleteAllFavouriteResult()

    @Query("DELETE FROM QrResult WHERE id = :id")
    fun deleteQrResult(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQrResult(qrResult: QrResult): Long

    @Query("SELECT * FROM QrResult WHERE id = :id")
    fun getQrResult(id: Int): QrResult

    @Query("UPDATE QrResult SET favourite = 1 WHERE id = :id")
    fun addToFavourite(id: Int): Int

    @Query("UPDATE QrResult SET favourite = 0 WHERE id = :id")
    fun removeFromFavourite(id: Int): Int

    @Query("SELECT * FROM QrResult WHERE result = :result ")
    fun checkIfQrResultExist(result: String): Int


}