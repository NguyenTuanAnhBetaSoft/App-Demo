package com.betasoft.appdemo.view.fragment.searchphoto

import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.betasoft.appdemo.databinding.ItemPhotoCompressionBinding

class PhotoItemViewHolder(
    private val binding: ItemPhotoCompressionBinding
) : RecyclerView.ViewHolder(
    binding.root
) {
    var photo: PhotoCompression? = null
    private lateinit var adapter: PhotoCompressionAdapter

    /**
     * Binds the parent adapter and the photo to the ViewHolder.
     */
    fun bindTo(adapter: PhotoCompressionAdapter, photo: PhotoCompression?) {
        this.photo = photo
        this.adapter = adapter

        binding.apply {
//            imgRoot.setOnClickListener {
//                adapter.onClickItem?.invoke(layoutPosition)
//            }
//
//            btnIconCheck.setOnClickListener {
//                setItemChecked(!adapter.isItemSelected(layoutPosition))
//            }

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
//        val isSelected = adapter.isItemSelected(layoutPosition)
//        if (isSelected) {
//            binding.iconCheck.setBackgroundResource(R.drawable.ic_selected_blue)
//        } else {
//            binding.iconCheck.setBackgroundResource(R.drawable.ic_select_white)
//        }

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