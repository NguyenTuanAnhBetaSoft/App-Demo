package com.betasoft.appdemo.ui.adpter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.ItemMediaBinding
import com.betasoft.appdemo.extensions.getDuration
import javax.inject.Inject

class MediaAdapter
@Inject
constructor() : PagingDataAdapter<MediaModel, MediaAdapter.MediaViewHolder>(DiffUtils) {
    var onClickItem: ((MediaModel) -> Unit)? = null

    private var select = false
    private val listItemChecked = arrayListOf<MediaModel>()
    var listSelected: ((List<MediaModel>) -> Unit)? = null
    fun setSelect(boolean: Boolean) {
        this.select = boolean
    }

    fun cleanListItemChecked() {
        listItemChecked.clear()
        listSelected?.invoke(arrayListOf())
    }

    fun isSelect() = select

    inner class MediaViewHolder(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(media: MediaModel) {

            binding.apply {
                item = media
                if (media.duration != 0L) {
                    binding.isVideo = true
                    binding.videoDuration.text = media.duration.getDuration()
                } else {
                    binding.isVideo = false
                }

                Log.d("54354", "item = ${media.duration.toString()}")


                imgRoot.setOnLongClickListener {
                    if (!select) {
                        listItemChecked.add(media)
                        setSelect(true)
                        listSelected?.invoke(listItemChecked)
                        notifyItemChanged(layoutPosition)
                        true
                    } else {
                        false
                    }
                }

            }

            val isChecked = listItemChecked.contains(media)
            binding.iconCheck.visibility = if (select) View.VISIBLE
            else View.GONE

            if (select) {
                if (isChecked) {
                    binding.iconCheck.setBackgroundResource(R.drawable.ic_check)
                } else {
                    binding.iconCheck.background = null
                }
            } else {
                binding.iconCheck.background = null
            }

            binding.imgRoot.setOnClickListener {
                if (select) {
                    if (isChecked) {
                        listItemChecked.remove(media)
                    } else {
                        listItemChecked.add(media)
                    }
                    if (listItemChecked.isEmpty()) {
                        setSelect(false)
                    }
                    notifyItemChanged(layoutPosition)
                    listSelected?.invoke(listItemChecked)
                }
                else {
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