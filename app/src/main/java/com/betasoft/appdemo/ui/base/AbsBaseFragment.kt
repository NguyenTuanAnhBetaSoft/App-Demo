package com.betasoft.appdemo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.betasoft.appdemo.MainActivity

abstract class AbsBaseFragment<V : ViewDataBinding> : Fragment() {
    lateinit var binding: V
    private var mView: View? = null
    protected val mainActivity by lazy {
        requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (mView != null) {
            mView
        } else {
            binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
            binding.lifecycleOwner = this
            mView = binding.root
            initView()
            binding.root
        }

    }

    abstract fun getLayout(): Int
    abstract fun initView()
}