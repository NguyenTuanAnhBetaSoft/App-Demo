package com.betasoft.appdemo.ui.photocast

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.MainActivity
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.FragmentPhotoCastBinding
import com.betasoft.appdemo.ui.adpter.CastPhotoAdapter
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.media.MediaFragmentArgs
import com.betasoft.appdemo.utils.bindTouchImage
import javax.inject.Inject


class PhotoCastFragment : AbsBaseFragment<FragmentPhotoCastBinding>() {

    private lateinit var mLayoutManager: LinearLayoutManager

    private val listMedia = arrayListOf<MediaModel>()

    //private val args: PhotoCastFragmentArgs by navArgs()

    private val castPhotoAdapter by lazy {
        CastPhotoAdapter().apply {
            onClickItem = {
                Log.d("t45454", "it = $it")
                touchImageView(it.uri.toString())

            }


        }
    }
    override fun getLayout(): Int {
        return R.layout.fragment_photo_cast
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        val list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().intent.getParcelableArrayListExtra("listMedia", MediaModel::class.java)
        } else {
            requireActivity().intent.getParcelableArrayListExtra("listMedia")
        }
        if (list != null) {
            listMedia.addAll(list)
        }

        binding.tvToolbar.text = listMedia[0].title



        initRecycleView()
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }

        binding.touchImageview.apply {
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

    private fun initRecycleView() {
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = castPhotoAdapter
            layoutManager = mLayoutManager

        }

        castPhotoAdapter.update(listMedia)
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun touchImageView(uri: String) {
        binding.touchImageview.bindTouchImage(uri)

    }

}