package com.betasoft.appdemo.ui.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.databinding.ItemImageMyfileBinding
import javax.inject.Inject

class MyFileAdapter
@Inject
constructor() : PagingDataAdapter<ImageLocal, MyFileAdapter.ImageLocalViewHolder>(DiffUtils) {

    private var listener: OnClickItemListeners? = null

    fun setListener(listener: OnClickItemListeners) {
        this.listener = listener
    }

    inner class ImageLocalViewHolder(private val binding: ItemImageMyfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ImageLocal) {
            binding.apply {
                binding.item = image
                binding.root.setOnClickListener {
                    listener?.onClickItemListeners(image)
                }
            }
        }
    }

    object DiffUtils : DiffUtil.ItemCallback<ImageLocal>() {
        override fun areItemsTheSame(oldItem: ImageLocal, newItem: ImageLocal): Boolean {
            return oldItem.imageId == newItem.imageId
        }

        override fun areContentsTheSame(oldItem: ImageLocal, newItem: ImageLocal): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ImageLocalViewHolder, position: Int) {
        val image = getItem(position)
        if (image != null) {
            holder.bind(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageLocalViewHolder {
        return ImageLocalViewHolder(
            ItemImageMyfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    interface OnClickItemListeners {
        fun onClickItemListeners(imageLocal: ImageLocal)
    }
}