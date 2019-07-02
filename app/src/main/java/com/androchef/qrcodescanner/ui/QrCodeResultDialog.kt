package com.androchef.qrcodescanner.ui

import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.text.ClipboardManager
import android.widget.Toast
import com.androchef.qrcodescanner.R
import kotlinx.android.synthetic.main.layout_qr_result_show.*


/**
 * Developed by Happy on 3/7/19
 */
class QrCodeResultDialog(var context: Context) {

    private lateinit var dialog: Dialog

    init {
        initDialog()
    }

    private fun initDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_qr_result_show)
        dialog.setCancelable(false)
        onClicks()
    }

    private fun onClicks() {

        dialog.favouriteAdd.setOnClickListener {
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
        }
    }


    private fun addToFavourite() {
        dialog.favouriteAdd.isSelected = true
    }

    private fun removeFromFavourite() {
        dialog.favouriteAdd.isSelected = false
    }

    fun show(string: String) {
        dialog.scannedText.text = string
        dialog.show()
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
        txtIntent.putExtra(Intent.EXTRA_TEXT, dialog.scannedText.text)
        context.startActivity(Intent.createChooser(txtIntent, "Share QR Result"))
    }

}