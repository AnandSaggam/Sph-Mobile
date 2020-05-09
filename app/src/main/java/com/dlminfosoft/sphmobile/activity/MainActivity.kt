package com.dlminfosoft.sphmobile.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.adapter.AdapterDataUsage
import com.dlminfosoft.sphmobile.databinding.ActivityMainBinding
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {
    private lateinit var mAdapter: AdapterDataUsage
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var activityMainBinding: ActivityMainBinding
    private var yearlyRecordList = mutableListOf<YearlyRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    /*
    *  Lambda function pass as argument in adapter to show alert dialog
    */
    private var performOnImgBtnClick = { record: YearlyRecord ->
        val value = record.treeMapWithDataUsage.getValue(record.decreaseVolumeQuarterKey)
        val message =
            "${record.year} - ${record.decreaseVolumeQuarterKey} ${getString(R.string.message_decrease_volume)} $value"
        showAlertDialog(getString(R.string.alert_title), message)
    }

    /*
    *  Initializer method
    */
    override fun setup() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mAdapter = AdapterDataUsage(this, yearlyRecordList, performOnImgBtnClick)
        activityMainBinding.showLoading = false
        activityMainBinding.myAdapter = mAdapter
        loadData(false)

        /*
        *  Swipe to refresh listener
        */
        swipe_to_refresh_list.setOnRefreshListener {
            clearList()
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
        activityMainBinding.showLoading = !isFromSwipeToRefresh
        mainViewModel.getListOfData()

        mainViewModel.yearlyRecordListObservable().observe(this, Observer { response ->
            if (response != null && response.isSuccess) {
                if (response.recordList.isNotEmpty()) {
                    yearlyRecordList = response.recordList.toMutableList()
                    mAdapter.setDataList(yearlyRecordList)
                } else if (response.recordList.isEmpty() && !response.isInternetAvailable) {
                    showSnackBar(getString(R.string.no_internet))
                } else {
                    showSnackBar(getString(R.string.no_record_available))
                }
            } else {
                showSnackBar(getString(R.string.something_went_wrong))
            }
            swipe_to_refresh_list.isRefreshing = false
            activityMainBinding.showLoading = false
        })
    }

    /*
    * Clear list data when do swipe to refresh
    */
    private fun clearList() {
        yearlyRecordList.clear()
        mAdapter.setDataList(yearlyRecordList)
    }
}
