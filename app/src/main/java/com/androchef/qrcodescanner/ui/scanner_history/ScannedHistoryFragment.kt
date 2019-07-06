package com.androchef.qrcodescanner.ui.scanner_history


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androchef.qrcodescanner.R
import com.androchef.qrcodescanner.db.DbHelper
import com.androchef.qrcodescanner.db.DbHelperI
import com.androchef.qrcodescanner.db.database.QrResultDataBase
import com.androchef.qrcodescanner.db.entities.QrResult
import com.androchef.qrcodescanner.ui.adapter.ScannedResultListAdapter
import com.androchef.qrcodescanner.utils.gone
import com.androchef.qrcodescanner.utils.visible
import kotlinx.android.synthetic.main.fragment_scanned_history.view.*
import kotlinx.android.synthetic.main.layout_header_history.view.*
import java.io.Serializable


class ScannedHistoryFragment : Fragment() {

    enum class ResultListType : Serializable {
        ALL_RESULT, FAVOURITE_RESULT
    }

    companion object {

        private const val ARGUMENT_RESULT_LIST_TYPE = "ArgumentResultType"

        fun newInstance(screenType: ResultListType): ScannedHistoryFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARGUMENT_RESULT_LIST_TYPE, screenType)
            val fragment = ScannedHistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    private lateinit var mView: View

    private lateinit var dbHelperI: DbHelperI

    private var resultListType: ResultListType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
    }

    private fun handleArguments() {
        resultListType = arguments?.getSerializable(ARGUMENT_RESULT_LIST_TYPE) as ResultListType
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_scanned_history, container, false)
        init()
        setSwipeRefresh()
        onClicks()
        showListOfResults()
        return mView.rootView
    }

    private fun init() {
        dbHelperI = DbHelper(QrResultDataBase.getAppDatabase(context!!)!!)
        mView.layoutHeader.tvHeaderText.text = getString(R.string.recent_scanned_results)
    }

    private fun showListOfResults() {
        when (resultListType) {
            ResultListType.ALL_RESULT -> showAllResults()
            ResultListType.FAVOURITE_RESULT -> showFavouriteResults()
        }
    }


    private fun showAllResults() {
        val listOfAllResult = dbHelperI.getAllQRScannedResult()
        showResults(listOfAllResult)
        mView.layoutHeader.tvHeaderText.text = getString(R.string.recent_scanned)
    }

    private fun showFavouriteResults() {
        val listOfFavouriteResult = dbHelperI.getAllFavouriteQRScannedResult()
        showResults(listOfFavouriteResult)
        mView.layoutHeader.tvHeaderText.text = getString(R.string.favourites_scanned_results)
    }


    private fun showResults(listOfQrResult: List<QrResult>) {
        if (listOfQrResult.isNotEmpty())
            initRecyclerView(listOfQrResult)
        else
            showEmptyState()
    }

    private fun initRecyclerView(listOfQrResult: List<QrResult>) {
        mView.scannedHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
        mView.scannedHistoryRecyclerView.adapter =
            ScannedResultListAdapter(dbHelperI, context!!, listOfQrResult.toMutableList())
        showRecyclerView()
    }

    private fun setSwipeRefresh() {
        mView.swipeRefresh.setOnRefreshListener {
            mView.swipeRefresh.isRefreshing = false
            showListOfResults()
        }
    }


    private fun onClicks() {
        mView.layoutHeader.removeAll.setOnClickListener {
            showRemoveAllScannedResultDialog()
        }
    }

    private fun showRemoveAllScannedResultDialog() {
        AlertDialog.Builder(context!!, R.style.CustomAlertDialog).setTitle(getString(R.string.clear_all))
            .setMessage(getString(R.string.clear_all_result))
            .setPositiveButton(getString(R.string.clear)) { _, _ ->
                clearAllRecords()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.show()

    }

    private fun clearAllRecords() {
        when (resultListType) {
            ResultListType.ALL_RESULT -> dbHelperI.deleteAllQRScannedResult()
            ResultListType.FAVOURITE_RESULT -> dbHelperI.deleteAllFavouriteQRScannedResult()
        }
        mView.scannedHistoryRecyclerView.adapter?.notifyDataSetChanged()
        showListOfResults()
    }

    private fun showRecyclerView() {
        mView.layoutHeader.removeAll.visible()
        mView.scannedHistoryRecyclerView.visible()
        mView.noResultFound.gone()
    }

    private fun showEmptyState() {
        mView.layoutHeader.removeAll.gone()
        mView.scannedHistoryRecyclerView.gone()
        mView.noResultFound.visible()
    }
}
