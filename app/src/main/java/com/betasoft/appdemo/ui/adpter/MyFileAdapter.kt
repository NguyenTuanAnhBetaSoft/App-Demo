package com.betasoft.appdemo.ui.adpter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.api.responseremote.ItemsItem
import com.betasoft.appdemo.databinding.ItemImageBinding

class MyFileAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var onClickItemListeners: OnClickItemListeners
    val data = mutableListOf<ItemsItem>()
    @SuppressLint("NotifyDataSetChanged")
    fun update(isAddMore: Boolean, newData: List<ItemsItem>?) {
        if (isAddMore) {
            val oldSize = data.size
            if (newData != null && newData.isNotEmpty()) {
                data.addAll(newData)
                notifyItemInserted(oldSize)
            }
        } else {
            data.clear()
            if (newData != null && newData.isNotEmpty()) {
                data.addAll(newData)
            }
            notifyDataSetChanged()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LocationViewHolder) {
            holder.bind(position)
        }
    }

    inner class LocationViewHolder(private val item: ItemImageBinding) :
        RecyclerView.ViewHolder(item.root) {
        fun bind(position: Int) {
            item.item = data[position]
            item.root.setOnClickListener {
                onClickItemListeners.onClickedItem(data[position])
            }

            item.btnDownLoad.setOnClickListener {
                onClickItemListeners.conClickedDownload(data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnClickItemListeners {
        fun onClickedItem(param: ItemsItem)
        fun conClickedDownload(param: ItemsItem)
    }
}