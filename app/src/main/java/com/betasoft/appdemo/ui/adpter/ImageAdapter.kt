package com.betasoft.appdemo.ui.adpter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.api.responseremote.ItemsItem
import com.betasoft.appdemo.databinding.ItemImageBinding
import com.betasoft.appdemo.extensions.getDimenResources
import com.betasoft.appdemo.extensions.setMargin
import com.betasoft.appdemo.extensions.setOnSingClickListener

class ImageAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val data = mutableListOf<ItemsItem>()
    private var select = false
    private val listItemChecked = arrayListOf<ItemsItem>()
    var listSelect: ((List<ItemsItem>) -> Unit)? = null
    var listSelected: ((List<ItemsItem>) -> Unit)? = null


    var onClickItem: ((ItemsItem) -> Unit)? = null
    var onClickDownLoad: ((ItemsItem) -> Unit)? = null
    fun setSelect(boolean: Boolean) {
        this.select = boolean
    }

    fun cleanListItemChecked() {
        listItemChecked.clear()
        listSelected?.invoke(arrayListOf())
    }

    fun isSelect() =select

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

        if (select) {
            listSelect?.invoke(data)
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
            Log.d("fsdfsd", "position = $position")
            item.item = data[position]

            item.btnDownLoad.setOnClickListener {
                onClickDownLoad?.invoke(data[position])
            }

            item.rootLayout.setOnLongClickListener {
                if (!select) {
                    listItemChecked.add(data[position])
                    Log.d("fsdfsd", "listItemchecked = $listItemChecked")
                    setSelect(true)
                    listSelected?.invoke(listItemChecked)
                    listSelect?.invoke(data)
                    notifyItemRangeChanged(0, data.size)
                    true
                } else false
            }



            val isChecked = listItemChecked.contains(data[position])
            Log.d("fsdfsd", "ischecked = $isChecked")
            item.iconCheck.visibility = if (select) View.VISIBLE
            else View.GONE

            if (select) {
                if (isChecked) {
                    item.iconCheck.setBackgroundResource(R.drawable.ic_check_circle)
                    item.rootLayout.setMargin(itemView.context.getDimenResources(R.dimen.dp_16))

                } else {
                    item.iconCheck.setBackgroundResource(R.drawable.ic_radio_uncheck_white)
                    item.rootLayout.setMargin(0)
                }
            } else {
                item.rootLayout.setMargin(0)
            }

            item.rootLayout.setOnSingClickListener {
                if (select) {
                    if (isChecked) {
                        listItemChecked.remove(data[position])
                    } else {
                        listItemChecked.add(data[position])
                    }

                    notifyItemChanged(position)
                    listSelected?.invoke(listItemChecked)

                } else {
                    onClickItem?.invoke(data[position])

                }
                Log.d("fsdfs", "listselected2 = $listItemChecked")
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}