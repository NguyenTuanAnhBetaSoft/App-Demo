package com.betasoft.appdemo.ui.photocast

import androidx.navigation.fragment.findNavController
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentPhotoCastBinding
import com.betasoft.appdemo.ui.base.AbsBaseFragment


class PhotoCastFragment : AbsBaseFragment<FragmentPhotoCastBinding>() {
    override fun getLayout(): Int {
        return R.layout.fragment_photo_cast
    }

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}