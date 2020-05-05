package com.dlminfosoft.sphmobile.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.adapter.AdapterDataUsage
import com.dlminfosoft.sphmobile.model.RecordsData
import com.dlminfosoft.sphmobile.utility.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var mAdapter: AdapterDataUsage
    private var dataUsageRecordList: ArrayList<RecordsData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        Log.e("Internet available:", "==>" + Constants.isInternetAvailable(this))
    }

    override fun setup() {
        for (i in 1..5) {
            dataUsageRecordList.add(RecordsData(i, "$i th quarter", "100"))
        }
        recycle_view_data_usage.layoutManager = LinearLayoutManager(this)
        mAdapter = AdapterDataUsage(this, dataUsageRecordList)
        recycle_view_data_usage.adapter = mAdapter
    }
}
