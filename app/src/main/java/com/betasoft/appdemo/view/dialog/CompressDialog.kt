package com.betasoft.appdemo.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.CompressDialogBinding
import com.betasoft.appdemo.view.base.dialog.BaseDialog
import com.betasoft.appdemo.view.base.dialog.BaseListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CompressDialog : BaseDialog<CompressDialogBinding, CompressDialog.IListener>() {

    private var key = arrayListOf<String>()

    override fun initViewModel() {

    }

    override fun initView() {

    }

    override fun getLayout(): Int {
        return R.layout.compress_dialog
    }

    companion object {
        const val KEY_ADD_FROM_URL = "KeyDialogCompress"
        fun create(key: ArrayList<String>, listener: IListener): CompressDialog {
            val dialog = CompressDialog()
            val bundle = Bundle()
            bundle.putStringArrayList(KEY_ADD_FROM_URL, key)
            dialog.listener = listener
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.listenerDismissDialog()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

    }

    interface IListener : BaseListener {
        fun listenerAddFailed()
        fun listenerDismissDialog()
        fun listenSuccess()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setWindowAnimations(R.style.DialogFullscreen)
        setStyle(STYLE_NORMAL, R.style.DialogFullscreen)
        key = requireArguments().getStringArrayList(KEY_ADD_FROM_URL) as ArrayList<String>

    }

    private fun observer() {

    }


}

