package com.betasoft.appdemo.view.fragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.databinding.FragmentCompressorBinding
import com.betasoft.appdemo.utils.ToastUtils
import com.betasoft.appdemo.utils.Utils.getReadableFileSize
import com.betasoft.appdemo.view.adpter.CompressorAdapter
import com.betasoft.appdemo.view.base.AbsBaseFragment
import com.betasoft.appdemo.view.dialog.CompressDialog
import com.betasoft.appdemo.view.viewmodel.CompressorViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CompressorFragment : AbsBaseFragment<FragmentCompressorBinding>() {
    private val args: CompressorFragmentArgs by navArgs()

    private val mViewModel: CompressorViewModel by viewModels()

    private val imagePathListRoot = mutableListOf<MediaModel>()
    private val imagePathListCache = mutableListOf<File>()

    private val compressAdapter by lazy {
        CompressorAdapter()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_compressor
    }

    override fun initView() {

        observe()
        onBackPressed()
        imagePathListRoot.addAll(args.param.listMedeaModel)
        mViewModel.compressCache(imagePathListRoot, 50)

        initRecycleView()

        binding.btnCompressed.setOnClickListener {
            mViewModel.compressImages(
                listImageCache = imagePathListCache,
                isKeepImage = binding.checkBox.isChecked,
                listImageRoot = imagePathListRoot
            )
        }



        compressAdapter.onViewPosition = {
            showInfoImage(it)
        }

        binding.toolbar.setNavigationOnClickListener {
            //mViewModel.deleteCache(requireContext())
            findNavController().popBackStack()
        }

        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {


            }
        }

        binding.btnChangeQuality.setOnClickListener {
            mViewModel.compressCache(
                imagePathListRoot,
                binding.edtQuality.text.toString().toInt()
            )
        }

    }

    private fun initRecycleView() {
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = compressAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }


    }

    private fun showInfoImage(int: Int) {
        binding.tvSizeActual.text = String.format(
            "Size Actual : %s",
            getReadableFileSize(imagePathListRoot[int].file!!.length())
        )
//        binding.tvResolutionActual.text =
//            buildString {
//                append("Resolution: ")
//                append(imagePathList[int].uri.getImageDimensions(requireContext()))
//            }

        binding.tvSizeCompressed.text = String.format(
            "Size Compressed : %s",
            getReadableFileSize(imagePathListCache[int].length())
        )
//        binding.tvResolutionCompressed.text =
//            buildString {
//                append("Resolution: ")
//                append(imagePathListCompressed[int].toUri().getImageDimensions(requireContext()))
//            }
    }

    private fun observe() {
        mViewModel.compressCacheImageListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        val body = (it as DataResponse.DataSuccess).body
                        imagePathListCache.clear()
                        imagePathListCache.addAll(body!!)
                        compressAdapter.submitList(imagePathListRoot)
                        showInfoImage(0)
                        ToastUtils.getInstance(requireContext()).showToast("Save cache success")

                    }

                    LoadingStatus.Loading -> {

                    }

                    LoadingStatus.Error -> {

                    }

                    else -> {

                    }
                }
            }

        }

        mViewModel.compressImageListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        val body = (it as DataResponse.DataSuccess).body
                        ToastUtils.getInstance(requireContext()).showToast("image saved")
                    }

                    LoadingStatus.Loading -> {

                    }

                    LoadingStatus.Error -> {
                        ToastUtils.getInstance(requireContext()).showToast("save fail")
                    }

                    else -> {

                    }
                }
            }

        }


    }

    private fun createDialogCompress(key: ArrayList<String>) {
        CompressDialog.create(key, object : CompressDialog.IListener {
            override fun listenerAddFailed() {
            }

            override fun listenerDismissDialog() {
            }

            override fun listenSuccess() {
            }
        }).show(parentFragmentManager, "Compress Dialog")
    }


    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
}