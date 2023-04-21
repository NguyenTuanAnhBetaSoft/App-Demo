package com.betasoft.appdemo.ui.media

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.MainActivity
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.CastMediaModel
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.FragmentMediaBinding
import com.betasoft.appdemo.ui.adpter.MediaAdapter
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.home.HomeFragmentDirections
import com.betasoft.appdemo.ui.photocast.PhotoCastActivity
import com.betasoft.appdemo.utils.Utils
import com.betasoft.appdemo.utils.bindThumbnailFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MediaFragment : AbsBaseFragment<FragmentMediaBinding>() {
    private var listItemSelected = arrayListOf<MediaModel>()

    private val args: MediaFragmentArgs by navArgs()

    @Inject
    lateinit var mediaAdapter: MediaAdapter

    private val mViewModel: MediaLocalViewModel by viewModels()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (!Utils.storagePermissionGrant(requireContext())) {
                Utils.showAlertPermissionNotGrant(
                    binding.root,
                    requireActivity(),
                    Utils.getStoragePermissions()
                )
            } else {
                when (args.param) {

                    1 -> {
                        binding.tvToolbar.text = "Videos"
                        refreshDataVideo()
                    }
                    2 -> {
                        refreshDataImage()
                        binding.tvToolbar.text = "Photos"
                    }

                }
            }
        }

    private lateinit var mLayoutManager: GridLayoutManager


    override fun getLayout(): Int {
        return R.layout.fragment_media
    }


    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.viewModel = mViewModel
        onBackPressed()

        observer()

        initRecycleView()

        binding.lPhotoSelected.btnCast.setOnClickListener {

            val intent = Intent((activity as MainActivity), PhotoCastActivity::class.java)
            intent.putParcelableArrayListExtra("listMedia", listItemSelected)
            startActivity(intent)

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
                if (mediaAdapter.isSelect()) {
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
        mLayoutManager = GridLayoutManager(requireContext(), 4, LinearLayoutManager.VERTICAL, false)
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = mediaAdapter
            layoutManager = mLayoutManager

        }

        mediaAdapter.onClickItem = {
            Log.d("6656565", "mediaClick = $it")
        }

        mediaAdapter.listSelected = {
            mViewModel.updateListItemSelected(it)
            listItemSelected.clear()
            listItemSelected.addAll(it)
        }

    }

    private fun refreshDataImage() {
        lifecycleScope.launch {
            mViewModel.fetAllImages().collectLatest { response ->
                mediaAdapter.submitData(response)
            }
        }
    }

    private fun refreshDataVideo() {
        lifecycleScope.launch {
            mViewModel.fetAllVideos().collectLatest { response ->
                mediaAdapter.submitData(response)
            }
        }
    }

    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mediaAdapter.isSelect()) {
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
        mediaAdapter.setSelect(false)
        mediaAdapter.notifyDataSetChanged()
        mediaAdapter.cleanListItemChecked()
    }

}