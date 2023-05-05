package com.betasoft.appdemo.view.adpter.compress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.ItemImageCompressBinding

class CompressAdapter(
    val lifecycleOwner: LifecycleOwner
) : PagingDataAdapter<MediaModel, PhotoItemViewHolder>(differCallback) {
    var onClickItem: ((MediaModel) -> Unit)? = null
    var updateCountSelected: (()-> Unit)? = null
    var updateStateIsSelect: (()-> Unit)? = null

    /**
     * Holds the layout positions of the selected items.
     */
    val selectedItems = ObservableArrayList<Int>()

    /**
     * Holds a Boolean indicating if multi selection is enabled. In a LiveData.
     */
    var isMultiSelectMode: MutableLiveData<Boolean> = MutableLiveData(false)
    //var isMultiSelectMode: MutableLiveData<Boolean> = MutableLiveData(true)

    override fun onBindViewHolder(holderItem: PhotoItemViewHolder, position: Int) {
        holderItem.bindTo(this, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        return PhotoItemViewHolder(
            ItemImageCompressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Called by ui. On Click.
     */
    fun viewPhoto(position: Int) {
        //viewPhotoCallback.invoke(getItem(position)?.id!!)
    }

    /**
     * Disables multi selection.
     */
    fun disableSelection() {
        selectedItems.clear()
        isMultiSelectMode.postValue(false)
        updateStateIsSelect?.invoke()
    }

    /**
     * Enables multi selection.
     */
    fun enableSelection() {
        isMultiSelectMode.postValue(true)
        updateStateIsSelect?.invoke()
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
    }

    /**
     * Get all items that are selected.
     */
    fun getAllSelected(): List<MediaModel> {
        val items = mutableListOf<MediaModel>()
        for (position in selectedItems) {
            val photo = getItem(position)
            if (photo != null) {
                items.add(photo)
            }
        }
        return items
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<MediaModel>() {

            override fun areItemsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean =
                oldItem == newItem

        }
    }

}