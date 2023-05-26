package com.betasoft.appdemo.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentTestBinding
import com.betasoft.appdemo.view.base.AbsBaseFragment



class TestFragment : AbsBaseFragment<FragmentTestBinding>() {
    override fun getLayout(): Int {
        return R.layout.fragment_test
    }

    override fun initView() {
        binding.btn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}