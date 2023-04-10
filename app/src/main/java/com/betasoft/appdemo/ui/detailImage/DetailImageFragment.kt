package com.betasoft.appdemo.ui.detailImage

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.databinding.FragmentDetailImageBinding
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.openart.HomeViewModel
import com.betasoft.appdemo.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailImageFragment : AbsBaseFragment<FragmentDetailImageBinding>() {
    private val args: DetailImageFragmentArgs by navArgs()
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun getLayout(): Int {
        return R.layout.fragment_detail_image
    }

    override fun initView() {
        observer()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.item = args.imageUrl

        binding.btnDownLoad.setOnClickListener {
            homeViewModel.downloadImageUrl(
                url = args.imageUrl,
                name = args.idImage,
                haveSave = true,
                context = requireContext()
            )
        }


    }

    private fun observer() {
        homeViewModel.downloadImageLiveData.observe(this) {
            it.let {
                if (it.loadingStatus == LoadingStatus.Success) {
                    ToastUtils.getInstance(requireContext()).showToast("DownloadSuccess")
                }
            }
        }
    }

}