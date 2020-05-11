package com.dlminfosoft.sphmobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlminfosoft.sphmobile.databinding.ListItemDataUsageBinding
import com.dlminfosoft.sphmobile.model.YearlyRecord
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * This class act as bridge between view and data source, to display data in view
 */
class AdapterDataUsage(
    context: Context,
    private var dataList: List<YearlyRecord>,
    private val onImgBtnClickCallback: (item: YearlyRecord) -> Unit
) : RecyclerView.Adapter<AdapterDataUsage.ItemHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ListItemDataUsageBinding.inflate(inflater)
        binding.listener = ClickHandler(onImgBtnClickCallback)
        val df = DecimalFormat("#.######")
        df.roundingMode = RoundingMode.CEILING
        binding.decimal = df
        return ItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(dataList[position])
    }


    inner class ItemHolder(private val binding: ListItemDataUsageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: YearlyRecord) {
            binding.record = item
            binding.executePendingBindings()
        }
    }

    internal fun setDataList(list: MutableList<YearlyRecord>) {
        this.dataList = list
        notifyDataSetChanged()
    }

    /**
     * This class used to handle click event of image buttons
     */
    class ClickHandler(private val onImgBtnClickCallback: (item: YearlyRecord) -> Unit) {
        fun onImgBtnClick(record: YearlyRecord) {
            onImgBtnClickCallback.invoke(record)
        }
    }
}