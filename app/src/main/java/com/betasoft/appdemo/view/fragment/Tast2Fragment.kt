package com.betasoft.appdemo.view.fragment

import androidx.navigation.fragment.findNavController
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentTast2Binding
import com.betasoft.appdemo.view.base.AbsBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Tast2Fragment : AbsBaseFragment<FragmentTast2Binding>() {

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

        binding.btnChooseCompressed.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalChooseCompressorFragment3())
        }

        binding.btnSearchPhoto.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalSearchPhotoFragment())
        }

        binding.btnAlbum.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalAlbumFragment())
        }
    }

}