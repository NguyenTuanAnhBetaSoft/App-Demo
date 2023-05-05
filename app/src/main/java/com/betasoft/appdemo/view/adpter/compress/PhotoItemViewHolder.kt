package com.betasoft.appdemo.view.adpter.compress

import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.ItemImageCompressBinding

class PhotoItemViewHolder(
    private val binding: ItemImageCompressBinding
) : RecyclerView.ViewHolder(
    binding.root
) {
    var photo: MediaModel? = null
    private lateinit var adapter: CompressAdapter

    /**
     * Binds the parent adapter and the photo to the ViewHolder.
     */
    fun bindTo(adapter: CompressAdapter, photo: MediaModel?) {
        this.photo = photo
        this.adapter = adapter

        binding.apply {
            imgRootCompress.setOnClickListener {
                if (adapter.isMultiSelectMode.value!!) {
                    // If the item clicked is the last selected item
                    if (adapter.isLastSelectedItem(layoutPosition)) {
                        adapter.disableSelection()
                        //adapter.updateStateIsSelect?.invoke()
                        //
                        return@setOnClickListener
                    }
                    // Set checked if not already checked
                    setItemChecked(!adapter.isItemSelected(layoutPosition))
                } else {
                    //adapter.viewPhoto(layoutPosition)
                    adapter.onClickItem?.invoke(photo!!)
                }
            }

            imgRootCompress.setOnLongClickListener {
                if (!adapter.isMultiSelectMode.value!!) {
                    adapter.enableSelection()
                    //
                    setItemChecked(true)
                    //adapter.updateStateIsSelect?.invoke()
                }
                true
            }

            adapter.isMultiSelectMode.observe(adapter.lifecycleOwner) {
                if (it) { // When selection gets enabled, show the checkbox
                    //checkBox.show()
                    //iconCheck.setBackgroundResource(R.drawable.ic_check_circle)
                } else {
                    //iconCheck.setBackgroundResource(R.drawable.ic_radio_uncheck_white)
                }
            }

            adapter.selectedItems.addOnListChangedCallback(onSelectedItemsChanged)

            listChanged()
            item = photo
        }


    }

    /**
     * Listener for changes in selected images.
     * Calls [listChanged] whatever happens.
     */
    private val onSelectedItemsChanged =
        object : ObservableList.OnListChangedCallback<ObservableList<Int>>() {

            override fun onChanged(sender: ObservableList<Int>?) {
                listChanged()
            }

            override fun onItemRangeChanged(
                sender: ObservableList<Int>?,
                positionStart: Int,
                itemCount: Int
            ) {
                listChanged()
            }

            override fun onItemRangeInserted(
                sender: ObservableList<Int>?,
                positionStart: Int,
                itemCount: Int
            ) {
                listChanged()
            }

            override fun onItemRangeMoved(
                sender: ObservableList<Int>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                listChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableList<Int>?,
                positionStart: Int,
                itemCount: Int
            ) {
                listChanged()
            }

        }

    private fun listChanged() {
        val isSelected = adapter.isItemSelected(layoutPosition)
        //val padding = if (isSelected) 20 else 0

        //iconCheck.isChecked = isSelected
        if (isSelected) {
            binding.iconCheckCompress.setBackgroundResource(R.drawable.ic_check_circle)
        } else {
            binding.iconCheckCompress.setBackgroundResource(R.drawable.ic_radio_uncheck_white)
        }
        //imageRoot.setPadding(padding)
    }

    private fun setItemChecked(checked: Boolean) {
        layoutPosition.let {
            if (checked) {
                adapter.addItemToSelection(it)
            } else {
                adapter.removeItemFromSelection(it)
            }
            adapter.updateCountSelected?.invoke()
        }
    }

}