package com.androchef.qrcodescanner.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.Global.getString
import android.text.ClipboardManager
import android.util.Log
import android.widget.Toast
import com.androchef.qrcodescanner.R
import com.androchef.qrcodescanner.db.DbHelper
import com.androchef.qrcodescanner.db.DbHelperI
import com.androchef.qrcodescanner.db.database.QrResultDataBase
import com.androchef.qrcodescanner.db.entities.QrResult
import com.androchef.qrcodescanner.utils.ContentCheckUtil
import com.androchef.qrcodescanner.utils.ContentCheckUtil.isWebUrl
import com.androchef.qrcodescanner.utils.toFormattedDisplay
import kotlinx.android.synthetic.main.layout_qr_result_show.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL


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

        dialog.scannedText.setOnClickListener {
            checkContentAndPerformAction(dialog.scannedText.text.toString())
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
        dialog.scannedDate.text = qrResult?.calendar?.toFormattedDisplay()
        dialog.scannedText.text = "QR Code: " + qrResult!!.result
        dialog.favouriteIcon.isSelected = qrResult!!.favourite
        dialog.show()

        // Fetch Qr code Data via api
        val url = "https://qr-scanner-api.herokuapp.com/api/user/" + qrResult!!.result
        dialog.userInfo1.text = ""
        dialog.userInfo2.text = context.getString(R.string.loading);
        dialog.userInfo3.text = ""
        dialog.userInfo4.text = ""

        // Run in another thread until completion to avoid thread blocking
        doAsync{
            val json = URL(url).readText()
            uiThread {
                val obj = JSONObject(json)

                try {

                    if (obj.getString("status") == "success"){

                        dialog.userInfo1.text = "Name: \n" + obj.getJSONObject("data").getString("name")
                        dialog.userInfo2.text = "Email: \n" + obj.getJSONObject("data").getString("email")
                        dialog.userInfo3.text = "Field: \n" + obj.getJSONObject("data").getString("field")
                        dialog.userInfo4.text = "Website: \n" + obj.getJSONObject("data").getString("website")
                    }
                    else if (obj.getString("status") == "fail"){

                        dialog.userInfo1.text = ""
                        dialog.userInfo2.text = obj.getString("message")
                        dialog.userInfo3.text = ""
                        dialog.userInfo4.text = ""
                    }
                }
                catch (e: NumberFormatException) {

                    Log.e("Tag", e.toString())
                }
            }
        }

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
            dialog.scannedText.text.toString()
        )
        context.startActivity(Intent.createChooser(txtIntent, "Share QR Result"))
    }

    interface OnDismissListener {
        fun onDismiss()
    }

    // Checking content type and performing action on it.
    private fun checkContentAndPerformAction(scannedText: String) {
        when {

            // if it is web url
            isWebUrl(scannedText) -> {

                // opening web url.
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(scannedText)
                context.startActivity(i)
            }
        }
    }
}