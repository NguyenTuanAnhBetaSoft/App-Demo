package com.betasoft.appdemo.view.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialog<V : ViewDataBinding, B : BaseListener> : DialogFragment() {

    protected lateinit var binding: V
    private val isBindingInitialized get() = this::binding.isInitialized
    var listener: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        binding.lifecycleOwner = this
        if (listener != null) {
            initView()
        } else {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBindingInitialized) {
            binding.unbind()
        }
    }

    abstract fun initViewModel()

    abstract fun initView(

    )

    abstract fun getLayout(): Int
}