package com.betasoft.appdemo.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.CastMediaModel
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.FragmentChooseCompressorBinding
import com.betasoft.appdemo.utils.Utils
import com.betasoft.appdemo.utils.bindThumbnailFile
import com.betasoft.appdemo.view.adpter.ChooseCompressorAdapter
import com.betasoft.appdemo.view.base.AbsBaseFragment
import com.betasoft.appdemo.view.viewmodel.ChooseCompressorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChooseCompressorFragment : AbsBaseFragment<FragmentChooseCompressorBinding>() {
    private var listItemSelected = arrayListOf<MediaModel>()


    @Inject
    lateinit var chooseCompressorAdapter: ChooseCompressorAdapter

    private val mViewModel: ChooseCompressorViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (!Utils.storagePermissionGrant(requireContext())) {
                Utils.showAlertPermissionNotGrant(
                    binding.root,
                    requireActivity(),
                    Utils.getStoragePermissions()
                )
            } else {
                refreshDataImage()
                binding.tvToolbar.text = getString(R.string.choose_image)
            }
        }


    override fun getLayout(): Int {
        return R.layout.fragment_choose_compressor
    }


    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.viewModel = mViewModel
        onBackPressed()

        observer()

        initRecycleView()

        binding.lPhotoSelected.btnCast.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionGlobalCompressorFragment(
                    CastMediaModel(listItemSelected)
                )
            )

        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestStoragePermission()
    }

    private fun requestStoragePermission() {
        resultLauncher.launch(
            Utils.getStoragePermissions()
        )
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        mViewModel.listItemSelectedLiveData.observe(this) {
            it?.let {
                if (chooseCompressorAdapter.isSelect()) {
                    mViewModel.isSelect(true)
                    val total = it.size
                    binding.lPhotoSelected.tvPtSelected.text = "$total photos selected"
                } else {
                    mViewModel.isSelect(false)
                }
            }
        }

        mViewModel.isSelectLiveData.observe(this) { isSelected ->
            if (isSelected) {
                binding.lPhotoSelected.imageRoot.visibility = View.GONE
                binding.lPhotoSelected.imageRoot2.visibility = View.GONE
                binding.lPhotoSelected.imageRoot3.visibility = View.GONE

                for (i in 0 until minOf(listItemSelected.size, 3)) {
                    val imageRoot = when (i) {
                        0 -> binding.lPhotoSelected.imageRoot
                        1 -> binding.lPhotoSelected.imageRoot2
                        else -> binding.lPhotoSelected.imageRoot3
                    }
                    imageRoot.visibility = View.VISIBLE
                    imageRoot.bindThumbnailFile(listItemSelected[listItemSelected.lastIndex - i])
                }
            }
        }

    }

    private fun initRecycleView() {
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = chooseCompressorAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 4, LinearLayoutManager.VERTICAL, false)

        }

        chooseCompressorAdapter.onClickItem = {
            //findNavController().navigate(HomeFragmentDirections.actionGlobalCompressorFragment(it))
        }

        chooseCompressorAdapter.listSelected = {
            mViewModel.updateListItemSelected(it)
            listItemSelected.clear()
            listItemSelected.addAll(it)
        }

    }

    private fun refreshDataImage() {
        lifecycleScope.launch {
            mViewModel.fetAllImages().collectLatest { response ->
                chooseCompressorAdapter.submitData(response)
            }
        }
    }

    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (chooseCompressorAdapter.isSelect()) {
                    updateUnSelect()
                } else {
                    if (isAdded) {
                        findNavController().popBackStack()
                    }
                }
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUnSelect() {
        mViewModel.isSelect(false)
        chooseCompressorAdapter.setSelect(false)
        chooseCompressorAdapter.notifyDataSetChanged()
        chooseCompressorAdapter.cleanListItemChecked()
    }

}