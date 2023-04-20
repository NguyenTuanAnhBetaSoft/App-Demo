package com.betasoft.appdemo.ui.tast2

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentTast2Binding
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.detailImage.DetailImageFragmentArgs
import com.betasoft.appdemo.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Tast2Fragment : AbsBaseFragment<FragmentTast2Binding>() {
    private val args: DetailImageFragmentArgs by navArgs()



    override fun getLayout(): Int {
        return R.layout.fragment_tast2
    }

    override fun initView() {
        binding.btnAllImage.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalMediaFragment(2))
        }

        binding.btnVideo.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalMediaFragment(1))
        }
    }

}