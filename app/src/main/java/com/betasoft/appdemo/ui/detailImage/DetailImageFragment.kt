package com.betasoft.appdemo.ui.detailImage

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentDetailImageBinding
import com.betasoft.appdemo.ui.base.AbsBaseFragment


class DetailImageFragment : AbsBaseFragment<FragmentDetailImageBinding>() {
    private val args: DetailImageFragmentArgs by navArgs()
    override fun getLayout(): Int {
        return R.layout.fragment_detail_image
    }

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.item = args.param
    }

}