package com.betasoft.appdemo.ui.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.api.responseremote.ItemsItem
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.databinding.FragmentHomeBinding
import com.betasoft.appdemo.ui.adpter.ImageAdapter
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : AbsBaseFragment<FragmentHomeBinding>() {

    private val mViewModel: HomeViewModel by viewModels()

    private lateinit var mLayoutManager: GridLayoutManager

    private val imageAdapter by lazy {
        ImageAdapter()

    }

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        mViewModel.fetchImageList()
        observer()
        initRecycleView()
    }

    private fun initRecycleView() {
        mLayoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = imageAdapter
            layoutManager = mLayoutManager

        }

    }

    private fun observer() {
        mViewModel.dataAppLiveData.observe(this) {
            it?.let {
                if (it.loadingStatus == LoadingStatus.Loading) {

                }
                if (it.loadingStatus == LoadingStatus.Success) {
                    val body = (it as DataResponse.DataSuccess).body
                    imageAdapter.update(false, body.items as List<ItemsItem>?)
                    Log.d("dfadsf", "body = $body")

                }
                if (it.loadingStatus == LoadingStatus.Error) {

                } else {

                }
            }
        }

    }

}