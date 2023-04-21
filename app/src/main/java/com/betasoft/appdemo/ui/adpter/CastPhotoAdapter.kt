package com.betasoft.appdemo.ui.adpter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.ItemCastPhotoBinding
import com.betasoft.appdemo.utils.setImageSelect
import com.betasoft.appdemo.utils.setImageUnSelect

class CastPhotoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<MediaModel>()
    private var positionSelect = 0

    var onClickItem: ((MediaModel) -> Unit)? = null

    fun mediaSelect(position: Int) {
        positionSelect = position
    }


    @SuppressLint("NotifyDataSetChanged")
    fun update(newData: List<MediaModel>?) {
        data.clear()
        if (newData != null && newData.isNotEmpty()) {
            data.addAll(newData)
        }
        notifyDataSetChanged()
        onClickItem?.invoke(data[0])
        positionSelect = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemCastPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MediaViewHolder) {
            holder.bind(position)
        }
    }

    inner class MediaViewHolder(private val item: ItemCastPhotoBinding) :
        RecyclerView.ViewHolder(item.root) {
        fun bind(position: Int) {

            item.item = data[position]
            Log.d("65656565", "position = $position}")

            if (position == positionSelect) {
                Log.d("65656565", "select")
                item.imgRoot.setImageSelect(1)

            } else {
                Log.d("65656565", "unselect")
                item.imgRoot.setImageUnSelect(1)
            }

            item.imgRoot.setOnClickListener {
                notifyItemChanged(positionSelect)
                positionSelect = position
                notifyItemChanged(position)
                onClickItem?.invoke(data[position])

            }


        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}