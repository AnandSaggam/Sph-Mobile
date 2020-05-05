package com.dlminfosoft.sphmobile.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.adapter.AdapterDataUsage
import com.dlminfosoft.sphmobile.model.RecordsData
import com.dlminfosoft.sphmobile.utility.Constants
import com.dlminfosoft.sphmobile.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var mAdapter: AdapterDataUsage
    private var mDataUsageRecordList: ArrayList<RecordsData> = ArrayList()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        Log.e("Internet available:", "==>" + Constants.isInternetAvailable(this))
    }

    override fun setup() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recycle_view_data_usage.layoutManager = LinearLayoutManager(this)
        mAdapter = AdapterDataUsage(this, mDataUsageRecordList)
        recycle_view_data_usage.adapter = mAdapter

        mainViewModel.callDataUsageDetails()
        mainViewModel.getListDataObservable().observe(this, Observer { response ->
            when {
                response != null -> {
                    if (response.success) {
                        if (response.result.records.isNotEmpty()) {
                            mDataUsageRecordList = response.result.records
                            mAdapter.setDataList(mDataUsageRecordList)
                        } else {
                            showSnackBar(getString(R.string.no_record_available))
                        }
                    } else {
                        showSnackBar(getString(R.string.something_went_wrong))
                    }
                }
                response == null -> {
                    showSnackBar(getString(R.string.something_went_wrong))
                }
            }
        })
    }
}
