package com.betasoft.appdemo.view.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.ItemCompressorBinding
import com.betasoft.appdemo.extensions.setOnSingClickListener
import com.betasoft.appdemo.utils.setImageSelect
import com.betasoft.appdemo.utils.setImageUnSelect

class CompressorAdapter : ListAdapter<MediaModel, CompressorAdapter.ViewHolder>(diffUtil) {

    private var positionSelect = 0
    var onViewPosition: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCompressorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemCompressorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaModel) {
            binding.item = item
            if (layoutPosition == positionSelect) {
                binding.imgRoot.setImageSelect(1)

            } else {
                binding.imgRoot.setImageUnSelect(1)
            }

            binding.imgRoot.setOnSingClickListener {
                notifyItemChanged(positionSelect)
                positionSelect = layoutPosition
                notifyItemChanged(layoutPosition)
                onViewPosition?.invoke(positionSelect)

            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MediaModel>() {
            override fun areItemsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}