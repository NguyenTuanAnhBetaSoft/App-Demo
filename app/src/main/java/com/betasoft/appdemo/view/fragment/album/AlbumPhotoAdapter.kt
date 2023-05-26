package com.betasoft.appdemo.view.fragment.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.ItemAlbumPhotoBinding
import com.betasoft.appdemo.databinding.ItemCastPhotoBinding
import com.betasoft.appdemo.view.fragment.searchphoto.Album

class AlbumPhotoAdapter : ListAdapter<Album, AlbumPhotoAdapter.ViewHolder>(diffUtil) {

    var onClickItem: ((Album) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemAlbumPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Album) {
            binding.item = item

            binding.myLayoutRoot.setOnClickListener {
                onClickItem?.invoke(item)
            }

        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem == newItem
            }
        }
    }

}