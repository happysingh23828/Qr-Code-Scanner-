package com.androchef.qrcodescanner.ui.dialogs

import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.text.ClipboardManager
import android.widget.Toast
import com.androchef.qrcodescanner.R
import com.androchef.qrcodescanner.db.DbHelper
import com.androchef.qrcodescanner.db.DbHelperI
import com.androchef.qrcodescanner.db.database.QrResultDataBase
import com.androchef.qrcodescanner.db.entities.QrResult
import kotlinx.android.synthetic.main.layout_qr_result_show.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Developed by Happy on 3/7/19
 */
class QrCodeResultDialog(var context: Context) {

    private lateinit var dialog: Dialog

    private lateinit var dbHelperI: DbHelperI

    private var qrResult: QrResult? = null

    private var onDismissListener: OnDismissListener? = null

    init {
        init()
        initDialog()
    }


    private fun init() {
        dbHelperI = DbHelper(QrResultDataBase.getAppDatabase(context)!!)
    }

    private fun initDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_qr_result_show)
        dialog.setCancelable(false)
        onClicks()
    }

    private fun onClicks() {

        dialog.favouriteIcon.setOnClickListener {
            if (it.isSelected) {
                removeFromFavourite()
            } else
                addToFavourite()
        }

        dialog.copyResult.setOnClickListener {
            copyResultToClipBoard()
        }

        dialog.shareResult.setOnClickListener {
            shareResult()
        }

        dialog.cancelDialog.setOnClickListener {
            dialog.dismiss()
            onDismissListener?.onDismiss()
        }
    }


    private fun addToFavourite() {
        dialog.favouriteIcon.isSelected = true
        dbHelperI.addToFavourite(qrResult?.id!!)
    }

    private fun removeFromFavourite() {
        dialog.favouriteIcon.isSelected = false
        dbHelperI.removeFromFavourite(qrResult?.id!!)
    }

    fun show(recentQrResult: QrResult) {
        this.qrResult = recentQrResult
        val simpleDateFormat = SimpleDateFormat("dd-mm-yyyy hh:mm a", Locale.US)
        dialog.scannedDate.text = simpleDateFormat.format(qrResult!!.calendar.time)
        dialog.scannedText.text = qrResult!!.result
        dialog.favouriteIcon.isSelected = qrResult!!.favourite
        dialog.show()
    }

    fun setOnDismissListener(dismissListener: OnDismissListener) {
        this.onDismissListener = dismissListener
    }

    private fun copyResultToClipBoard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("QrCodeScannedResult", dialog.scannedText.text)
        clipboard.text = clip.getItemAt(0).text.toString()
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun shareResult() {
        val txtIntent = Intent(Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(
            Intent.EXTRA_TEXT,
            dialog.scannedText.text.toString().plus("\n \nScanned By AndroChefQRScanner")
        )
        context.startActivity(Intent.createChooser(txtIntent, "Share QR Result"))
    }

    interface OnDismissListener {
        fun onDismiss()
    }
}