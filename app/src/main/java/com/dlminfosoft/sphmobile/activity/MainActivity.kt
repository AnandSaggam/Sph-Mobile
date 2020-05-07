package com.dlminfosoft.sphmobile.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.adapter.AdapterDataUsage
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.utility.Constants
import com.dlminfosoft.sphmobile.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var mAdapter: AdapterDataUsage
    private lateinit var mainViewModel: MainViewModel
    private var yearlyRecordList = ArrayList<YearlyRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    /*
    *  Lambda function pass as argument in adapter to show alert dialog
    */
    var performOnImgBtnClick = { record: YearlyRecord ->
        val value = record.treeMapWithDataUsage.getValue(record.decreaseVolumeQuarterKey)
        val message =
            "${record.year} - ${record.decreaseVolumeQuarterKey} ${getString(R.string.message_decrease_volume)} $value"
        showAlertDialog(getString(R.string.alert_title), message)
    }

    /*
    *  Initializer method
    */
    override fun setup() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recycle_view_data_usage.layoutManager = LinearLayoutManager(this)
        mAdapter = AdapterDataUsage(
            this, ArrayList(), performOnImgBtnClick
        )

        recycle_view_data_usage.adapter = mAdapter
        loadData(false)

        /*
        *  Swipe to refresh listener
        */
        swipe_to_refresh_list.setOnRefreshListener {
            loadData(true)
        }

        /*
        * Set different colors of loading
        */
        swipe_to_refresh_list.setColorSchemeColors(
            ContextCompat.getColor(this, android.R.color.holo_blue_bright),
            ContextCompat.getColor(this, android.R.color.holo_green_light),
            ContextCompat.getColor(this, android.R.color.holo_red_light),
            ContextCompat.getColor(this, android.R.color.holo_red_light)
        )
    }

    /*
    *  Call viewModel method to get data and Observer the response
    */
    private fun loadData(isFromSwipeToRefresh: Boolean) {
        if (Constants.isInternetAvailable(this) || !isFromSwipeToRefresh) {
            if (!isFromSwipeToRefresh) showLoading()
            else clearList()

            mainViewModel.callDataUsageDetails(Constants.isInternetAvailable(this))

            mainViewModel.yearlyRecordListObservable().observe(this, Observer { response ->
                if (response != null && response.isSuccess) {
                    if (response.recordList.isNotEmpty()) {
                        yearlyRecordList = response.recordList
                        mAdapter.setDataList(yearlyRecordList)
                        if (!Constants.isInternetAvailable(this))
                            showSnackBar(getString(R.string.message_data_from_local_db))
                    } else {
                        if (!isFromSwipeToRefresh) showSnackBar(getString(R.string.no_internet))
                        else showSnackBar(getString(R.string.no_record_available))
                    }
                } else {
                    showSnackBar(getString(R.string.something_went_wrong))
                }
                swipe_to_refresh_list.isRefreshing = false
                hideLoading()
            })
        } else {
            swipe_to_refresh_list.isRefreshing = false
            showSnackBar(getString(R.string.no_internet))
        }
    }

    /*
    * Clear list data when do swipe to refresh
    */
    private fun clearList() {
        yearlyRecordList.clear()
        mAdapter.setDataList(yearlyRecordList)
    }

    /*
    * Hiding progress bar
    */
    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    /*
    * Showing progress bar
    */
    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }
}
