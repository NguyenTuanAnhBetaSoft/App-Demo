package com.betasoft.appdemo.view.fragment.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentAlbumBinding
import com.betasoft.appdemo.view.base.AbsBaseFragment
import com.betasoft.appdemo.view.fragment.searchphoto.SearchPhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : AbsBaseFragment<FragmentAlbumBinding>() {
    private val mViewModel by viewModels<AlbumPhotoViewModel>()

    private val albumPhotoAdapter by lazy {
        AlbumPhotoAdapter()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_album
    }

    override fun initView() {
        mViewModel.loadAlbums()
        initRecycleView()

        mViewModel.albumsLiveData.observe(this) {
            it?.let {
                Log.d("423423423", "live data $it")
                if (it.isNotEmpty()) {
                    albumPhotoAdapter.submitList(it)
                }

            }
        }

    }

    private fun initRecycleView() {
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = albumPhotoAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, false)
        }

        albumPhotoAdapter.onClickItem = {
          findNavController().navigate(AlbumFragmentDirections.actionGlobalDetailAlbumFragment(it.id))
        }

    }

}