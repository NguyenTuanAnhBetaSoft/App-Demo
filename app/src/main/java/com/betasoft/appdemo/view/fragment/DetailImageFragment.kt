package com.betasoft.appdemo.view.fragment

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.databinding.FragmentDetailImageBinding
import com.betasoft.appdemo.utils.ToastUtils
import com.betasoft.appdemo.utils.bindTouchImage
import com.betasoft.appdemo.view.base.AbsBaseFragment
import com.betasoft.appdemo.view.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailImageFragment : AbsBaseFragment<FragmentDetailImageBinding>() {
    private val args: DetailImageFragmentArgs by navArgs()
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun getLayout(): Int {
        return R.layout.fragment_detail_image
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        observer()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.touchImageview.apply {
            bindTouchImage(args.param.image_url)
            setOnTouchListener { view, event ->
                var result = true
                //can scroll horizontally checks if there's still a part of the image
                //that can be scrolled until you reach the edge
                if (event.pointerCount >= 2 || view.canScrollHorizontally(1) && canScrollHorizontally(
                        -1
                    )
                ) {
                    //multi-touch event
                    result = when (event.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            // Disallow RecyclerView to intercept touch events.
                            parent.requestDisallowInterceptTouchEvent(true)
                            // Disable touch on view
                            false
                        }
                        MotionEvent.ACTION_UP -> {
                            // Allow RecyclerView to intercept touch events.
                            parent.requestDisallowInterceptTouchEvent(false)
                            true
                        }
                        else -> true
                    }
                }
                result
            }

        }


        binding.btnDownLoad.setOnClickListener {
            homeViewModel.downloadImageUrl(
                url = args.param.image_url.toString(),
                name = args.param.id.toString(),
                haveSave = true,
                context = requireContext(),
                nameAuthor = args.param.userProfile!!.name.toString(),
                prompt = args.param.prompt.toString()
            )
        }


    }

    private fun observer() {
        homeViewModel.downloadImageLiveData.observe(this) {
            it?.let {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        ToastUtils.getInstance(requireContext()).showToast("DownloadSuccess")
                        val body = (it as DataResponse.DataSuccess).body
                        homeViewModel.insertImageLocal(body)
                    }
                    LoadingStatus.Error -> {
                        ToastUtils.getInstance(requireContext()).showToast("image already exists")
                    }
                    else -> {

                    }
                }
            }
        }
    }

}