package com.betasoft.appdemo.ui.myfile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.databinding.FragmentMyFileBinding
import com.betasoft.appdemo.ui.adpter.MyFileAdapter
import com.betasoft.appdemo.ui.base.AbsBaseFragment
import com.betasoft.appdemo.ui.home.HomeFragmentDirections
import com.betasoft.appdemo.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFileFragment : AbsBaseFragment<FragmentMyFileBinding>(),
    MyFileAdapter.OnClickItemListeners {
    @Inject
    lateinit var myFileAdapter: MyFileAdapter

    private val mViewModel: MyFileViewModel by viewModels()

    private lateinit var mLayoutManager: LinearLayoutManager

    override fun getLayout(): Int {
        return R.layout.fragment_my_file
    }

    override fun initView() {
        initRecycleView()
        configureStateListener()
        observer()
        refreshData()
        binding.mySwipeRefreshLayout.setOnRefreshListener {
            binding.mySwipeRefreshLayout.isEnabled = true
            binding.mySwipeRefreshLayout.isRefreshing = true
            refreshData()
        }



        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    searchImageLocal(newText.trim())
                } else {
                    refreshData()
                }
                return false
            }

        })

    }


    private fun observer() {

    }

    private fun initRecycleView() {
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rV.apply {
            setHasFixedSize(true)
            adapter = myFileAdapter
            layoutManager = mLayoutManager
        }
        myFileAdapter.setListener(this)

    }

    override fun onClickItemListeners(imageLocal: ImageLocal) {
        findNavController().navigate(HomeFragmentDirections.actionGlobalDetailImageLocalFragment(imageLocal))
    }

    private fun configureStateListener() {
        myFileAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            ) {
                // Show ProgressBar
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.mySwipeRefreshLayout.isEnabled = false
                binding.mySwipeRefreshLayout.isRefreshing = false
            } else {
                // Hide ProgressBar
                binding.shimmerLayout.visibility = View.GONE
                binding.mySwipeRefreshLayout.isEnabled = true
                binding.mySwipeRefreshLayout.isRefreshing = false
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    binding.shimmerLayout.visibility = View.GONE

                }

            }
        }
    }
    ////

    private fun refreshData() {
        lifecycleScope.launch {
            mViewModel.getAllImageLocal().collectLatest { response ->
                Timber.d("onCreate: $response")
                myFileAdapter.submitData(response)
            }
        }
    }

    private fun searchImageLocal(searchQuery: String) {
        lifecycleScope.launch {
            mViewModel.searchImageLocal(searchQuery).collectLatest { response ->
                myFileAdapter.submitData(response)
            }
        }
    }

}