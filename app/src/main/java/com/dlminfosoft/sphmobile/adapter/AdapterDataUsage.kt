package com.dlminfosoft.sphmobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.model.RecordsData
import kotlinx.android.synthetic.main.list_item_data_usage.view.*

class AdapterDataUsage(context: Context, private var dataList: ArrayList<RecordsData>) :
    RecyclerView.Adapter<AdapterDataUsage.ItemHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = inflater.inflate(R.layout.list_item_data_usage, parent, false)
        return ItemHolder(itemView)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.txtYear.text = dataList[position].quarter
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtYear:TextView = itemView.txt_year
    }

    internal fun setDataList(listMyActivity: List<RecordsData>) {
        this.dataList = listMyActivity as ArrayList<RecordsData>
        notifyDataSetChanged()
    }
}