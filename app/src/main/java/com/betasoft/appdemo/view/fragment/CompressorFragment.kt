package com.betasoft.appdemo.view.fragment

import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.databinding.FragmentCompressorBinding
import com.betasoft.appdemo.utils.Utils.getImageDimensions
import com.betasoft.appdemo.utils.Utils.getReadableFileSize
import com.betasoft.appdemo.view.adpter.CompressorAdapter
import com.betasoft.appdemo.view.base.AbsBaseFragment
import com.betasoft.appdemo.view.viewmodel.CompressorViewModel
import java.io.File

class CompressorFragment : AbsBaseFragment<FragmentCompressorBinding>() {
    private val args: CompressorFragmentArgs by navArgs()

    private val mViewModel: CompressorViewModel by viewModels()

    private val imagePathList = mutableListOf<MediaModel>()
    private val imagePathListCompressed = mutableListOf<File>()

    private val compressAdapter by lazy {
        CompressorAdapter()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_compressor
    }

    override fun initView() {
        observe()
        onBackPressed()
        imagePathList.addAll(args.param.listMedeaModel)
        mViewModel.test(imagePathList, true, 50, requireContext())

        initRecycleView()

        binding.btnCompressed.setOnClickListener {
            mViewModel.compressImages(imagePathListCompressed, true, 50, requireContext())
        }

        binding.btnDeleteCache.setOnClickListener {

        }

        binding.btnSaveImage.setOnClickListener {
        }

        compressAdapter.onViewPosition = {
            showInfoImage(it)
        }

        binding.toolbar.setNavigationOnClickListener {
            mViewModel.deleteCache(requireContext())
            findNavController().popBackStack()
        }

    }

    private fun initRecycleView() {
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = compressAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        compressAdapter.submitList(imagePathList)
    }

    private fun showInfoImage(int: Int) {
        binding.tvSizeActual.text = String.format(
            "Size Actual : %s",
            getReadableFileSize(imagePathList[int].file!!.length())
        )
        binding.tvResolutionActual.text =
            buildString {
                append("Resolution: ")
                append(imagePathList[int].uri.getImageDimensions(requireContext()))
            }

        binding.tvSizeCompressed.text = String.format(
            "Size Compressed : %s",
            getReadableFileSize(imagePathListCompressed[int].length())
        )
        binding.tvResolutionCompressed.text =
            buildString {
                append("Resolution: ")
                append(imagePathListCompressed[int].toUri().getImageDimensions(requireContext()))
            }
    }

    private fun observe() {
        mViewModel.compressImageListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        val body = (it as DataResponse.DataSuccess).body
                        imagePathListCompressed.clear()
                        imagePathListCompressed.addAll(body!!)
                        showInfoImage(0)
                    }

                    LoadingStatus.Error -> {

                    }

                    else -> {

                    }
                }
            }

        }


    }

    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mViewModel.deleteCache(requireContext())
                findNavController().popBackStack()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
}