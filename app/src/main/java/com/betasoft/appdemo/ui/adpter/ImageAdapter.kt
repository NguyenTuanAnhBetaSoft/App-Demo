package com.betasoft.appdemo.ui.adpter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.api.responseremote.Item
import com.betasoft.appdemo.databinding.ItemImageBinding

class ImageAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val data = mutableListOf<Item>()
    @SuppressLint("NotifyDataSetChanged")
    fun update(isAddMore: Boolean, newData: List<Item>?) {
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

    inner class LocationViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.item = data[position]
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}