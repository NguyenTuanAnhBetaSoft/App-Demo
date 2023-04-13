package com.betasoft.appdemo.ui.detailImagelocal

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentDetailImageLocalBinding
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.utils.bindTouchImage

class DetailImageLocalFragment : AbsBaseFragment<FragmentDetailImageLocalBinding>() {
    private val args: DetailImageLocalFragmentArgs by navArgs()

    override fun getLayout(): Int {
        return R.layout.fragment_detail_image_local
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.touchImageview.apply {
            bindTouchImage(args.param.filePath)
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
    }

}