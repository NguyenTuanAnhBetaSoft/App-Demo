package com.betasoft.appdemo.view.fragment.detailalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentDetailAlbumBinding
import com.betasoft.appdemo.databinding.FragmentDetailImageBinding
import com.betasoft.appdemo.view.base.AbsBaseFragment
import com.betasoft.appdemo.view.fragment.searchphoto.PhotoCompressionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailAlbumFragment : AbsBaseFragment<FragmentDetailAlbumBinding>() {
    private val mViewModel by viewModels<DetailAlbumViewModel>()
    private val args: DetailAlbumFragmentArgs by navArgs()

    private var searchJob: Job? = null

    private val photoCompressionAdapter by lazy {
        PhotoCompressionAdapter()
    }


    override fun getLayout(): Int {
        return R.layout.fragment_detail_album
    }

    override fun initView() {
        initRecycleView()
        searchJob = lifecycleScope.launch {
            mViewModel.searchPhotosByBucketId(args.bucketId.toString())
            mViewModel.listPhotos.collectLatest { response ->
                photoCompressionAdapter.submitData(response)
            }
        }
    }

    private fun initRecycleView() {
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = photoCompressionAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        }

    }

}