package com.betasoft.appdemo.ui.base.popup

import android.content.Context
import android.view.View
import android.widget.ListPopupWindow
import com.betasoft.appdemo.R

class ListActionPopup(private val context: Context) {
    private val popupMenu: ListPopupWindow = ListPopupWindow(context)

    fun showPopup(
        anchor: View,
        array: MutableList<ActionModel>,
        onActionClickListener: ActionAdapter.OnActionClickListener?
    ) {
        val adapter = ActionAdapter(array, object : ActionAdapter.OnActionClickListener {
            override fun onItemActionClick(position: Int) {
                popupMenu.dismiss()
                onActionClickListener?.onItemActionClick(position)
            }
        })
        popupMenu.setAdapter(adapter)
        popupMenu.width = context.resources.getDimension(R.dimen.popup_dialog_width).toInt()
        popupMenu.height = ListPopupWindow.WRAP_CONTENT
        popupMenu.anchorView = anchor
        popupMenu.isModal = true
        popupMenu.show()
    }

    fun isShowing(): Boolean {
        return popupMenu.isShowing
    }

    fun dismiss() {
        popupMenu.dismiss()
    }
}