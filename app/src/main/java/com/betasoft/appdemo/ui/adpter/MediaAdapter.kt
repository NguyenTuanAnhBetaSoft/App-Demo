package com.betasoft.appdemo.ui.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.model.ImageLocal
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.ItemAllImageBinding
import com.betasoft.appdemo.databinding.ItemImageMyfileBinding
import javax.inject.Inject

class MediaAdapter
@Inject
constructor() : PagingDataAdapter<MediaModel, MediaAdapter.MediaViewHolder>(DiffUtils) {

    var onClickItem: ((MediaModel) -> Unit)? = null


    inner class MediaViewHolder(private val binding: ItemAllImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: MediaModel) {
            binding.apply {
                binding.item = image

                binding.root.setOnClickListener {
                    onClickItem?.invoke(image)
                }


            }
        }
    }

    object DiffUtils : DiffUtil.ItemCallback<MediaModel>() {
        override fun areItemsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val image = getItem(position)
        if (image != null) {
            holder.bind(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            ItemAllImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


}