package com.betasoft.appdemo.view.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.FragmentPhotoCastBinding
import com.betasoft.appdemo.view.adpter.CastPhotoAdapter
import com.betasoft.appdemo.view.base.AbsBaseFragment
import com.betasoft.appdemo.utils.bindTouchImage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File


class PhotoCastFragment : AbsBaseFragment<FragmentPhotoCastBinding>() {

    private val listMedia = arrayListOf<MediaModel>()

    private var compressedImage: File? = null

    //private val args: PhotoCastFragmentArgs by navArgs()

    private val castPhotoAdapter by lazy {
        CastPhotoAdapter().apply {
            onClickItem = {
                Log.d("t45454", "it = $it")
                touchImageView(it)


            }


        }
    }
    override fun getLayout(): Int {
        return R.layout.fragment_photo_cast
    }


    @Suppress("DEPRECATION")
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
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = castPhotoAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        castPhotoAdapter.submitList(listMedia)
        touchImageView(listMedia[0])
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun touchImageView(media: MediaModel) {
        binding.tvToolbar.text = media.title
        binding.touchImageview.bindTouchImage(media.uri.toString())


    }



}