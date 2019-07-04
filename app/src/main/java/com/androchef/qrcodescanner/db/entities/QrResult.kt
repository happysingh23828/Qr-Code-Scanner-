package com.androchef.qrcodescanner.db.entities

import androidx.room.*
import com.androchef.qrcodescanner.db.converters.DateTimeConverters
import java.sql.Timestamp
import java.util.*

/**
 * Developed by Happy on 5/7/19
 */
@Entity
@TypeConverters(DateTimeConverters::class)
data class QrResult(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "result")
    val result: String?,

    @ColumnInfo(name = "result_type")
    val resultType: String?,

    @ColumnInfo(name = "favourite")
    val favourite: Boolean?,

    @ColumnInfo(name = "time")
    val calendar: Calendar

)