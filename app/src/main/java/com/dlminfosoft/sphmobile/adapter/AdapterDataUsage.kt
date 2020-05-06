package com.dlminfosoft.sphmobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.model.YearlyRecord
import kotlinx.android.synthetic.main.list_item_data_usage.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class AdapterDataUsage(
    context: Context,
    private var dataList: ArrayList<YearlyRecord>
) :
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
        val df = DecimalFormat("#.######")
        df.roundingMode = RoundingMode.CEILING

        holder.txtYear.text = dataList[position].year
        holder.imgBtnDecrease.visibility = if (dataList[position].isDecreaseVolumeData)
            View.VISIBLE
        else
            View.GONE
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtYear: TextView = itemView.txt_year
        val imgBtnDecrease: ImageView = itemView.img_btn_decrease
    }

    internal fun setDataList(list: ArrayList<YearlyRecord>) {
        this.dataList = list
        notifyDataSetChanged()
    }
}