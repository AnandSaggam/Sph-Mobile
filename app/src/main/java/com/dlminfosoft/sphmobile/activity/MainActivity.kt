package com.dlminfosoft.sphmobile.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.adapter.AdapterDataUsage
import com.dlminfosoft.sphmobile.model.YearlyRecord
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

    override fun setup() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recycle_view_data_usage.layoutManager = LinearLayoutManager(this)
        mAdapter = AdapterDataUsage(
            this, ArrayList()
        )

        recycle_view_data_usage.adapter = mAdapter
        mainViewModel.callDataUsageDetails()
        mainViewModel.getListDataObservable().observe(this, Observer { response ->
            when {
                response != null -> {
                    if (response.success) {
                        if (response.result.records.isNotEmpty()) {
                            yearlyRecordList = mainViewModel.getResultYearlyRecord()
                            mAdapter.setDataList(yearlyRecordList)
                        } else {
                            showSnackBar(getString(R.string.no_record_available))
                        }
                    } else {
                        showSnackBar(getString(R.string.something_went_wrong))
                    }
                }
                else -> {
                    showSnackBar(getString(R.string.something_went_wrong))
                }
            }
        })
        mainViewModel.getInternetErrorObservable().observe(this, Observer {
            if (it) {
                showSnackBar(getString(R.string.no_internet))
            }
        })
    }
}
