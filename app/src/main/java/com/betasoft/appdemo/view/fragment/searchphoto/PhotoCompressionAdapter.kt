package com.betasoft.appdemo.view.fragment.searchphoto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.betasoft.appdemo.databinding.ItemPhotoCompressionBinding

class PhotoCompressionAdapter :
    PagingDataAdapter<PhotoCompression, PhotoItemViewHolder>(differCallback) {
    var onClickItem: ((Int) -> Unit)? = null
    var updateCountSelected: (() -> Unit)? = null

    /**
     * Holds the layout positions of the selected items.
     */
    val selectedItems = ObservableArrayList<Int>()

    override fun onBindViewHolder(holderItem: PhotoItemViewHolder, position: Int) {
        holderItem.bindTo(this, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        return PhotoItemViewHolder(
            ItemPhotoCompressionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Disables multi selection.
     */
    fun disableSelection() {
        selectedItems.clear()
        updateCountSelected?.invoke()
    }

    fun updatePreview() {
        updateCountSelected?.invoke()
    }

    /**
     * Add an item it the selection.
     */
    fun addItemToSelection(position: Int): Boolean = selectedItems.add(position)

    /**
     * Remove an item to the selection.
     */
    fun removeItemFromSelection(position: Int) = selectedItems.remove(position)

    /**
     * Indicate if an item is already selected.
     */
    fun isItemSelected(position: Int) = selectedItems.contains(position)

    /**
     * Indicate if an item is the last selected.
     */
    fun isLastSelectedItem(position: Int) = isItemSelected(position) && selectedItems.size == 1

    /**
     * Select all items.
     */
    fun selectAll() {
        for (i in 0 until itemCount) {
            if (!isItemSelected(i)) {
                addItemToSelection(i)
            }
        }
        updateCountSelected?.invoke()
    }

    /**
     * Get all items that are selected.
     */
    fun getAllSelected(): List<PhotoCompression> {
        val items = mutableListOf<PhotoCompression>()
        for (position in selectedItems) {
            val photo = getItem(position)
            if (photo != null) {
                items.add(photo)
            }
        }
        return items
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<PhotoCompression>() {

            override fun areItemsTheSame(
                oldItem: PhotoCompression,
                newItem: PhotoCompression
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PhotoCompression,
                newItem: PhotoCompression
            ): Boolean =
                oldItem == newItem

        }
    }

}