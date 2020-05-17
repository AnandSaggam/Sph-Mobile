package com.dlminfosoft.sphmobile.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.adapter.AdapterDataUsage
import com.dlminfosoft.sphmobile.databinding.ActivityMainBinding
import com.dlminfosoft.sphmobile.model.YearlyRecord
import com.dlminfosoft.sphmobile.utility.EspressoIdlingResource
import com.dlminfosoft.sphmobile.viewmodel.MainViewModelImpl
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * This is first activity which display in  app
 */
class MainActivity : BaseActivity() {
    private lateinit var mAdapter: AdapterDataUsage
    private val mainViewModelImpl: MainViewModelImpl by viewModel()
    private lateinit var activityMainBinding: ActivityMainBinding
    private var yearlyRecordList = mutableListOf<YearlyRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    /**
     *  Lambda function pass as argument in adapter to show alert dialog,
     *  alternatively we can pass viewModel instance in adapter, and observe when to saw dialog in view
     */
    private var performOnImgBtnClick = { record: YearlyRecord ->
        val value = record.treeMapWithDataUsage.getValue(record.decreaseVolumeQuarterKey)
        val message =
            "${record.year} - ${record.decreaseVolumeQuarterKey} ${getString(R.string.message_decrease_volume)} $value"
        showAlertDialog(getString(R.string.alert_title), message)
    }

    /**
     *  Initializer method
     */
    override fun setup() {
        EspressoIdlingResource.increment() // This used for only Espresso UI test cases
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mAdapter = AdapterDataUsage(this, yearlyRecordList, performOnImgBtnClick)
        activityMainBinding.showLoading = false
        activityMainBinding.myAdapter = mAdapter
        loadData(false)

        /**
         *  Swipe to refresh listener
         */
        swipe_to_refresh_list.setOnRefreshListener {
            clearList()
            loadData(true)
        }

        /**
         * Set different colors of loading
         */
        swipe_to_refresh_list.setColorSchemeColors(
            ContextCompat.getColor(this, android.R.color.holo_blue_bright),
            ContextCompat.getColor(this, android.R.color.holo_green_light),
            ContextCompat.getColor(this, android.R.color.holo_red_light),
            ContextCompat.getColor(this, android.R.color.holo_red_light)
        )
    }

    /**
     *  Call viewModel method to get data and Observer the response
     */
    private fun loadData(isFromSwipeToRefresh: Boolean) {
        activityMainBinding.showLoading = !isFromSwipeToRefresh
        if (isFromSwipeToRefresh) mainViewModelImpl.fetchDataFromRepo()

        /**
         *  this Observer invoke whenever there is list of records available
         */
        mainViewModelImpl.getObservableMainApiResponse().value?.yearlyRecordListLiveData?.observe(
            this,
            Observer {
                yearlyRecordList = it.toMutableList()
                mAdapter.setDataList(yearlyRecordList)
                hideLoading()
            })

        /**
         *  this Observer invoke whenever there is any error message
         */
        mainViewModelImpl.getObservableMainApiResponse().value?.errorLiveData?.observe(
            this,
            Observer {
                showSnackBar(it.message.toString())
                hideLoading()
            })
    }

    /**
     *  this method executed after Observer gets invoke
     */
    private fun hideLoading() {
        swipe_to_refresh_list.isRefreshing = false
        activityMainBinding.showLoading = false
        EspressoIdlingResource.decrement() // This used for only Espresso UI test cases, we can disable it in release build
    }

    /**
     * Clear list data when do swipe to refresh
     */
    private fun clearList() {
        yearlyRecordList.clear()
        mAdapter.setDataList(yearlyRecordList)
    }
}
