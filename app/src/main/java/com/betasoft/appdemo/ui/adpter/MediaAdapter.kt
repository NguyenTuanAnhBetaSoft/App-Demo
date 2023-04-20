package com.betasoft.appdemo.ui.adpter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.ItemMediaBinding
import com.betasoft.appdemo.extensions.getDuration
import javax.inject.Inject

class MediaAdapter
@Inject
constructor() : PagingDataAdapter<MediaModel, MediaAdapter.MediaViewHolder>(DiffUtils) {

    var onClickItem: ((MediaModel) -> Unit)? = null


    inner class MediaViewHolder(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(media: MediaModel) {

            binding.apply {
                binding.item = media
                if (media.duration != 0L) {
                    binding.isVideo = true
                    binding.videoDuration.text = media.duration.getDuration()
                } else {
                    binding.isVideo = false
                }

                Log.d("54354", "item = ${media.duration.toString()}")

                binding.imgRoot.setOnClickListener {
                    onClickItem?.invoke(media)
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
            ItemMediaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


}