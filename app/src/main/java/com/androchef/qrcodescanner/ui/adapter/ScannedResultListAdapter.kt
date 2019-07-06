package com.androchef.qrcodescanner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.androchef.qrcodescanner.R
import com.androchef.qrcodescanner.db.DbHelperI
import com.androchef.qrcodescanner.db.entities.QrResult
import com.androchef.qrcodescanner.ui.dialogs.QrCodeResultDialog
import kotlinx.android.synthetic.main.layout_single_item_qr_result.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Developed by Happy on 6/7/19
 */
class ScannedResultListAdapter(var dbHelperI: DbHelperI,var context: Context, private var listOfScannedResult: MutableList<QrResult>) :
    RecyclerView.Adapter<ScannedResultListAdapter.ScannedResultListViewHolder>() {

    private var resultDialog: QrCodeResultDialog =
        QrCodeResultDialog(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedResultListViewHolder {
        return ScannedResultListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_single_item_qr_result,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfScannedResult.size
    }

    override fun onBindViewHolder(holder: ScannedResultListViewHolder, position: Int) {
        holder.bind(listOfScannedResult[position],position)
    }

    inner class ScannedResultListViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(qrResult: QrResult, position: Int) {
            setResultTypeIcon(qrResult.resultType)
            setFavourite(qrResult.favourite)
            setTime(qrResult.calendar)
            view.result.text = qrResult.result!!
            onClicks(qrResult,position)
        }

        private fun setTime(calendar: Calendar) {
            val simpleDateFormat = SimpleDateFormat("dd-mm-yyyy hh:mm a", Locale.US)
            view.tvTime.text = simpleDateFormat.format(calendar.time)
        }


        private fun setResultTypeIcon(resultType: String?) {

        }

        private fun setFavourite(isFavourite: Boolean) {
            if (isFavourite)
                view.favouriteIcon.visibility = View.VISIBLE
            else
                view.favouriteIcon.visibility = View.GONE
        }


        private fun onClicks(qrResult: QrResult, position: Int) {
            view.setOnClickListener {
                resultDialog.show(qrResult)
            }

            view.setOnLongClickListener {
                showDeleteDialog(qrResult,position)
                return@setOnLongClickListener true
            }
        }

        private fun showDeleteDialog(qrResult: QrResult, position: Int) {
            AlertDialog.Builder(context,R.style.CustomAlertDialog).setTitle(context.getString(R.string.delete))
                .setMessage(context.getString(R.string.want_to_delete))
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    deleteThisRecord(qrResult,position)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }.show()
        }

        private fun deleteThisRecord(qrResult: QrResult, position: Int) {
            dbHelperI.deleteQrResult(qrResult.id!!)
            listOfScannedResult.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}