package com.betasoft.appdemo.ui.media

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentMediaBinding
import com.betasoft.appdemo.ui.adpter.MediaAdapter
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.utils.ToastUtils
import com.betasoft.appdemo.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MediaFragment : AbsBaseFragment<FragmentMediaBinding>() {
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
                ToastUtils.getInstance(requireContext()).showToast("ahihih")
            }
        }

    private lateinit var mLayoutManager: GridLayoutManager


    override fun getLayout(): Int {
        return R.layout.fragment_media
    }

    override fun initView() {
        observer()
        mViewModel.fetAllImage()
        initRecycleView()
        refreshData()

        mediaAdapter.onClickItem = {

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

    private fun observer() {

    }

    private fun initRecycleView() {
        mLayoutManager = GridLayoutManager(requireContext(), 4, LinearLayoutManager.VERTICAL, false)
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = mediaAdapter
            layoutManager = mLayoutManager

        }

    }

    private fun refreshData() {
        lifecycleScope.launch {
            mViewModel.fetAllImage().collectLatest { response ->
                Log.d("gffds", "respon ${response.toString()}")
                mediaAdapter.submitData(response)
            }
        }
    }

}