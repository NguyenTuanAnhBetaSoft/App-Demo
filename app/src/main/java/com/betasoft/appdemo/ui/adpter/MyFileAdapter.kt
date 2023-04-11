package com.betasoft.appdemo.ui.adpter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.data.api.responseremote.ItemsItem
import com.betasoft.appdemo.databinding.ItemImageMyfileBinding

class MyFileAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var onClickItemListeners: OnClickItemListeners
    val data = mutableListOf<ImageLocal>()

    @SuppressLint("NotifyDataSetChanged")
    fun update(imageLocals: List<ImageLocal>?) {
        data.clear()
        if (imageLocals != null && imageLocals.isNotEmpty()) {
            data.addAll(imageLocals)
        }
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemImageMyfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LocationViewHolder) {
            holder.bind(position)
        }
    }

    inner class LocationViewHolder(private val item: ItemImageMyfileBinding) :
        RecyclerView.ViewHolder(item.root) {
        fun bind(position: Int) {
            item.item = data[position]
            item.root.setOnClickListener {
                onClickItemListeners.onClickedItem(data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnClickItemListeners {
        fun onClickedItem(param: ImageLocal)
    }
}