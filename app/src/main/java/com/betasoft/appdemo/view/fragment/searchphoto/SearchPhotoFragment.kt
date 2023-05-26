package com.betasoft.appdemo.view.fragment.searchphoto

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentSearchPhotoBinding
import com.betasoft.appdemo.view.base.AbsBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPhotoFragment : AbsBaseFragment<FragmentSearchPhotoBinding>() {
    private val mViewModel by activityViewModels<SearchPhotoViewModel>()
    private var searchJob: Job? = null

    private val photoCompressionAdapter by lazy {
        PhotoCompressionAdapter()
    }
    override fun getLayout(): Int {
        return R.layout.fragment_search_photo
    }

    override fun initView() {

        initRecycleView()
        searchJob = lifecycleScope.launch {
            mViewModel.listPhotos.collectLatest { response ->
                photoCompressionAdapter.submitData(response)
            }
        }

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        mViewModel.searchPhotos(newText.toString().trim())
                        mViewModel.listPhotos.collectLatest { response ->
                            photoCompressionAdapter.submitData(response)
                        }
                    }

                } else {

                }
                return false
            }

        })

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